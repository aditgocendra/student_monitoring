package com.ark.studentmonitoring.Model;

public class ModelStudentClass {

    private String student_class;
    private String sub_student_class;
    private String year_school;
    private String teacher;
    private String key;

    public ModelStudentClass(){

    }

    public ModelStudentClass(String student_class, String sub_student_class, String year_school, String teacher) {
        this.student_class = student_class;
        this.sub_student_class = sub_student_class;
        this.year_school = year_school;
        this.teacher = teacher;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getSub_student_class() {
        return sub_student_class;
    }

    public void setSub_student_class(String sub_student_class) {
        this.sub_student_class = sub_student_class;
    }

    public String getYear_school() {
        return year_school;
    }

    public void setYear_school(String year_school) {
        this.year_school = year_school;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
