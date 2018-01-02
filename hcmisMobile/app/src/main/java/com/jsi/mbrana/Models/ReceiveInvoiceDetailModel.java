package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 6/15/2016.
 */
public class ReceiveInvoiceDetailModel {

    private int ID;
    private int NoOfItems;
    private String STVOrInvoiceNo;
    private String Supplier;
    private String ReceiptStatus;
    private String ReceiptStatusCode;
    private String Model19;
    private String ReceiptDate;
    private String DocumentType;
    private String DocumentTypesCode;
    private String FullName;
    private Double GRNFNumber;
    private String ModifiedDate;
    private boolean isCampain;
    public ReceiveInvoiceDetailModel(){}
    public ReceiveInvoiceDetailModel(int NoOfItems, int ID,String STVOrInvoiceNo,
                               String Supplier,String ReceiptStatus,
                               String Model19,String ReceiptDate,String DocumentType,String FullName,
                                     Double GRNFNumber){
        this.STVOrInvoiceNo=STVOrInvoiceNo;
        this.Supplier=Supplier;
        this.ReceiptStatus=ReceiptStatus;
        this.Model19=Model19;
        this.ReceiptDate=ReceiptDate;
        this.DocumentType=DocumentType;
        this.FullName=FullName;
        this.GRNFNumber=GRNFNumber;
        this.ID=ID;
        this.NoOfItems=NoOfItems;

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

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getReceiptStatusCode() {
        return ReceiptStatusCode;
    }

    public void setReceiptStatusCode(String receiptStatusCode) {
        ReceiptStatusCode = receiptStatusCode;
    }

    public String getReceiptDate() {
        return ReceiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        ReceiptDate = receiptDate;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public Double getGRNFNumber() {
        return GRNFNumber;
    }

    public void setGRNFNumber(Double GRNFNumber) {
        this.GRNFNumber = GRNFNumber;
    }

    public String getDocumentTypesCode() {
        return DocumentTypesCode;
    }

    public void setDocumentTypesCode(String documentTypesCode) {
        DocumentTypesCode = documentTypesCode;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public boolean isCampain() {
        return isCampain;
    }

    public void setCampain(boolean campain) {
        isCampain = campain;
    }
}
