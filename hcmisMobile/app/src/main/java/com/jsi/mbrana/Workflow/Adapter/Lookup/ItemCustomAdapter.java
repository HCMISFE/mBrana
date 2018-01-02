package com.jsi.mbrana.Workflow.Adapter.Lookup;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsi.mbrana.R;

import java.util.ArrayList;

/**
 * Created by surafel on 7/7/2016.
 */
public class ItemCustomAdapter extends ArrayAdapter {
    ArrayList<String> objects;
    Context context;

    public ItemCustomAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_spinneritem_white, null);
        }
        String ItemModel = objects.get(position);
        if (ItemModel != null) {
            TextView textView = (TextView) view.findViewById(R.id.spinnerTv);
            if (Html.fromHtml(ItemModel.toString() + "").length() > 35)
                textView.setText(Html.fromHtml(ItemModel.toString().substring(0, 35)));
            else textView.setText(Html.fromHtml(ItemModel.toString() + ""));
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_spinneritemforenvironmentlist, null);
        }
        String ItemModel = objects.get(position);
        if (ItemModel != null) {
            TextView textView = (TextView) view.findViewById(R.id.spinnerTv);
            textView.setText(Html.fromHtml(ItemModel.toString() + ""));
        }
        return view;
    }
}
