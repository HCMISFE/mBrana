package com.jsi.mbrana.Workflow.Adapter.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.Models.InstitutionModel;
import com.jsi.mbrana.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by pc-6 on 7/11/2016.
 */
public class ListOfFacilityWithIssueAdapter extends ArrayAdapter {
    ArrayList<InstitutionModel> objects;

    public ListOfFacilityWithIssueAdapter(Context context, int resource, ArrayList<InstitutionModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_facilitieswithissue, null);
        }


        InstitutionModel institution = objects.get(position);

        if (institution != null) {

            TextView tvName = (TextView) view.findViewById(R.id.frontpage_facilityName);
            TextView tvType=(TextView)view.findViewById(R.id.frontpage_facilityType);
            TextView tvRowNo=(TextView)view.findViewById(R.id.frontpage_rowNo);



            tvName.setText(institution.getInstitutionName());
            tvType.setText(NumberFormat.getNumberInstance(Locale.US).format(institution.getQuantityIssued()) +" ");
            tvRowNo.setText((position+1)+"");

            //handel row strip
            int index = objects.indexOf(institution);
            if ((index % 2) != 0) {
                view.setBackgroundResource(R.color.whitesmoke);
            } else {
                view.setBackgroundResource(R.color.white);
            }

        }

        return view;
    }
}