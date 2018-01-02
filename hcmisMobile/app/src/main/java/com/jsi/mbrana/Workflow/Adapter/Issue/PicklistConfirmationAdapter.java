package com.jsi.mbrana.Workflow.Adapter.Issue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsi.mbrana.Models.PicklistModel;
import com.jsi.mbrana.CommonModels.VVMModel;
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
public class PicklistConfirmationAdapter extends ArrayAdapter {
    ArrayList<PicklistModel> objects;
    ArrayList<VVMModel> VVMList;
    ArrayList<String> VVMCode;
    ArrayAdapter<String> dataAdapter;
    Context context;

    public PicklistConfirmationAdapter(Context context, int resource, ArrayList<PicklistModel> objects, ArrayList<VVMModel> VVMList) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.VVMList = VVMList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_picklistconfirmation, null);
        }

        PicklistModel item = objects.get(position);

        if (item != null) {
            TextView tvItemColumn = (TextView) view.findViewById(R.id.pk_ItemColumn);
            TextView tvBatch = (TextView) view.findViewById(R.id.pk_batchColumn);
            TextView tvExpiry = (TextView) view.findViewById(R.id.pk_expiryColumn);
            TextView tvApprovedQuantityColumn = (TextView) view.findViewById(R.id.pk_approvedQtyColumn);
            TextView tvUnitColumn = (TextView) view.findViewById(R.id.pk_unitColum);
            TextView rowno = (TextView) view.findViewById(R.id.pk_rowno);
            Spinner vvmSpinner = (Spinner) view.findViewById(R.id.vvm_spinner);
            TextView tvManufacturer = (TextView) view.findViewById(R.id.pk_ManufacturerColumn);
            VVMCode = new ArrayList<>();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            Date expiry = null;

            try {
                expiry = simpleDateFormat.parse(item.getExpireDate().replace("T", " "));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            rowno.setText((position + 1) + "");
            tvItemColumn.setText(item.getProductCN());
            tvBatch.setText("Batch " + item.getBatchNumber() + "");

            if (VVMList != null)
                for (int i = 0; i < VVMList.size(); i++) {
                    VVMCode.add(VVMList.get(i).getCode());
                }
            dataAdapter = new ArrayAdapter<>(context, R.layout.layout_vvm_list, VVMCode);
            dataAdapter.setDropDownViewResource(R.layout.layout_vvm_list);
            vvmSpinner.setAdapter(dataAdapter);
            vvmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                    // Assigning selection from the spinner to the model
                    objects.get(position).setVVMCode(VVMList.get(index).getCode());
                    objects.get(position).setVVMID(VVMList.get(index).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            tvExpiry.setText(new SimpleDateFormat("MM-yyyy").format(expiry));
            tvApprovedQuantityColumn.setText(item.getQuantityInBU() + "");
            tvApprovedQuantityColumn.setText(NumberFormat.getNumberInstance(Locale.US).format(item.getQuantityInBU()) + "");
            tvUnitColumn.setText(item.getUnit().replace("Vial  of", ""));
            tvManufacturer.setText(item.getManufacturerSH());
        }

        return view;
    }
}