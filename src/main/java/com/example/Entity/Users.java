package com.example.Entity;


public class Users {

    protected int userid;
    protected int role;
    protected String firstname;
    protected String lastname;
    protected String address;
    protected String mobilenumber;
    protected String password;

    public Users() {

    }

    public Users(int userid, int role, String firstname, String lastname, String address, String mobilenumber,
            String password) {
        this.userid = userid;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.mobilenumber = mobilenumber;
        this.password = password;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                " userid='" + getUserid() + "'" +
                ", role='" + getRole() + "'" +
                ", firstname='" + getFirstname() + "'" +
                ", lastname='" + getLastname() + "'" +
                ", address='" + getAddress() + "'" +
                ", mobilenumber='" + getMobilenumber() + "'" +
                ", password='" + getPassword() + "'" +
                "}";
    }

}