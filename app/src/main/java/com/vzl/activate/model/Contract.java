package com.vzl.activate.model;

public class Contract {

    private int mDiscount;
    private int mAmount;
    private long mDate;

    public Contract() {
    }

    public Contract(long date, int discount, int amount) {
        mDate = date;
        mDiscount = discount;
        mAmount = amount;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public int getDiscount() {
        return mDiscount;
    }

    public void setDiscount(int discount) {
        mDiscount = discount;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }
}
