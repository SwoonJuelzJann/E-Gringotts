package com.example.egringotts;

public class userType {
    private final int levelUpQuota;
    private double maxTransferK;
    private double maxTransferS;
    private double maxTransferG;
    private double exchangeFee;

    public userType(double maxTransferK, double maxTransferS, double maxTransferG, double exchangeFee, int tierUpQuota) {
        this.maxTransferK = maxTransferK;
        this.maxTransferS = maxTransferS;
        this.maxTransferG = maxTransferG;
        this.exchangeFee = exchangeFee;
        this.levelUpQuota = tierUpQuota;
    }

    public double getMaxTransferK() {
        return maxTransferK;
    }

    public void setMaxTransferK(double maxTransferK) {
        this.maxTransferK = maxTransferK;
    }

    public double getMaxTransferS() {
        return maxTransferS;
    }

    public void setMaxTransferS(double maxTransferS) {
        this.maxTransferS = maxTransferS;
    }

    public double getMaxTransferG() {
        return maxTransferG;
    }

    public void setMaxTransferG(double maxTransferG) {
        this.maxTransferG = maxTransferG;
    }

    public double getExchangeFee() {
        return exchangeFee;
    }

    public void setExchangeFee(double exchangeFee) {
        this.exchangeFee = exchangeFee;
    }

    public int getLevelUpQuota() {
        return levelUpQuota;
    }
}
