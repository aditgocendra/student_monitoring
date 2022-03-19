package com.ark.studentmonitoring.Model;

public class ModelChat {

    private String uid1;
    private String uid2;
    private String key;

    public ModelChat() {
    }

    public ModelChat(String uid1, String uid2) {
        this.uid1 = uid1;
        this.uid2 = uid2;
    }

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
