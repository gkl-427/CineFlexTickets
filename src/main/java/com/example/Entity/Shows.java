package com.example.Entity;

import java.math.BigInteger;
import java.sql.Date;

public class Shows {

 
    protected int showid;
    protected int shownumber;
    protected BigInteger showstarttime;
    protected Date showdate;
    protected boolean showstatus;
    protected int theatreid;
    protected int movieid;


    public Shows(int showid, int shownumber, BigInteger showstarttime, Date showdate, boolean showstatus, int theatreid, int movieid) {
        this.showid = showid;
        this.shownumber = shownumber;
        this.showstarttime = showstarttime;
        this.showdate = showdate;
        this.showstatus = showstatus;
        this.theatreid = theatreid;
        this.movieid = movieid;
    }

    public Shows() {
    }
   

    public int getShowid() {
        return this.showid;
    }

    public void setShowid(int showid) {
        this.showid = showid;
    }

    public int getShownumber() {
        return this.shownumber;
    }

    public void setShownumber(int shownumber) {
        this.shownumber = shownumber;
    }

    public BigInteger getShowstarttime() {
        return this.showstarttime;
    }

    public void setShowstarttime(BigInteger showstarttime) {
        this.showstarttime = showstarttime;
    }

    public Date getShowdate() {
        return this.showdate;
    }

    public void setShowdate(Date showdate) {
        this.showdate = showdate;
    }

    public boolean isShowstatus() {
        return this.showstatus;
    }

    public boolean getShowstatus() {
        return this.showstatus;
    }

    public void setShowstatus(boolean showstatus) {
        this.showstatus = showstatus;
    }

    public int getTheatreid() {
        return this.theatreid;
    }

    public void setTheatreid(int theatreid) {
        this.theatreid = theatreid;
    }

    public int getMovieid() {
        return this.movieid;
    }

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    @Override
    public String toString() {
        return "{" +
            " showid='" + getShowid() + "'" +
            ", shownumber='" + getShownumber() + "'" +
            ", showstarttime='" + getShowstarttime() + "'" +
            ", showdate='" + getShowdate() + "'" +
            ", showstatus='" + isShowstatus() + "'" +
            ", theatreid='" + getTheatreid() + "'" +
            ", movieid='" + getMovieid() + "'" +
            "}";
    }

}