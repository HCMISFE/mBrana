package com.jsi.mbrana.Workflow.Receipt.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jsi.mbrana.Workflow.Adapter.Receipt.Adapter_ReceiptDetail_Master;
import com.jsi.mbrana.Workflow.Adapter.Receipt.Adapter_ReceiptDetail_Master_Detail;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Models.ReceiveInvoiceDetailModel;
import com.jsi.mbrana.Models.ReceiveInvoiceMastersDetailModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class View_Receipt extends Fragment implements IDataNotification {
    ProgressDialog progress;
    ListView tablecontainerMaster;
    RecyclerView tablecontainerMastersDetail;
    EditText edit_text;
    Button button;
    ReceiveInvoiceDetailModel item;
    ViewPager viewPager;
    ArrayList<ReceiveInvoiceDetailModel> ReceiptInvoicesdetailheader = new ArrayList<ReceiveInvoiceDetailModel>();
    ArrayList<ReceiveInvoiceMastersDetailModel> ReceiptInvoicesMasterDetail = new ArrayList<ReceiveInvoiceMastersDetailModel>();

    public View_Receipt() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_receipt, container, false);

        tablecontainerMaster = (ListView) view.findViewById(R.id.ReceiptInvloceListMaster_TableContainer);
        button = (Button) view.findViewById(R.id.StartDetailFragmentID);
        edit_text = (EditText) view.findViewById(R.id.EditModel19);
        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (edit_text.getText().toString() != null) {
                    Log.d("EditText value=", edit_text.getText().toString());
                    Log.d("Item VieR", item.getSTVOrInvoiceNo().toString());
                    item.setModel19(edit_text.getText().toString());
                    GlobalVariables.setRImodel19(edit_text.getText().toString());
                    GlobalVariables.setRIMaster(item);

                    FragmentTransaction trans = getFragmentManager()
                            .beginTransaction();
                    LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.RIMasterLinearLay);
                    mainLayout.setVisibility(LinearLayout.GONE);
                    trans.replace(R.id.RIIMaster, new RIDetailInput());
                    trans.commit();
                }
            }
        });
        LoadMasterData(getActivity().getIntent().getIntExtra("ReceiptID", 0));
        return view;
    }

    public void LoadMasterData(int ReceiptID) {
        if (Helper.isNetworkAvailable((Activity) getContext())) {
            new DataServiceTask(View_Receipt.this, 1).execute("Receipt/GetReceipt?ReceiptID=" + ReceiptID + "&EnvironmentCode=" + GlobalVariables.getSelectedEnvironmentCode(), "");
        } else {
            handelNotification(getResources().getString(R.string.connectionErrorMessage));
        }
    }

    @Override
    public void dataObjectReceived(JSONObject jsonObject, int requestCode) {

    }

    @Override
    public void dataReceived(JSONArray jsonArray, int requestCode) {

        try {
            if (requestCode == 1) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(0);
                    Log.d("jo ", jo.toString());
                    item = new ReceiveInvoiceDetailModel();
                    Log.d("jo.getString", jo.getString("STVOrInvoiceNo"));
                    item.setSTVOrInvoiceNo(jo.getString("STVOrInvoiceNo"));
                    item.setID(jo.getInt("ID"));
                    item.setSupplier(jo.getString("Supplier"));
                    item.setNoOfItems(jo.getInt("NoOfItems"));
                    item.setReceiptStatus(jo.getString("ReceiptStatus"));
                    item.setFullName(jo.getString("FullName"));
                    item.setDocumentType(jo.getString("DocumentType"));
                    item.setGRNFNumber(jo.getDouble("GRNFNumber"));
                    item.setModel19(jo.getString("Model19"));
                    item.setReceiptDate(jo.getString("ReceiptDate"));
                    item.setReceiptStatusCode(jo.getString("ReceiptStatusCode"));

                    ReceiptInvoicesdetailheader.add(item);
                }
                Adapter_ReceiptDetail_Master adapter = new Adapter_ReceiptDetail_Master(getContext(), R.layout.layout_viewreceiptinvoices_detail, ReceiptInvoicesdetailheader);
                tablecontainerMaster.setAdapter(adapter);
            } else if (requestCode == 2) {
                Log.d("requestCode2", String.valueOf(requestCode));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    ReceiveInvoiceMastersDetailModel item = new ReceiveInvoiceMastersDetailModel();
                    Log.d("FullName", jo.getString("FullItemName"));
                    item.setID(jo.getInt("ID"));
                    item.setFullItemName(jo.getString("FullItemName"));
                    item.setProductCN(jo.getString("ProductCN"));
                    item.setUnit(jo.getString("Unit"));
                    item.setManufacturer(jo.getString("Manufacturer"));
                    item.setManufacturerId(jo.getInt("ManufacturerId"));
                    item.setBatchNo(jo.getString("BatchNo"));
                    item.setModel19(jo.getString("Model19"));
                    item.setExpDate(jo.getString("ExpDate"));
                    item.setInvoicedQuantity(jo.getDouble("InvoicedQuantity"));
                    item.setInvoicedQuantity(jo.getDouble("Quantity"));

                    ReceiptInvoicesMasterDetail.add(item);
                }
                Adapter_ReceiptDetail_Master_Detail adapter = new Adapter_ReceiptDetail_Master_Detail(getContext(), ReceiptInvoicesMasterDetail, tablecontainerMastersDetail);
                tablecontainerMastersDetail.setAdapter(adapter);
            }

        } catch (Exception e) {

            this.handelNotification("Error found");
            e.printStackTrace();
        }
    }

    @Override
    public void handelNotification(String message) {

    }

    @Override
    public void handelProgressDialog(Boolean showProgress, String Message) {
        if (showProgress) {
            progress = new ProgressDialog(getContext());
            progress.setMessage("Loading");
            progress.show();
        } else progress.dismiss();
    }

    @Override
    public void readFromDatabase(int requestCode) {

    }
}
