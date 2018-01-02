package com.jsi.mbrana.Workflow.Adapter.Issue;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsi.mbrana.Helpers.Constants;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Models.InstitutionModel;
import com.jsi.mbrana.R;
import com.jsi.mbrana.Workflow.Issue.FacilitySelectionActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sololia on 8/30/2016.
 */
public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.MyViewHolder> {
    ArrayList<InstitutionModel> objects;
    Context context;
    int tempposition = 0;
    int tempsndpostion = 100;
    int temp3rd = 0;
    Constants cons = new Constants();

    public FacilityAdapter(Context context, int resource, ArrayList<InstitutionModel> objects) {
        this.objects = objects;
        this.context = context;
    }

    public FacilityAdapter(ArrayList<InstitutionModel> data) {
        this.objects = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.facility_cards_layout, parent, false);

        view.setOnClickListener(FacilitySelectionActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        TextView facility_FirstLetter = holder.facility_FirstLetter;

        textViewName.setText(objects.get(listPosition).getInstitutionName());
        Date IssuedDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");

        try {
            IssuedDate = simpleDateFormat.parse(objects.get(listPosition).getIssueDate().replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("Date", objects.get(listPosition).getIssueDate());
        if (objects.get(listPosition).getIssueDate().equals("0001-01-01T00:00:00")) {
            textViewVersion.setText("-");
        } else {
            textViewVersion.setText(Helper.getMomentFromNow(IssuedDate));
        }

        facility_FirstLetter.setText(String.valueOf(objects.get(listPosition).getInstitutionName().toString().charAt(0)));
        if (tempposition < 255) {
            tempposition = tempposition + 25;
            tempsndpostion = tempsndpostion + listPosition;
            temp3rd = 15 + temp3rd;
        } else {
            tempposition = 26;
        }
        if (tempsndpostion < 255) {

            tempsndpostion = tempsndpostion + listPosition;

        } else {
            tempsndpostion = listPosition;
        }
        if (temp3rd < 255) {

            temp3rd = 15 + temp3rd;
        } else {
            temp3rd = 17;
        }
        facility_FirstLetter.setBackgroundColor(Color.rgb(tempposition, tempsndpostion, temp3rd));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewVersion;
        TextView facility_FirstLetter;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.facility_FirstLetter = (TextView) itemView.findViewById(R.id.facility_FirstLetter);
        }
    }
}