package com.jsi.mbrana.Workflow.Receipt.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsi.mbrana.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptInvoiceDetailOne extends Fragment {

    public ReceiptInvoiceDetailOne() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt_invoice_detail_one, container, false);
    }
}
