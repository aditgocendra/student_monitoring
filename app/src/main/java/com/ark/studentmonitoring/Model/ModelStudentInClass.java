package com.ark.studentmonitoring.Model;

public class ModelStudentInClass {

    private String key_student;
    private String key;

    public ModelStudentInClass() {
    }

    public ModelStudentInClass(String key_student) {
        this.key_student = key_student;
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
