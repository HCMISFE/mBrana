package com.jsi.mbrana.Models;

/**
 * Created by user on 21/3/2017.
 */

public class RRFModel {
    int EnvironmentID = 0;
    String ProductCN = "";
    int ItemID = 0;
    int UnitOfIssueID = 0;
    String FullItemName = "";
    String Environment = "";
    String Unit = "";
    int BeginningBalance = 0;
    int QuantityReceived = 0;
    int DOS = 0;
    int Loss = 0;
    int Adjustment = 0;
    int EndingBalance = 0;
    Double WasteFactor = 0.0;
    int TargetCoverage = 0;
    int Dose = 0;
    int RequiredForNextSupplyPeriod = 0;
    int RequestedQuantity = 0;
    int Consumption = 0;
    int MaxStockQuantity = 0;
    int QuantityToReachMax = 0;

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getProductCN() {
        return ProductCN;
    }

    public void setProductCN(String productCN) {
        ProductCN = productCN;
    }

    public int getUnitOfIssueID() {
        return UnitOfIssueID;
    }

    public void setUnitOfIssueID(int unitOfIssueID) {
        UnitOfIssueID = unitOfIssueID;
    }

    public String getFullItemName() {
        return FullItemName;
    }

    public void setFullItemName(String fullItemName) {
        FullItemName = fullItemName;
    }

    public String getEnvironment() {
        return Environment;
    }

    public void setEnvironment(String environment) {
        Environment = environment;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getBeginningBalance() {
        return BeginningBalance;
    }

    public void setBeginningBalance(int beginningBalance) {
        BeginningBalance = beginningBalance;
    }

    public int getQuantityReceived() {
        return QuantityReceived;
    }

    public void setQuantityReceived(int quantityReceived) {
        QuantityReceived = quantityReceived;
    }

    public int getDOS() {
        return DOS;
    }

    public void setDOS(int DOS) {
        this.DOS = DOS;
    }

    public int getLoss() {
        return Loss;
    }

    public void setLoss(int loss) {
        Loss = loss;
    }

    public int getAdjustment() {
        return Adjustment;
    }

    public void setAdjustment(int adjustment) {
        Adjustment = adjustment;
    }

    public int getEndingBalance() {
        return EndingBalance;
    }

    public void setEndingBalance(int endingBalance) {
        EndingBalance = endingBalance;
    }

    public Double getWasteFactor() {
        return WasteFactor;
    }

    public void setWasteFactor(Double wasteFactor) {
        WasteFactor = wasteFactor;
    }

    public int getTargetCoverage() {
        return TargetCoverage;
    }

    public void setTargetCoverage(int targetCoverage) {
        TargetCoverage = targetCoverage;
    }

    public int getDose() {
        return Dose;
    }

    public void setDose(int dose) {
        Dose = dose;
    }

    public int getRequiredForNextSupplyPeriod() {
        return RequiredForNextSupplyPeriod;
    }

    public void setRequiredForNextSupplyPeriod(int requiredForNextSupplyPeriod) {
        RequiredForNextSupplyPeriod = requiredForNextSupplyPeriod;
    }

    public int getRequestedQuantity() {
        return RequestedQuantity;
    }

    public void setRequestedQuantity(int requestedQuantity) {
        RequestedQuantity = requestedQuantity;
    }

    public int getConsumption() {
        return Consumption;
    }

    public void setConsumption(int consumption) {
        Consumption = consumption;
    }

    public int getMaxStockQuantity() {
        return MaxStockQuantity;
    }

    public void setMaxStockQuantity(int maxStockQuantity) {
        MaxStockQuantity = maxStockQuantity;
    }

    public int getQuantityToReachMax() {
        return QuantityToReachMax;
    }

    public void setQuantityToReachMax(int quantityToReachMax) {
        QuantityToReachMax = quantityToReachMax;
    }
}
