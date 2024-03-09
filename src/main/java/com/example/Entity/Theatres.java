package com.example.Entity;

public class Theatres{
    protected  int theatreid;
    protected String theatrename;
    protected  String theatrecity;
    protected String features;
    protected int userid;


    public Theatres(int theatreid, String theatrename, String theatrecity, String features, int userid) {
        this.theatreid = theatreid;
        this.theatrename = theatrename;
        this.theatrecity = theatrecity;
        this.features = features;
        this.userid = userid;
    }
    
    public Theatres() {
    }
    

    public int getTheatreid() {
        return this.theatreid;
    }

    public void setTheatreid(int theatreid) {
        this.theatreid = theatreid;
    }

    public String getTheatrename() {
        return this.theatrename;
    }

    public void setTheatrename(String theatrename) {
        this.theatrename = theatrename;
    }

    public String getTheatrecity() {
        return this.theatrecity;
    }

    public void setTheatrecity(String theatrecity) {
        this.theatrecity = theatrecity;
    }

    public String getFeatures() {
        return this.features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    
    @Override
    public String toString() {
        return "{" +
            " theatreid='" + getTheatreid() + "'" +
            ", theatrename='" + getTheatrename() + "'" +
            ", theatrecity='" + getTheatrecity() + "'" +
            ", features='" + getFeatures() + "'" +
            ", managerid='" + getUserid() + "'" +
            "}";
    }
}