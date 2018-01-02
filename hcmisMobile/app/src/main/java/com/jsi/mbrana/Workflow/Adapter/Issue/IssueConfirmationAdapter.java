package com.jsi.mbrana.Workflow.Adapter.Issue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.Models.ApiIssueModel;
import com.jsi.mbrana.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class IssueConfirmationAdapter extends ArrayAdapter {
    ArrayList<ApiIssueModel> objects;

    public IssueConfirmationAdapter(Context context, int resource, ArrayList<ApiIssueModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_issueconfirmation, null);
        }

        ApiIssueModel issue = objects.get(position);

        if (issue != null) {
            TextView tvItemColumn = (TextView) view.findViewById(R.id.pk_ItemColumn);
            TextView tvBatch = (TextView) view.findViewById(R.id.pk_batchColumn);
            TextView tvExpiry = (TextView) view.findViewById(R.id.pk_expiryColumn);
            TextView tvApprovedQuantityColumn = (TextView) view.findViewById(R.id.pk_approvedQtyColumn);
            TextView tvUnitColumn = (TextView) view.findViewById(R.id.pk_unitColum);
            TextView rowno = (TextView) view.findViewById(R.id.pk_rowno);
            TextView vvm = (TextView) view.findViewById(R.id.pk_VVMColumn);
            TextView tvManufacturer = (TextView) view.findViewById(R.id.pk_manufacturer);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            Date expiry = null;

            try {
                expiry = simpleDateFormat.parse(issue.getExpireDate().replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tvItemColumn.setText(issue.getProductCN());
            vvm.setText(issue.getVVMCode());

            if (issue.getBatchNo() != null && !issue.getBatchNo().equalsIgnoreCase("null"))
                tvBatch.setText("Batch " + issue.getBatchNo());
            else tvBatch.setText("-");

            tvExpiry.setText(new SimpleDateFormat("MM-yyyy").format(expiry));
            tvApprovedQuantityColumn.setText(NumberFormat.getNumberInstance(Locale.US).format(issue.getQuantity()) + "");
            tvUnitColumn.setText(issue.getUnit());
            tvManufacturer.setText(issue.getManufacturer());
        }

        return view;
    }
}