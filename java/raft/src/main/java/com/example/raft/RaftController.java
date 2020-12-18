package com.example.raft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcus lv
 * @date 2020/9/27 16:37
 */
@RestController
@RequestMapping("/raft")
public class RaftController {
    
    @Autowired
    private RaftCore raftCore;
    
    @PostMapping("/vote")
    public RaftPeer vote(@RequestBody RaftPeer vote) {
        return raftCore.receivedVote(vote);
    }
    
    @PostMapping("/beat")
    public RaftPeer beat(@RequestBody RaftPeer beat) {
        return raftCore.receivedBeat(beat);
    }
}
