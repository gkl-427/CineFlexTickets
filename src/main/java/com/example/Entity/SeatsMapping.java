package com.example.Entity;

public class SeatsMapping {
    protected int showid;
    protected int seatid;
    protected int ticketid;

    public SeatsMapping() {
    }

    public SeatsMapping(int showid, int seatid, int ticketid) {
        this.showid = showid;
        this.seatid = seatid;
        this.ticketid = ticketid;
    }


    public int getShowid() {
        return this.showid;
    }

    public void setShowid(int showid) {
        this.showid = showid;
    }

    public int getSeatid() {
        return this.seatid;
    }

    public void setSeatid(int seatid) {
        this.seatid = seatid;
    }

    public int getTicketid() {
        return this.ticketid;
    }

    public void setTicketid(int ticketid) {
        this.ticketid = ticketid;
    }
   
    @Override
    public String toString() {
        return "{" +
            " showid='" + getShowid() + "'" +
            ", seatid='" + getSeatid() + "'" +
            ", ticketid='" + getTicketid() + "'" +
            "}";
    }

}