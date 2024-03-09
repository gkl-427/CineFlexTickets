package com.example.Entity;

import java.math.*;

public class TransactionDetails {

    protected int transactionid;
    protected String paymentmode;
    protected boolean paymentstatus;
    protected double amount;
    protected BigInteger paymenttime;

    public TransactionDetails() {
    }

    public TransactionDetails(int transactionid, String paymentmode, boolean paymentstatus, double amount, BigInteger paymenttime) {
        this.transactionid = transactionid;
        this.paymentmode = paymentmode;
        this.paymentstatus = paymentstatus;
        this.amount = amount;
        this.paymenttime = paymenttime;
    }


    public int getTransactionid() {
        return this.transactionid;
    }

    public void setTransactionid(int transactionid) {
        this.transactionid = transactionid;
    }

    public String getPaymentmode() {
        return this.paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public boolean isPaymentstatus() {
        return this.paymentstatus;
    }

    public boolean getPaymentstatus() {
        return this.paymentstatus;
    }

    public void setPaymentstatus(boolean paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BigInteger getPaymenttime() {
        return this.paymenttime;
    }

    public void setPaymenttime(BigInteger paymenttime) {
        this.paymenttime = paymenttime;
    }

    @Override
    public String toString() {
        return "{" +
            " transactionid='" + getTransactionid() + "'" +
            ", paymentmode='" + getPaymentmode() + "'" +
            ", paymentstatus='" + isPaymentstatus() + "'" +
            ", amount='" + getAmount() + "'" +
            ", paymenttime='" + getPaymenttime() + "'" +
            "}";
    }
   
}