package com.example.Entity;

//check ticket id if null make sure to convert into int value 0
public class SeatDetails {
    protected int seatid;
    protected String seatrow;
    protected int seatnumber;
    protected int tierid;


    public SeatDetails() {
    }

    public SeatDetails(int seatid, String seatrow, int seatnumber, int tierid) {
        this.seatid = seatid;
        this.seatrow = seatrow;
        this.seatnumber = seatnumber;
        this.tierid = tierid;
    }

    public int getSeatid() {
        return this.seatid;
    }

    public void setSeatid(int seatid) {
        this.seatid = seatid;
    }

    public String getSeatrow() {
        return this.seatrow;
    }

    public void setSeatrow(String seatrow) {
        this.seatrow = seatrow;
    }

    public int getSeatnumber() {
        return this.seatnumber;
    }

    public void setSeatnumber(int seatnumber) {
        this.seatnumber = seatnumber;
    }

    public int getTierid() {
        return this.tierid;
    }

    public void setTierid(int tierid) {
        this.tierid = tierid;
    }

    @Override
    public String toString() {
        return "{" +
            " seatid='" + getSeatid() + "'" +
            ", seatrow='" + getSeatrow() + "'" +
            ", seatnumber='" + getSeatnumber() + "'" +
            ", tierid='" + getTierid() + "'" +
            "}";
    }

    
    
}