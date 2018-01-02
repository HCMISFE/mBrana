package com.jsi.mbrana.Modules.Receive.Model.ReceiptInvoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Surafel Nigussie on 12/18/2017.
 */

public class ReceiveInvoiceDetailModel {
    @SerializedName("ReceiveInvoiceDetailId")
    @Expose
    private Integer receiveInvoiceDetailId;
    @SerializedName("ItemUnitId")
    @Expose
    private Integer itemUnitId;
    @SerializedName("FullItemName")
    @Expose
    private String fullItemName;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("UnitOfIssue")
    @Expose
    private String unitOfIssue;
    @SerializedName("ManufacturerName")
    @Expose
    private String manufacturerName;
    @SerializedName("ManufacturerId")
    @Expose
    private Integer manufacturerId;
    @SerializedName("ExpiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("BatchNumber")
    @Expose
    private String batchNumber;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("ReceivedQuantity")
    @Expose
    private Integer receivedQuantity;

    public Integer getReceiveInvoiceDetailId() {
        return receiveInvoiceDetailId;
    }

    public void setReceiveInvoiceDetailId(Integer receiveInvoiceDetailId) {
        this.receiveInvoiceDetailId = receiveInvoiceDetailId;
    }

    public Integer getItemUnitId() {
        return itemUnitId;
    }

    public void setItemUnitId(Integer itemUnitId) {
        this.itemUnitId = itemUnitId;
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitOfIssue() {
        return unitOfIssue;
    }

    public void setUnitOfIssue(String unitOfIssue) {
        this.unitOfIssue = unitOfIssue;
    }

    public String getManufacturer() {
        return manufacturerName;
    }

    public void setManufacturer(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getExpDate() {
        return expiryDate;
    }

    public void setExpDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    @Override
    public String toString() {
        return "ReceiveInvoiceDetailModel{" +
                "receiveInvoiceDetailId=" + receiveInvoiceDetailId +
                ", itemUnitId=" + itemUnitId +
                ", fullItemName='" + fullItemName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", unitOfIssue='" + unitOfIssue + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", manufacturerId=" + manufacturerId +
                ", expiryDate='" + expiryDate + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", quantity=" + quantity +
                ", receivedQuantity=" + receivedQuantity +
                '}';
    }
}
