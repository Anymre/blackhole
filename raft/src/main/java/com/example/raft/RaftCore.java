package com.example.raft;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Marcus lv
 * @date 2020/9/27 15:07
 */
@Component
public class RaftCore {
    
    private static final Logger LOG = LoggerFactory.getLogger(RaftCore.class);
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private RestTemplate restTemplate;
    
    
    @Autowired
    private RaftPeerSet peers;
    
    
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, (i) -> {
        Thread thread = new Thread(i, "raft-thread");
        thread.setDaemon(true);
        return thread;
    });
    
    @PostConstruct
    public void init() {
        LOG.info("initializing Raft sub-system");
        
        executor.scheduleAtFixedRate(new MasterElection(), 0, 500, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(new MasterElection(), 0, 500, TimeUnit.MILLISECONDS);
        
        LOG.info("initializing finished");
    }
    
    public synchronized RaftPeer receivedVote(RaftPeer remote) {
        LOG.info("receivedVote {}", remote.toString());
        
        RaftPeer local = peers.local();
        if (remote.term.get() < local.term.get()) {
            String msg = "received illegitimate vote" + ", voter-term:" + remote.term + ", votee-term:" + local.term;
            LOG.info(msg);
            
            if (StringUtils.isEmpty(local.voteFor)) {
                local.voteFor = local.ip;
            }
            return local;
        }
        
        local.resetLeaderDue();
        
        local.state = RaftPeer.State.FOLLOWER;
        local.voteFor = remote.ip;
        local.term.set(remote.term.get());
        
        LOG.info("vote {} as leader, term: {}", remote.ip, remote.term);
        
        return local;
    }
    
    public class MasterElection implements Runnable {
        
        @Override
        public void run() {
            try {
                if (!peers.isReady()) {
                    return;
                }
                RaftPeer local = peers.local();
                local.leaderDueMs -= RaftPeer.TICK_PERIOD_MS;
                
                if (local.leaderDueMs > 0) {
                    return;
                }
                local.resetLeaderDue();
                local.resetHeartbeatDue();
                //vote
                sendVote();
            } catch (Exception ignore) {
            }
        }
    }
    
    private void sendVote() throws Exception {
        RaftPeer local = peers.local();
        LOG.info("leader timeout or not election, start voting, leader {}, term{}", peers.getLeader(), local.term);
        
        peers.reset();
        
        local.term.incrementAndGet();
        local.voteFor = local.ip;
        local.state = RaftPeer.State.CANDIDATE;
        
        Map<String, String> params = new HashMap<>(1);
        params.put("vote", objectMapper.writeValueAsString(local));
        
        for (String sever : peers.allServersWithoutMySelf()) {
            String url = "http://" + sever + "/raft/vote";
            try {
                RaftPeer peer = restTemplate.postForObject(url, local, RaftPeer.class);
                LOG.info("received approve from peer: {}", objectMapper.writeValueAsString(peer));
                
                peers.decideLeader(peer);
            } catch (Exception ignore) {
                LOG.error("error while sending vote to server: {}", sever);
            }
        }
    }
}
