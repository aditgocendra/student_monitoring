package com.ark.studentmonitoring.Model;

public class ModelStudent {
    private long index_student;
    private String name;
    private String nisn;
    private String class_now;
    private String age;
    private String gender;
    private String diagnosa;
    private String key;

    public ModelStudent(){

    }

    public ModelStudent(long index_student, String name, String nisn, String class_now, String age, String gender, String diagnosa) {
        this.index_student = index_student;
        this.name = name;
        this.nisn = nisn;
        this.class_now = class_now;
        this.age = age;
        this.gender = gender;
        this.diagnosa = diagnosa;
    }

    public long getIndex_student() {
        return index_student;
    }

    public void setIndex_student(long index_student) {
        this.index_student = index_student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getClass_now() {
        return class_now;
    }

    public void setClass_now(String class_now) {
        this.class_now = class_now;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiagnosa() {
        return diagnosa;
    }

    public void setDiagnosa(String diagnosa) {
        this.diagnosa = diagnosa;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
