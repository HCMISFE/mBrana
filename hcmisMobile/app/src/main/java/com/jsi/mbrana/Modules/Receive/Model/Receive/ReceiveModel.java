package com.jsi.mbrana.Modules.Receive.Model.Receive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Surafel Nigussie on 12/19/2017.
 */

public class ReceiveModel {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("InvoiceDate")
    @Expose
    private String invoiceDate;
    @SerializedName("ReceiveInvoiceId")
    @Expose
    private int receiveInvoiceId;
    @SerializedName("EnvironmentName")
    @Expose
    private String environmentName;
    @SerializedName("EnvironmentId")
    @Expose
    private int environmentId;
    @SerializedName("SupplierName")
    @Expose
    private String supplierName;
    @SerializedName("ActivityName")
    @Expose
    private String activityName;
    @SerializedName("ReceiveStatusCode")
    @Expose
    private String receiveStatusCode;
    @SerializedName("ActivityId")
    @Expose
    private Integer activityId;
    @SerializedName("ReceiveDetail")
    @Expose
    private List<ReceiveDetailModel> receiveDetail = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getReceiveInvoiceId() {
        return receiveInvoiceId;
    }

    public void setReceiveInvoiceId(int receiveInvoiceId) {
        this.receiveInvoiceId = receiveInvoiceId;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public int getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public List<ReceiveDetailModel> getReceiveDetail() {
        return receiveDetail;
    }

    public void setReceiveDetail(List<ReceiveDetailModel> receiveDetail) {
        this.receiveDetail = receiveDetail;
    }

    public String getReceiveStatusCode() {
        return receiveStatusCode;
    }

    public void setReceiveStatusCode(String receiveStatusCode) {
        this.receiveStatusCode = receiveStatusCode;
    }

    @Override
    public String toString() {
        return "ReceiveModel{" +
                "id=" + id +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", receiveInvoiceId=" + receiveInvoiceId +
                ", environmentName='" + environmentName + '\'' +
                ", environmentId=" + environmentId +
                ", supplierName='" + supplierName + '\'' +
                ", activityName='" + activityName + '\'' +
                ", receiveStatusCode='" + receiveStatusCode + '\'' +
                ", activityId=" + activityId +
                ", receiveDetail=" + receiveDetail +
                '}';
    }
}
