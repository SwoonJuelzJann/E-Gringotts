package com.example.egringotts;

import javafx.scene.image.Image;

public class account {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String userAddress;
    private String postcode;
    private String userType;
    private double balance_K;
    private double balance_S;
    private double balance_G;
    private boolean goblinStatus;
    private String avatar;
    private String email;
    private String pin;

    public account(String username, String password, String firstName, String lastName, String phoneNo, String userAddress, String postcode, String userType, double balance_K, double balance_S, double balance_G, boolean goblinStatus, String avatar, String email, String pin) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.userAddress = userAddress;
        this.postcode = postcode;
        this.userType = userType;
        this.balance_K = balance_K;
        this.balance_S = balance_S;
        this.balance_G = balance_G;
        this.goblinStatus = goblinStatus;
        this.avatar = avatar;
        this.email = email;
        this.pin = pin;
    }

    // Getters and setters for each instance variable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public double getBalance_K() {
        return balance_K;
    }

    public void setBalance_K(double balance_K) {
        this.balance_K = balance_K;
    }

    public double getBalance_S() {
        return balance_S;
    }

    public void setBalance_S(double balance_S) {
        this.balance_S = balance_S;
    }

    public double getBalance_G() {
        return balance_G;
    }

    public void setBalance_G(double balance_G) {
        this.balance_G = balance_G;
    }

    public boolean getGoblinStatus() {
        return goblinStatus;
    }

    public void setGoblinStatus(boolean goblinStatus) {
        this.goblinStatus = goblinStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}

