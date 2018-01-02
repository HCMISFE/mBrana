package com.jsi.mbrana.Modules.Receive.Model.ReceiptInvoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Surafel Nigussie on 12/18/2017.
 */

public class ReceiveInvoiceModel {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("InvoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("OrderId")
    @Expose
    private Integer orderId;
    @SerializedName("EnvironmentId")
    @Expose
    private Integer environmentId;
    @SerializedName("SupplierId")
    @Expose
    private Integer supplierId;
    @SerializedName("ActivityId")
    @Expose
    private Integer activityId;
    @SerializedName("ReceiveInvoiceDate")
    @Expose
    private String receiveInvoiceDate;
    @SerializedName("ReceiveInvoiceTypeId")
    @Expose
    private Integer receiveInvoiceTypeId;
    @SerializedName("ReceiveInvoiceTypeName")
    @Expose
    private String receiveInvoiceTypeName;
    @SerializedName("ReceiveInvoiceTypeCode")
    @Expose
    private String receiveInvoiceTypeCode;
    @SerializedName("Progress")
    @Expose
    private Integer progress;
    @SerializedName("ItemCount")
    @Expose
    private Integer itemCount;
    @SerializedName("ReceiveInvoiceDetail")
    @Expose
    private List<ReceiveInvoiceDetailModel> receiveInvoiceDetail = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Integer environmentId) {
        this.environmentId = environmentId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getReceiveInvoiceDate() {
        return receiveInvoiceDate;
    }

    public void setReceiveInvoiceDate(String receiveInvoiceDate) {
        this.receiveInvoiceDate = receiveInvoiceDate;
    }

    public Integer getReceiveInvoiceTypeId() {
        return receiveInvoiceTypeId;
    }

    public void setReceiveInvoiceTypeId(Integer receiveInvoiceTypeId) {
        this.receiveInvoiceTypeId = receiveInvoiceTypeId;
    }

    public String getReceiveInvoiceTypeName() {
        return receiveInvoiceTypeName;
    }

    public void setReceiveInvoiceTypeName(String receiveInvoiceTypeName) {
        this.receiveInvoiceTypeName = receiveInvoiceTypeName;
    }

    public String getReceiveInvoiceTypeCode() {
        return receiveInvoiceTypeCode;
    }

    public void setReceiveInvoiceTypeCode(String receiveInvoiceTypeCode) {
        this.receiveInvoiceTypeCode = receiveInvoiceTypeCode;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public List<ReceiveInvoiceDetailModel> getReceiveInvoiceDetail() {
        return receiveInvoiceDetail;
    }

    public void setReceiveInvoiceDetail(List<ReceiveInvoiceDetailModel> receiveInvoiceDetail) {
        this.receiveInvoiceDetail = receiveInvoiceDetail;
    }

    @Override
    public String toString() {
        return "ReceiveInvoiceModel{" +
                "id=" + id +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", orderId=" + orderId +
                ", environmentId=" + environmentId +
                ", supplierId=" + supplierId +
                ", activityId=" + activityId +
                ", receiveInvoiceDate='" + receiveInvoiceDate + '\'' +
                ", receiveInvoiceTypeId=" + receiveInvoiceTypeId +
                ", receiveInvoiceTypeName='" + receiveInvoiceTypeName + '\'' +
                ", receiveInvoiceTypeCode='" + receiveInvoiceTypeCode + '\'' +
                ", progress=" + progress +
                ", itemCount=" + itemCount +
                ", receiveInvoiceDetail=" + receiveInvoiceDetail +
                '}';
    }
}
