package com.example.Entity;

import java.math.BigInteger;

public class Bookings {
    
    protected int ticketid;
    protected double amount;
    protected int bookingstatus;
    protected int userid;
    protected int showid;
    protected int seatcount;
    protected int transactionid;
    protected BigInteger cancellationtime;


    public Bookings() {
    }
       

    public Bookings(int ticketid, double amount, int bookingstatus, int userid, int showid, int seatcount, int transactionid, BigInteger cancellationtime) {
        this.ticketid = ticketid;
        this.amount = amount;
        this.bookingstatus = bookingstatus;
        this.userid = userid;
        this.showid = showid;
        this.seatcount = seatcount;
        this.transactionid = transactionid;
        this.cancellationtime = cancellationtime;
    }

    public int getTicketid() {
        return this.ticketid;
    }

    public void setTicketid(int ticketid) {
        this.ticketid = ticketid;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getBookingstatus() {
        return this.bookingstatus;
    }

    public void setBookingstatus(int bookingstatus) {
        this.bookingstatus = bookingstatus;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getShowid() {
        return this.showid;
    }

    public void setShowid(int showid) {
        this.showid = showid;
    }

    public int getSeatcount() {
        return this.seatcount;
    }

    public void setSeatcount(int seatcount) {
        this.seatcount = seatcount;
    }

    public int getTransactionid() {
        return this.transactionid;
    }

    public void setTransactionid(int transactionid) {
        this.transactionid = transactionid;
    }

    public BigInteger getCancellationtime() {
        return this.cancellationtime;
    }

    public void setCancellationtime(BigInteger cancellationtime) {
        this.cancellationtime = cancellationtime;
    }

    @Override
    public String toString() {
        return "{" +
            " ticketid='" + getTicketid() + "'" +
            ", amount='" + getAmount() + "'" +
            ", bookingstatus='" + getBookingstatus() + "'" +
            ", userid='" + getUserid() + "'" +
            ", showid='" + getShowid() + "'" +
            ", seatcount='" + getSeatcount() + "'" +
            ", transactionid='" + getTransactionid() + "'" +
            ", cancellationtime='" + getCancellationtime() + "'" +
            "}";
    }
    



}