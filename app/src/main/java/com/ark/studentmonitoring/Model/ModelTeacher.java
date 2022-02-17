package com.ark.studentmonitoring.Model;

public class ModelTeacher {

    private String full_name;
    private String nip;
    private String key;

    public ModelTeacher(){

    }

    public ModelTeacher(String full_name, String nip) {
        this.full_name = full_name;
        this.nip = nip;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
