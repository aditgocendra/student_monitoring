package com.ark.studentmonitoring.Model;

public class ModelMessage {
    private String message;
    private String timestamp;
    private String uidSender;
    private String uidReceiver;
    private boolean read;
    private String key;

    public ModelMessage() {
    }

    public ModelMessage(String message, String timestamp, String uidSender, String uidReceiver, boolean read) {
        this.message = message;
        this.timestamp = timestamp;
        this.uidSender = uidSender;
        this.uidReceiver = uidReceiver;
        this.read = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUidSender() {
        return uidSender;
    }

    public void setUidSender(String uidSender) {
        this.uidSender = uidSender;
    }

    public String getUidReceiver() {
        return uidReceiver;
    }

    public void setUidReceiver(String uidReceiver) {
        this.uidReceiver = uidReceiver;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
