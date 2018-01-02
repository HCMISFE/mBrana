package com.jsi.mbrana.Workflow.Issue.Depreciated.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.R;

import java.util.ArrayList;

public class NewOrder_Fragment extends Fragment {
    private Spinner facilitiesListSpinner;
    private ListView itemsTable;
    private Button btnSave;
    private Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_order, container, false);

        String[] facilities = {"Facility1", "Facility2", "Facility3", "Facility4", "Facility5", "Facility6", "Facility7"};
        final ArrayList<ItemModel> items = new ArrayList<ItemModel>();

        for (int i = 0; i < 7; i++) {
            ItemModel item = new ItemModel();
            switch (i) {
                case 0:
                    item.setItemCode("BCG");
                    break;
                case 1:
                    item.setItemCode("BOPV");
                    break;
                case 2:
                    item.setItemCode("IPV");
                    break;
                case 3:
                    item.setItemCode("Measle");
                    break;
                case 4:
                    item.setItemCode("MenA");
                    break;
                case 5:
                    item.setItemCode("PCV");
                    break;
                case 6:
                    item.setItemCode("Penta");
                    break;
                case 7:
                    item.setItemCode("Rota");
                    break;
                case 8:
                    item.setItemCode("Topv");
                    break;
                case 9:
                    item.setItemCode("TT");
                    break;
            }

            item.setQuantity(0);
            items.add(item);
        }

        //set values to spinner for facility drop down
        facilitiesListSpinner = (Spinner) rootView.findViewById(R.id.newOrder_FaclitiesSpinner);
        ArrayAdapter<String> facilitiesListAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.layout_spinneritem, facilities);
        facilitiesListAdapter.setDropDownViewResource(R.layout.layout_spinneritem);
        facilitiesListSpinner.setAdapter(facilitiesListAdapter);

        //set Adapter to itemsListTable
        itemsTable = (ListView) rootView.findViewById(R.id.newOrder_ItemsTable);

        GlobalVariables.setDraftOrderItems(items);

        btnSave = (Button) rootView.findViewById(R.id.neworder_buttonSave);
        btnSubmit = (Button) rootView.findViewById(R.id.neworder_buttonSubmit);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText quanityColumn;
                ArrayList<ItemModel> itemsToBeSaved = GlobalVariables.getDraftOrderItems();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> itemsToBeSaved = GlobalVariables.getDraftOrderItems();
            }
        });

        // Inflate the layout for this fragment
        return rootView;

    }
}
