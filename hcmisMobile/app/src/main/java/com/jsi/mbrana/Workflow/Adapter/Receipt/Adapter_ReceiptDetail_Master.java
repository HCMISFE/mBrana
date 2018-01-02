package com.jsi.mbrana.Workflow.Adapter.Receipt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.R;
import com.jsi.mbrana.Models.ReceiveInvoiceDetailModel;

import java.util.ArrayList;

/**
 * Created by Sololia on 6/15/2016.
 */
public class Adapter_ReceiptDetail_Master extends ArrayAdapter {

    ArrayList<ReceiveInvoiceDetailModel> objects;
    Context context;

    public Adapter_ReceiptDetail_Master(Context context, int resource, ArrayList<ReceiveInvoiceDetailModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_receipt_invoice_detail_master, null);
        }

        final ReceiveInvoiceDetailModel receiptinvoice = objects.get(position);

        if (receiptinvoice != null) {
            TextView STVOrInvoiceNo = (TextView) view.findViewById(R.id.ViewRecDetailMaster_TableCelSTVOrInvoiceNo);
            TextView DocumentType = (TextView) view.findViewById(R.id.ViewRecDetailMaster_TableCelDocumentType);
            TextView ReceiptStatus = (TextView) view.findViewById(R.id.ViewRecDetailMaster_TableCelDocumentReceiptStatus);
            TextView Supplier = (TextView) view.findViewById(R.id.ViewRecDetailMaster_TableCelSuppliere);

            try {
                STVOrInvoiceNo.setText(receiptinvoice.getSTVOrInvoiceNo());
                DocumentType.setText((receiptinvoice.getDocumentType()));
                ReceiptStatus.setText((receiptinvoice.getReceiptStatus()));
                Supplier.setText(receiptinvoice.getSupplier());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //handel row strip
            int index = objects.indexOf(receiptinvoice);
            if ((index % 2) != 0) {
                view.setBackgroundResource(R.color.whitesmoke);
            } else {
                view.setBackgroundResource(R.color.white);
            }

            view.setPadding(0, 6, 0, 6);
        }

        return view;
    }
}


