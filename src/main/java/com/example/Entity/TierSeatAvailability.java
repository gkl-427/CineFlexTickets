package com.example.Entity;

public class TierSeatAvailability{
    protected int  tierid;
    protected  int tiernumber;
    protected  double ticketprice;
    protected int theatreid;


    public TierSeatAvailability() {
    }

    public TierSeatAvailability(int tierid, int tiernumber, double ticketprice, int theatreid) {
        this.tierid = tierid;
        this.tiernumber = tiernumber;
        this.ticketprice = ticketprice;
        this.theatreid = theatreid;
    }

    public int getTierid() {
        return this.tierid;
    }

    public void setTierid(int tierid) {
        this.tierid = tierid;
    }

    public int getTiernumber() {
        return this.tiernumber;
    }

    public void setTiernumber(int tiernumber) {
        this.tiernumber = tiernumber;
    }

    public double getTicketprice() {
        return this.ticketprice;
    }

    public void setTicketprice(double ticketprice) {
        this.ticketprice = ticketprice;
    }

    public int getTheatreid() {
        return this.theatreid;
    }

    public void setTheatreid(int theatreid) {
        this.theatreid = theatreid;
    }
        

    @Override
    public String toString() {
        return "{" +
            " tierid='" + getTierid() + "'" +
            ", tiernumber='" + getTiernumber() + "'" +
            ", ticketprice='" + getTicketprice() + "'" +
            ", theatreid='" + getTheatreid() + "'" +
            "}";
    }
     
}    