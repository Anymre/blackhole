/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.raft;


import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Raft peer.
 *
 * @author nacos
 */
public class RaftPeer {
    
    public String ip;
    
    public String voteFor;
    
    public AtomicLong term = new AtomicLong(0L);
    
    private static final Random RANDOM = new Random();
    
    public static final long HEARTBEAT_INTERVAL_MS = TimeUnit.SECONDS.toMillis(5L);
    
    public static final long LEADER_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(15L);
    
    public static final long RANDOM_MS = TimeUnit.SECONDS.toMillis(5L);
    
    public volatile long leaderDueMs = nextLong(0, LEADER_TIMEOUT_MS);
    
    public volatile long heartbeatDueMs = nextLong(0, HEARTBEAT_INTERVAL_MS);
    
    public static final long TICK_PERIOD_MS = TimeUnit.MILLISECONDS.toMillis(500L);
    
    
    
    public volatile State state = State.FOLLOWER;
    
    public void resetLeaderDue() {
        leaderDueMs = LEADER_TIMEOUT_MS + nextLong(0, RANDOM_MS);
    }
    
    public void resetHeartbeatDue() {
        heartbeatDueMs = HEARTBEAT_INTERVAL_MS;
    }
    
    public enum State {
        /**
         * Leader of the cluster, only one leader stands in a cluster.
         */
        LEADER,
        /**
         * Follower of the cluster, report to and copy from leader.
         */
        FOLLOWER,
        /**
         * Candidate leader to be elected.
         */
        CANDIDATE
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (!(obj instanceof RaftPeer)) {
            return false;
        }
        
        RaftPeer other = (RaftPeer) obj;
        
        return ip.equals(other.ip);
    }
    
    @Override
    public String toString() {
        return "RaftPeer{" + "ip='" + ip + '\'' + ", voteFor='" + voteFor + '\'' + ", term=" + term + ", leaderDueMs="
                + leaderDueMs + ", heartbeatDueMs=" + heartbeatDueMs + ", state=" + state + '}';
    }
    
    public static long nextLong(long startInclusive, long endExclusive) {
        return startInclusive == endExclusive ? startInclusive
                : (long) nextDouble((double) startInclusive, (double) endExclusive);
    }
    
    public static double nextDouble(double startInclusive, double endInclusive) {
        return startInclusive == endInclusive ? startInclusive
                : startInclusive + (endInclusive - startInclusive) * RANDOM.nextDouble();
    }
}
