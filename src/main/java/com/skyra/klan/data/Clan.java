package com.skyra.klan.data;

import java.util.*;

public class Clan {
    private String name;
    private String tag;
    private String leader;
    private List<String> members;
    private int maxMembers;
    private long createdAt;

    public Clan(String name, String tag, String leader) {
        this.name = name;
        this.tag = tag;
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader);
        this.maxMembers = 5;
        this.createdAt = System.currentTimeMillis();
    }

    // Gettery
    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getLeader() {
        return leader;
    }

    public List<String> getMembers() {
        return new ArrayList<>(members);
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    // Metody zarządzania członkami
    public void addMember(String playerName) {
        if (members.size() < maxMembers && !members.contains(playerName)) {
            members.add(playerName);
        }
    }

    public void removeMember(String playerName) {
        members.remove(playerName);
    }

    public boolean isMember(String playerName) {
        return members.contains(playerName);
    }

    public boolean isLeader(String playerName) {
        return leader.equals(playerName);
    }

    public int getMemberCount() {
        return members.size();
    }

    public boolean canAddMore() {
        return members.size() < maxMembers;
    }
}