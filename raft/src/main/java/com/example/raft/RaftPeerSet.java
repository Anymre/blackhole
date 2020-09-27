package com.example.raft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Marcus lv
 * @date 2020/9/27 15:42
 */
@Component
public class RaftPeerSet {
    
    private static final Logger LOG = LoggerFactory.getLogger(RaftPeerSet.class);
    
    private AtomicLong localTerm = new AtomicLong(0L);
    
    private RaftPeer leader = null;
    
    private volatile boolean ready = false;
    
    private volatile Map<String, RaftPeer> peers = new HashMap<>(8);
    
    @Value(value = "${server.port}")
    private Integer port;
    
    @Value(value = "${members}")
    private Set<String> members;
    
    @PostConstruct
    public void init() {
        for (String port : members) {
            String address = "127.0.0.1:" + port;
            
            RaftPeer raftPeer = new RaftPeer();
            raftPeer.ip = address;
            
            if (address.equals(getLocalAddress())) {
                raftPeer.term.set(localTerm.get());
            }
            peers.put(address, raftPeer);
        }
        ready = true;
        LOG.info("raft peers ready, {}", peers.toString());
    }
    
    public RaftPeer get(String server) {
        return peers.get(server);
    }
    
    public RaftPeer getLeader() {
        return leader;
    }
    
    public boolean isReady() {
        return ready;
    }
    
    public RaftPeer update(RaftPeer peer) {
        peers.put(peer.ip, peer);
        return peer;
    }
    
    public boolean isLeader(String ip) {
        if (leader == null) {
            LOG.warn("[IS LEADER] no leader is available now!");
            return false;
        }
        
        return ip.equals(leader.ip);
    }
    
    public Set<String> allServersWithoutMySelf() {
        Set<String> servers = new HashSet<String>(peers.keySet());
        
        // exclude myself
        servers.remove(local().ip);
        
        return servers;
    }
    
    public String getLocalAddress() {
        return "127.0.0.1:" + port;
    }
    
    public RaftPeer local() {
        
        RaftPeer peer = peers.get(getLocalAddress());
        
        if (peer == null) {
            throw new IllegalStateException(
                    "unable to find local peer , all peers: " + Arrays.toString(peers.keySet().toArray()));
        }
        
        return peer;
    }
    
    public void setTerm(long term) {
        localTerm.set(term);
    }
    
    public long getTerm() {
        return localTerm.get();
    }
    
    public void reset() {
        leader = null;
        for (RaftPeer peer : peers.values()) {
            peer.voteFor = null;
        }
    }
    
    public RaftPeer decideLeader(RaftPeer candidate) {
        peers.put(candidate.ip, candidate);
        
        Map<String, Integer> ips = new HashMap<>();
        
        int maxApproveCount = 0;
        String maxApprovePeer = null;
        
        for (RaftPeer peer : peers.values()) {
            if (StringUtils.isEmpty(peer.voteFor)) {
                continue;
            }
            int votes = ips.get(peer.voteFor) != null ? ips.get(peer.voteFor) : 0;
            votes++;
            ips.put(peer.voteFor, votes);
            
            if (votes > maxApproveCount) {
                maxApproveCount = votes;
                maxApprovePeer = peer.voteFor;
            }
        }
        
        if (maxApproveCount > peers.size() / 2 + 1) {
            RaftPeer peer = peers.get(maxApprovePeer);
            peer.state = RaftPeer.State.LEADER;
            if (!peer.equals(leader)) {
                leader = peer;
                LOG.info("{} has become the LEADER", leader.ip);
            }
            
        }
        
        return leader;
    }
}
