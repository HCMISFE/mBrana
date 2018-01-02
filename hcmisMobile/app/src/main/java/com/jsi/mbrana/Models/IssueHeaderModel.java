package com.jsi.mbrana.Models;

/**
 * Created by pc-6 on 7/4/2016.
 */
public class IssueHeaderModel {
    private int ID;
    private String PrintedDate;
    private String ModifiedDate;
    private int PickListID;
    private int UserID;
    private int StoreID;
    private int IDPrinted;
    private String IsVoided;
    private int FiscalYearID;
    private int AccountID;
    private int DocumentTypeID;
    private String rowguid;
    private String IssNo;
    private String IsSynced;
    private int EnvironmentID;
    private String EnvironmentCode;
    private int OrderID;

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPrintedDate(String printedDate) {
        PrintedDate = printedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public void setPickListID(int pickListID) {
        PickListID = pickListID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public void setIDPrinted(int IDPrinted) {
        this.IDPrinted = IDPrinted;
    }

    public void setIsVoided(String isVoided) {
        IsVoided = isVoided;
    }

    public void setFiscalYearID(int fiscalYearID) {
        FiscalYearID = fiscalYearID;
    }

    public void setAccountID(int accountID) {
        AccountID = accountID;
    }

    public void setDocumentTypeID(int documentTypeID) {
        DocumentTypeID = documentTypeID;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public void setIssNo(String issNo) {
        IssNo = issNo;
    }

    public void setIsSynced(String isSynced) {
        IsSynced = isSynced;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }

    public int getID() {
        return ID;
    }

    public String getPrintedDate() {
        return PrintedDate;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public int getPickListID() {
        return PickListID;
    }

    public int getUserID() {
        return UserID;
    }

    public int getStoreID() {
        return StoreID;
    }

    public int getIDPrinted() {
        return IDPrinted;
    }

    public String getIsVoided() {
        return IsVoided;
    }

    public int getFiscalYearID() {
        return FiscalYearID;
    }

    public int getAccountID() {
        return AccountID;
    }

    public int getDocumentTypeID() {
        return DocumentTypeID;
    }

    public String getRowguid() {
        return rowguid;
    }

    public String getIssNo() {
        return IssNo;
    }

    public String getIsSynced() {
        return IsSynced;
    }

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }
}
