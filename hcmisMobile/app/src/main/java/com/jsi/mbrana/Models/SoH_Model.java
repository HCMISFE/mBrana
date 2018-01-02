package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 7/4/2016.
 */
public class SoH_Model {
    private int ItemId;
    private int UnitId;
    private int ActivityID;
    private int SOH;
    private int UsableSOH;
    private Double AMC;
    private Double AWC;
    private Double MOS;
    private Double WOS;
    private int GIT;
    private String GITMOS;
    private double Ordered;
    private double OrderedMos;
    private String ProductCN;
    private String fullitemname;
    private String Unit;
    private int ExpiredQuantity;
    private int NearExpiredQuantity;
    private int VVMExpired;

    public int getVVMExpired() {
        return VVMExpired;
    }

    public void setVVMExpired(int VVMExpired) {
        this.VVMExpired = VVMExpired;
    }

    public Double getWOS() {
        return WOS;
    }

    public void setWOS(Double WOS) {
        this.WOS = WOS;
    }

    public Double getAWC() {

        return AWC;
    }

    public void setAWC(Double AWC) {
        this.AWC = AWC;
    }

    public int getUsableSOH() {
        return UsableSOH;
    }

    public void setUsableSOH(int usableSOH) {
        UsableSOH = usableSOH;
    }

    public int getExpiredQuantity() {
        return ExpiredQuantity;
    }

    public void setExpiredQuantity(int expiredQuantity) {
        ExpiredQuantity = expiredQuantity;
    }

    public int getNearExpiredQuantity() {
        return NearExpiredQuantity;
    }

    public void setNearExpiredQuantity(int nearExpiredQuantity) {
        NearExpiredQuantity = nearExpiredQuantity;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public int getUnitId() {
        return UnitId;
    }

    public void setUnitId(int unitId) {
        UnitId = unitId;
    }

    public int getActivityID() {
        return ActivityID;
    }

    public void setActivityID(int activityID) {
        ActivityID = activityID;
    }

    public int getSOH() {
        return SOH;
    }

    public void setSOH(int SOH) {
        this.SOH = SOH;
    }

    public Double getAMC() {
        return AMC;
    }

    public void setAMC(Double AMC) {
        this.AMC = AMC;
    }

    public Double getMOS() {
        return MOS;
    }

    public void setMOS(Double MOS) {
        this.MOS = MOS;
    }

    public int getGIT() {
        return GIT;
    }

    public void setGIT(int GIT) {
        this.GIT = GIT;
    }

    public String getGITMOS() {
        return GITMOS;
    }

    public void setGITMOS(String GITMOS) {
        this.GITMOS = GITMOS;
    }

    public double getOrdered() {
        return Ordered;
    }

    public void setOrdered(double ordered) {
        Ordered = ordered;
    }

    public double getOrderedMos() {
        return OrderedMos;
    }

    public void setOrderedMos(double orderedMos) {
        OrderedMos = orderedMos;
    }

    public String getProductCN() {
        return ProductCN;
    }

    public void setProductCN(String productCN) {
        ProductCN = productCN;
    }

    public String getFullitemname() {
        return fullitemname;
    }

    public void setFullitemname(String fullitemname) {
        this.fullitemname = fullitemname;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }
}
