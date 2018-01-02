package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 6/15/2016.
 */
public class ReceiveInvoiceMastersDetailModel {
    private int ID;
    private int ManufacturerId;
    private String FullItemName;
    private String ProductCN;
    private String Unit;
    private String Manufacturer;
    private String Model19;
    private String BatchNo;
    private String ExpDate;
    private Double InvoicedQuantity;
    private Double Quantity;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getManufacturerId() {
        return ManufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        ManufacturerId = manufacturerId;
    }

    public String getFullItemName() {
        return FullItemName;
    }

    public void setFullItemName(String fullItemName) {
        FullItemName = fullItemName;
    }

    public String getProductCN() {
        return ProductCN;
    }

    public void setProductCN(String productCN) {
        ProductCN = productCN;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getModel19() {
        return Model19;
    }

    public void setModel19(String model19) {
        Model19 = model19;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    public Double getInvoicedQuantity() {
        return InvoicedQuantity;
    }

    public void setInvoicedQuantity(Double invoicedQuantity) {
        InvoicedQuantity = invoicedQuantity;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }
}
