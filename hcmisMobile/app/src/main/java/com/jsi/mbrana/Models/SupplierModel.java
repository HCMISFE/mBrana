package com.jsi.mbrana.Models;

/**
 * Created by pc-6 on 7/21/2016.
 */
public class SupplierModel {
    private String Supplier;
    private int SupplierID;

    public void setSupplier(String supplier) {
        Supplier = supplier;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public String getSupplier() {
        return Supplier;
    }

    public int getSupplierID() {
        return SupplierID;
    }
}
