package com.jsi.mbrana.Workflow.Receipt.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jsi.mbrana.Models.ReceiptInvoiceModel;
import com.jsi.mbrana.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddReceipt extends Fragment {
    static TextView EditSTVOrInvoiceNo;
    static TextView EditSupplier;
    static TextView EditInvoiceType;
    static TextView EditInvoiceAmount;
    static TextView EditReceivedAmount;
    static TextView EditwPrintedDate;
    static TextView EditModel19;
    private Button button;
    private View myview;

    public AddReceipt() {
        // Required empty public constructor
    }

    public static void getSelectedRIData(final ReceiptInvoiceModel selectedRI) {
        Log.d("strings", selectedRI.toString());
        EditSTVOrInvoiceNo.setText(selectedRI.getSTVOrInvoiceNo());
        EditSupplier.setText(selectedRI.getSupplier());
        EditInvoiceType.setText(selectedRI.getInvoiceType());
        EditInvoiceAmount.setText((selectedRI.getInvoicedAmount()) + "");
        EditReceivedAmount.setText((selectedRI.getReceivedAmount()) + "");
        EditwPrintedDate.setText(selectedRI.getPrintedDate());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_add_receipt, container, false);
        EditSTVOrInvoiceNo = (TextView) myview.findViewById(R.id.EditSTVOrInvoiceNo);
        EditSupplier = (TextView) myview.findViewById(R.id.EditSupplier);
        EditInvoiceType = (TextView) myview.findViewById(R.id.EditInvoiceType);
        EditInvoiceAmount = (TextView) myview.findViewById(R.id.EditInvoicedAmount);
        EditReceivedAmount = (TextView) myview.findViewById(R.id.EditReceivedAmount);
        EditwPrintedDate = (TextView) myview.findViewById(R.id.edittxt_Printed);
        EditModel19 = (TextView) myview.findViewById(R.id.EditModel19);

        return myview;
    }
}
