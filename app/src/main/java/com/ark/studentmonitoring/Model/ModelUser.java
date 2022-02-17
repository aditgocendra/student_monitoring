package com.ark.studentmonitoring.Model;

public class ModelUser {

    private String username;
    private String email;
    private String role;
    private String phone_number;
    private String address;
    private String key;

    public ModelUser(){

    }

    public ModelUser(String username, String email, String role, String phone_number, String address) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.phone_number = phone_number;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
