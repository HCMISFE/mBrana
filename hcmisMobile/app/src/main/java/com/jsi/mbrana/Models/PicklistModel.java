package com.jsi.mbrana.Models;

import java.util.Date;

/**
 * Created by pc-6 on 6/21/2016.
 */
public class PicklistModel {
    private String ProductCN;
    private String BatchNumber;
    private String ExpireDate;
    private String FullItemName;
    private int QuantityInBU;
    private int ItemID;
    private int UnitID;
    private int PickListId;
    private int OrderId;
    private int EnvironmentID;
    private String EnvironmentCode;
    private int ManufacturerID;
    private String Manufacturer;
    private String ManufacturerSH;
    private int PhysicalStoreID;
    private int QtyPerPack;
    private int PickListDetailId;
    private int ReceiveDocID;
    private int StoreID;
    private int Margin;
    private int UnitCost;
    private int SellingPrice;
    private String Unit;
    private String OrderStatusCode;
    private Date PickedDate;
    private int VVMID;
    private String VVMCode;

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setVVMID(int VVMID) {
        this.VVMID = VVMID;
    }

    public void setVVMCode(String VVMCode) {
        this.VVMCode = VVMCode;
    }

    public int getVVMID() {
        return VVMID;
    }

    public String getVVMCode() {
        return VVMCode;
    }

    public void setPickedDate(Date pickedDate) {
        PickedDate = pickedDate;
    }

    public Date getPickedDate() {
        return PickedDate;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        OrderStatusCode = orderStatusCode;
    }

    public String getOrderStatusCode() {
        return OrderStatusCode;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnit() {
        return Unit;
    }

    public void setProductCN(String productCN) {
        ProductCN = productCN;
    }

    public void setBatchNumber(String batchNumber) {
        BatchNumber = batchNumber;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public void setFullItemName(String fullItemName) {
        FullItemName = fullItemName;
    }

    public void setQuantityInBU(int quantityInBU) {
        QuantityInBU = quantityInBU;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public void setPickListId(int pickListId) {
        PickListId = pickListId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public void setEnvironmentID(int environmentID) {
        EnvironmentID = environmentID;
    }

    public void setEnvironmentCode(String environmentCode) {
        EnvironmentCode = environmentCode;
    }

    public void setManufacturerID(int manufacturerID) {
        ManufacturerID = manufacturerID;
    }

    public void setPhysicalStoreID(int physicalStoreID) {
        PhysicalStoreID = physicalStoreID;
    }

    public void setQtyPerPack(int qtyPerPack) {
        QtyPerPack = qtyPerPack;
    }

    public void setPickListDetailId(int pickListDetailId) {
        PickListDetailId = pickListDetailId;
    }

    public void setReceiveDocID(int receiveDocID) {
        ReceiveDocID = receiveDocID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public void setMargin(int margin) {
        Margin = margin;
    }

    public void setUnitCost(int unitCost) {
        UnitCost = unitCost;
    }

    public void setSellingPrice(int sellingPrice) {
        SellingPrice = sellingPrice;
    }

    public String getProductCN() {
        return ProductCN;
    }

    public String getBatchNumber() {
        return BatchNumber;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public String getFullItemName() {
        return FullItemName;
    }

    public int getQuantityInBU() {
        return QuantityInBU;
    }

    public int getItemID() {
        return ItemID;
    }

    public int getUnitID() {
        return UnitID;
    }

    public int getPickListId() {
        return PickListId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public int getEnvironmentID() {
        return EnvironmentID;
    }

    public String getEnvironmentCode() {
        return EnvironmentCode;
    }

    public int getManufacturerID() {
        return ManufacturerID;
    }

    public int getPhysicalStoreID() {
        return PhysicalStoreID;
    }

    public int getQtyPerPack() {
        return QtyPerPack;
    }

    public int getPickListDetailId() {
        return PickListDetailId;
    }

    public int getReceiveDocID() {
        return ReceiveDocID;
    }

    public int getStoreID() {
        return StoreID;
    }

    public int getMargin() {
        return Margin;
    }

    public int getUnitCost() {
        return UnitCost;
    }

    public int getSellingPrice() {
        return SellingPrice;
    }

    public String getManufacturerSH() {
        return ManufacturerSH;
    }

    public void setManufacturerSH(String manufacturerSH) {
        ManufacturerSH = manufacturerSH;
    }
}
