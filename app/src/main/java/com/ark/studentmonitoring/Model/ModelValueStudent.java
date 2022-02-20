package com.ark.studentmonitoring.Model;

public class ModelValueStudent {
    private String date;
    private String value;
    private String description;
    private String classes;
    private String key;

    public ModelValueStudent(){

    }

    public ModelValueStudent(String date, String value, String description, String classes) {
        this.date = date;
        this.value = value;
        this.description = description;
        this.classes = classes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
