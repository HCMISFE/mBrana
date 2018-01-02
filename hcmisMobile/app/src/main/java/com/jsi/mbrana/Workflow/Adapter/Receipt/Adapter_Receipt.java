package com.jsi.mbrana.Workflow.Adapter.Receipt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Modules.Receive.Model.ReceiptInvoice.ReceiveInvoiceModel;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sololia on 6/14/2016.
 */
public class Adapter_Receipt extends ArrayAdapter {
    List<ReceiveInvoiceModel> objects;
    Context context;
    private List<ReceiveInvoiceModel> arraylist;

    public Adapter_Receipt(Context context, int resource, List<ReceiveInvoiceModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.arraylist = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_viewreceiptinvoices, null);
        }

        final ReceiveInvoiceModel receiptinvoice = objects.get(position);

        if (receiptinvoice != null) {

            TextView STVOrInvoiceNo = (TextView) view.findViewById(R.id.ViewRec_TableRowSTVOrInvoiceNo);
            //   TextView Supplier = (TextView) view.findViewById(R.id.ViewRec_TableRowSupplier);
            TextView PrintedDate = (TextView) view.findViewById(R.id.ViewRec_TableRowPrintedDate);
            TextView NoOfItems = (TextView) view.findViewById(R.id.ViewRec_TableGridRowsNoOfItems);
            ProgressBar Progress = (ProgressBar) view.findViewById(R.id.progressissuerec);
            TextView ProgressText = (TextView) view.findViewById(R.id.ProgressText);
            TextView tvFirstLetter = (TextView) view.findViewById(R.id.documentType_FirstLetter);

            Progress.setScaleY(3f);
            String string = receiptinvoice.getReceiveInvoiceDate();
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            Date date = null;
            try {
                date = df.parse(string);
                date = df.parse(df.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            Date printedDate = null;
            try {
                printedDate = simpleDateFormat.parse(receiptinvoice.getReceiveInvoiceDate().replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                String[] datevaluesArray = df.format(date).split("\\-");

                DateFormatSymbols dfs = new DateFormatSymbols();
                String month = dfs.getShortMonths()[Integer.parseInt(datevaluesArray[1]) - 1];

                STVOrInvoiceNo.setText("STV#");
                // Supplier.setText((receiptinvoice.getSupplier()));
                PrintedDate.setText(Helper.getMomentFromNow(printedDate));
                Progress.setProgress((int) receiptinvoice.getProgress());
                NoOfItems.setText("Items " + String.valueOf(receiptinvoice.getItemCount()));

                ProgressText.setText((receiptinvoice.getProgress()) + "%");

                tvFirstLetter.setBackgroundResource(R.drawable.circle_orange);

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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(arraylist);
        } else {
            for (ReceiveInvoiceModel ri : arraylist) {
//                String str = Helper.DocumentTypeCleanup(ri.getDocumentTypesCode()) + " " + ri.getSTVOrInvoiceNoModifed();
//                if (str.toLowerCase(Locale.getDefault()).contains(charText)) {
//                    objects.add(ri);
//                }
            }
        }
        notifyDataSetChanged();
    }
}

