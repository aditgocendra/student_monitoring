package com.ark.studentmonitoring.Model;

public class ModelParent {

    private String nisn;
    private String key_student;
    private String key;

    public ModelParent(){

    }

    public ModelParent(String nisn, String key_student) {
        this.nisn = nisn;
        this.key_student = key_student;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getKey_student() {
        return key_student;
    }

    public void setKey_student(String key_student) {
        this.key_student = key_student;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
