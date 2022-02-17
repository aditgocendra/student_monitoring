package com.ark.studentmonitoring.Model;

public class ModelYearSchool {

    private String from_year;
    private String to_year;
    private String key;

    public ModelYearSchool(){

    }

    public ModelYearSchool(String from_year, String to_year) {
        this.from_year = from_year;
        this.to_year = to_year;
    }

    public String getFrom_year() {
        return from_year;
    }

    public void setFrom_year(String from_year) {
        this.from_year = from_year;
    }

    public String getTo_year() {
        return to_year;
    }

    public void setTo_year(String to_year) {
        this.to_year = to_year;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
