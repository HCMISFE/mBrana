package com.jsi.mbrana.Models;

import java.util.ArrayList;

/**
 * Created by pc-6 on 6/15/2016.
 */
public class ApiPoModel {
    private POModel purchaseOrder;
    private ArrayList<ItemModel> purchaseOrderDetail;

    public void setPurchaseOrder(POModel purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public void setPurchaseOrderDetail(ArrayList<ItemModel> purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public POModel getPurchaseOrder() {
        return purchaseOrder;
    }

    public ArrayList<ItemModel> getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }
}
