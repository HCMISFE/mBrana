package com.jsi.mbrana.Models;

import java.util.ArrayList;

/**
 * Created by Sololia on 10/12/2016.
 */
public class ApiReceiptModel {
    private ReceiptInvoiceModel Receipt;
    private ArrayList<AddNewReceiptInvoiceDetailModel> ReceiveDocs;

    public ReceiptInvoiceModel getReceipt() {
        return Receipt;
    }

    public void setReceipt(ReceiptInvoiceModel order) {
        Receipt = order;
    }

    public ArrayList<AddNewReceiptInvoiceDetailModel> getReceiveDocs() {
        return ReceiveDocs;
    }

    public void setReceiveDocs(ArrayList<AddNewReceiptInvoiceDetailModel> orderDetail) {
        ReceiveDocs = orderDetail;
    }
}
