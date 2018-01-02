package com.jsi.mbrana.Models;

/**
 * Created by pc-6 on 6/16/2016.
 */
public class StatusModel {
    private String orderStatus;
    private String orderStatusCode;

    public StatusModel(String orderStatus, String orderStatusCode){
        this.orderStatus=orderStatus;
        this.orderStatusCode=orderStatusCode;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
