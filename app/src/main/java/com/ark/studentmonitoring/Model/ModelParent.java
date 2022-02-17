package com.ark.studentmonitoring.Model;

public class ModelParent {

    private String child_name;
    private String nisn;
    private String key;

    public ModelParent(){

    }

    public ModelParent(String child_name, String nisn) {
        this.child_name = child_name;
        this.nisn = nisn;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
