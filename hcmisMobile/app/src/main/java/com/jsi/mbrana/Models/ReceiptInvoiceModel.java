package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 6/14/2016.
 */
public class ReceiptInvoiceModel {
    private int ReceiptInvoiceID;
    private int EnvironmentID;
    private int ID;
    private Double InvoicedQuantity;
    private Double ReceivedQty;
    private String EnvironmentCode;
    private int NoOfItems;
    private String DocumentTypesCode;
    private double Progress;
    private String STVOrInvoiceNo;
    private String STVOrInvoiceNoModifed;
    private String Supplier;
    private int SupplierID;
    private String ReceiptStatus;
    private String ReceiptStatusCode;
    private String Model19;
    private String PrintedDate;
    private String DocumentType;
    private String InvoiceType;
    private double InvoicedAmount;
    private String GRNF;
    private double ReceivedAmount;
    private String ModifiedDate;
    private boolean IsCampaign;
    private int UserID;
    private String UserName;
    private String ActivityCode;
    private int ActivityID;
    private String PurchaseOrderType;
    private String PurchaseOrderTypeCode;
    private String ReceiptDate;
    private int WarehouseID;
    private String Warehouse;

    public ReceiptInvoiceModel() {
    }

    public ReceiptInvoiceModel(int NoOfItems, int ReceiptInvoiceID, String STVOrInvoiceNo,
                               String Supplier, String ReceiptStatus,
                               String Model19, String PrintedDate, String DocumentType,
                               String InvoiceType, int InvoicedAmount, double ReceivedAmount) {
        this.STVOrInvoiceNo = STVOrInvoiceNo;
        this.Supplier = Supplier;
        this.ReceiptStatus = ReceiptStatus;
        this.Model19 = Model19;
        this.PrintedDate = PrintedDate;
        this.DocumentType = DocumentType;
        this.InvoiceType = InvoiceType;
        this.InvoicedAmount = InvoicedAmount;
        this.ReceivedAmount = ReceivedAmount;
        this.ReceiptInvoiceID = ReceiptInvoiceID;
        this.NoOfItems = NoOfItems;

    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public String getGRNF() {
        return GRNF;
    }

    public void setGRNF(String GRNF) {
        this.GRNF = GRNF;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public int getReceiptInvoiceID() {
        return ReceiptInvoiceID;
    }

    public void setReceiptInvoiceID(int receiptInvoiceID) {
        ReceiptInvoiceID = receiptInvoiceID;
    }

    public int getNoOfItems() {
        return NoOfItems;
    }

    public void setNoOfItems(int noOfItems) {
        NoOfItems = noOfItems;
    }

    public String getSTVOrInvoiceNo() {
        return STVOrInvoiceNo;
    }

    public void setSTVOrInvoiceNo(String STVOrInvoiceNo) {
        this.STVOrInvoiceNo = STVOrInvoiceNo;
    }

    public String getSupplier() {
        return Supplier;
    }

    public void setSupplier(String supplier) {
        Supplier = supplier;
    }

    public String getReceiptStatus() {
        return ReceiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        ReceiptStatus = receiptStatus;
    }

    public String getModel19() {
        return Model19;
    }

    public void setModel19(String model19) {
        Model19 = model19;
    }

    public String getPrintedDate() {
        return PrintedDate;
    }

    public void setPrintedDate(String printedDate) {
        PrintedDate = printedDate;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }

    public String getInvoiceType() {
        return InvoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        InvoiceType = invoiceType;
    }

    public double getInvoicedAmount() {
        return InvoicedAmount;
    }

    public void setInvoicedAmount(double invoicedAmount) {
        InvoicedAmount = invoicedAmount;
    }

    public double getReceivedAmount() {
        return ReceivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        ReceivedAmount = receivedAmount;
    }

    public String getSTVOrInvoiceNoModifed() {
        return STVOrInvoiceNoModifed;
    }

    public void setSTVOrInvoiceNoModifed(String STVOrInvoiceNoModifed) {
        this.STVOrInvoiceNoModifed = STVOrInvoiceNoModifed;
    }

    public double getProgress() {
        return Progress;
    }

    public void setProgress(double progress) {
        Progress = progress;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public String getReceiptStatusCode() {
        return ReceiptStatusCode;
    }

    public void setReceiptStatusCode(String receiptStatusCode) {
        ReceiptStatusCode = receiptStatusCode;
    }

    public Double getInvoicedQuantity() {
        return InvoicedQuantity;
    }

    public void setInvoicedQuantity(Double invoicedQuantity) {
        InvoicedQuantity = invoicedQuantity;
    }

    public Double getReceivedQty() {
        return ReceivedQty;
    }

    public void setReceivedQty(Double receivedQty) {
        ReceivedQty = receivedQty;
    }

    public String getDocumentTypesCode() {
        return DocumentTypesCode;
    }

    public void setDocumentTypesCode(String documentTypesCode) {
        DocumentTypesCode = documentTypesCode;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isCampaign() {
        return IsCampaign;
    }

    public void setCampaign(boolean campaign) {
        IsCampaign = campaign;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
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

    public String getPurchaseOrderTypeCode() {
        return PurchaseOrderTypeCode;
    }

    public void setPurchaseOrderTypeCode(String purchaseOrderTypeCode) {
        PurchaseOrderTypeCode = purchaseOrderTypeCode;
    }

    public String getPurchaseOrderType() {
        return PurchaseOrderType;
    }

    public void setPurchaseOrderType(String purchaseOrderType) {
        PurchaseOrderType = purchaseOrderType;
    }

    public String getReceiptDate() {
        return ReceiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        ReceiptDate = receiptDate;
    }

    public int getWarehouseID() {
        return WarehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        WarehouseID = warehouseID;
    }

    public String getWarehouse() {
        return Warehouse;
    }

    public void setWarehouse(String warehouse) {
        Warehouse = warehouse;
    }
}
