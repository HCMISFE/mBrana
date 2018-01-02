package com.jsi.mbrana.Models;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class ItemModel {
    public int GIT;
    public int OrderedQuantity;
    public String StockStatus;
    private String itemName;
    private String itemNameSH;
    private String itemCode;
    private String unit;
    private String bulkUnits;
    private String UnitOfIssue;
    private String transactionDate;
    private String EthiopianDate;
    private int itemSN;
    private int ItemID;
    private int UnitID;
    private int Quantity;
    private int SystemQuantity;
    private int ApprovedQuantity;
    private int issuedQuantity;
    private int ReceivedQuantity;
    private int SOH;
    private int AMC;
    private int MOS;
    private int WOS;
    private int POQuantity;
    private int UnitOfIssueID;
    private int PurchaseOrderDetailID;
    private int cStockOnHand;
    private int Balance;
    private int ActivityID;
    private String ActivityCode;
    private String ToFrom;
    private int ID;
    private Boolean IsVoid;
    private int BeginningBalance;
    private int QuantityReceived;
    private int Loss;
    private int EndingBalance;
    private double WasteFactor;
    private int TargetCoverage;
    private int Dose;
    private int RequiredForNextSupplyPeriod;
    private int Consumption;
    private int RequestedQuantity;

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

    public int getLoss() {
        return Loss;
    }

    public void setLoss(int loss) {
        Loss = loss;
    }

    public int getEndingBalance() {
        return EndingBalance;
    }

    public void setEndingBalance(int endingBalance) {
        EndingBalance = endingBalance;
    }

    public double getWasteFactor() {
        return WasteFactor;
    }

    public void setWasteFactor(double wasteFactor) {
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

    public int getConsumption() {
        return Consumption;
    }

    public void setConsumption(int consumption) {
        Consumption = consumption;
    }

    public int getRequestedQuantity() {
        return RequestedQuantity;
    }

    public void setRequestedQuantity(int requestedQuantity) {
        RequestedQuantity = requestedQuantity;
    }

    public int getSystemQuantity() {
        return SystemQuantity;
    }

    public void setSystemQuantity(int systemQuantity) {
        SystemQuantity = systemQuantity;
    }

    public Boolean getVoid() {
        return IsVoid;
    }

    public void setVoid(Boolean aVoid) {
        IsVoid = aVoid;
    }

    public String getEthiopianDate() {
        return EthiopianDate;
    }

    public void setEthiopianDate(String ethiopianDate) {
        EthiopianDate = ethiopianDate;
    }

    public String getToFrom() {
        return ToFrom;
    }

    public void setToFrom(String toFrom) {
        ToFrom = toFrom;
    }

    public String getStockStatus() {
        return StockStatus;
    }

    public void setStockStatus(String ss) {
        StockStatus = ss;
    }

    public int getWOS() {
        return WOS;
    }

    public void setWOS(int wos) {
        WOS = wos;
    }

    public int getGIT() {
        return GIT;
    }

    public void setGIT(int git) {
        GIT = git;
    }

    public int getOrderedQuantity() {
        return OrderedQuantity;
    }

    public void setOrderedQuantity(int ordered) {
        OrderedQuantity = ordered;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }

    public int getReceivedQuantity() {
        return ReceivedQuantity;
    }

    public void setReceivedQuantity(int receivedQuantity) {
        ReceivedQuantity = receivedQuantity;
    }

    public int getcStockOnHand() {
        return cStockOnHand;
    }

    public void setcStockOnHand(int cStockOnHand) {
        this.cStockOnHand = cStockOnHand;
    }

    public int getPurchaseOrderDetailID() {
        return PurchaseOrderDetailID;
    }

    public void setPurchaseOrderDetailID(int purchaseOrderDetailID) {
        PurchaseOrderDetailID = purchaseOrderDetailID;
    }

    public int getAMC() {
        return AMC;
    }

    public void setAMC(int AMC) {
        this.AMC = AMC;
    }

    public int getMOS() {
        return MOS;
    }

    public void setMOS(int MOS) {
        this.MOS = MOS;
    }

    public int getUnitOfIssueID() {
        return UnitOfIssueID;
    }

    public void setUnitOfIssueID(int unitOfIssueID) {
        UnitOfIssueID = unitOfIssueID;
    }

    public int getPOQuantity() {
        return POQuantity;
    }

    public void setPOQuantity(int POQuantity) {
        this.POQuantity = POQuantity;
    }

    public String getUnitOfIssue() {
        return UnitOfIssue;
    }

    public void setUnitOfIssue(String unitOfIssue) {
        UnitOfIssue = unitOfIssue;
    }

    public int getSOH() {
        return SOH;
    }

    public void setSOH(int SOH) {
        this.SOH = SOH;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        this.UnitID = unitID;
    }

    public String getBulkUnits() {
        return bulkUnits;
    }

    public void setBulkUnits(String bulkUnits) {
        this.bulkUnits = bulkUnits;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNameSH() {
        return itemNameSH;
    }

    public void setItemNameSH(String itemNameSH) {
        this.itemNameSH = itemNameSH;
    }

    public int getItemSN() {
        return itemSN;
    }

    public void setItemSN(int itemSN) {
        this.itemSN = itemSN;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        this.ItemID = itemID;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public int getApprovedQuantity() {
        return ApprovedQuantity;
    }

    public void setApprovedQuantity(int approvedQuantity) {
        this.ApprovedQuantity = approvedQuantity;
    }

    public int getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(int issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String activityCode) {
        ActivityCode = activityCode;
    }

    public int getActivityID() {
        return ActivityID;
    }

    public void setActivityID(int activityID) {
        ActivityID = activityID;
    }
}
