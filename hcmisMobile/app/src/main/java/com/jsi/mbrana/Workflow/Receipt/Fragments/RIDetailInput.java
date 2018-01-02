package com.jsi.mbrana.Workflow.Receipt.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jsi.mbrana.Workflow.Adapter.Receipt.Adapter_ReceiptDetail_Master_Detail;
import com.jsi.mbrana.DataAccess.DataServiceTask;
import com.jsi.mbrana.DataAccess.IDataNotification;
import com.jsi.mbrana.Helpers.GlobalVariables;
import com.jsi.mbrana.Helpers.Helper;
import com.jsi.mbrana.Models.ReceiveInvoiceMastersDetailModel;
import com.jsi.mbrana.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RIDetailInput extends Fragment implements IDataNotification {
    ProgressDialog progress;
    ArrayList<ReceiveInvoiceMastersDetailModel> ReceiptInvoicesMasterDetail = new ArrayList<ReceiveInvoiceMastersDetailModel>();
    RecyclerView tablecontainerMastersDetail;
    ReceiveInvoiceMastersDetailModel detailitem;
    String model19;
    Button submitButton;
    Adapter_ReceiptDetail_Master_Detail adapter;
    JSONObject ReceiveDocs = new JSONObject();
    JSONObject Receipt = new JSONObject();

    public RIDetailInput() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_ridetail_input, container, false);
        tablecontainerMastersDetail = (RecyclerView) view.findViewById(R.id.ReceiptInvloceListMasterDetail_TableContainer);
        LoadMasterData(getActivity().getIntent().getIntExtra("ReceiptID", 0));
        Log.v("getSTVOrInvoiceNo", GlobalVariables.getRIMaster().getSTVOrInvoiceNo());
        Log.v("model 19", GlobalVariables.getRIMaster().getModel19());
        JSONObject headerData = new JSONObject();
        JSONArray headerDetailData = new JSONArray();

        try {
            headerData.put("STVOrInvoiceNo", GlobalVariables.getRIMaster().getSTVOrInvoiceNo());
            headerData.put("ID", GlobalVariables.getRIMaster().getID());
            headerData.put("Supplier", GlobalVariables.getRIMaster().getSupplier());
            headerData.put("NoOfItems", GlobalVariables.getRIMaster().getNoOfItems());

            headerData.put("ReceiptStatus", GlobalVariables.getRIMaster().getReceiptStatus());
            headerData.put("FullName", GlobalVariables.getRIMaster().getFullName());
            headerData.put("DocumentType", GlobalVariables.getRIMaster().getDocumentType());
            headerData.put("GRNFNumber", GlobalVariables.getRIMaster().getGRNFNumber());

            headerData.put("Model19", GlobalVariables.getRIMaster().getModel19());
            headerData.put("ReceiptDate", GlobalVariables.getRIMaster().getReceiptDate());
            headerData.put("ReceiptStatusCode", GlobalVariables.getRIMaster().getReceiptStatusCode());
            Receipt.put("Receipt", headerData);
            Log.d("JSON Body", Receipt.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        submitButton = (Button) view.findViewById(R.id.UpdateRIDetailButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject detailsData = new JSONObject();
                JSONArray headerDetailData = new JSONArray();
                try {
                    // for(int i =0;i< tablecontainerMastersDetail.getAdapter().getCount()-1 ;i++){
                    for (int i = 0; i < 10; i++) {
                        Log.d("Adapter child", ReceiptInvoicesMasterDetail.get(i).getFullItemName());
                        Log.d("adpater count", String.valueOf(i));
                        EditText InvoicedQuantityEditText = (EditText) tablecontainerMastersDetail.getChildAt(i).findViewById(R.id.ViewRecMaterDetail_TableRowInvoicedQuantity);
                        //  TextView ProductCNyEditText = (TextView) tablecontainerMastersDetail.getChildAt(i).findViewById(R.id.ViewRecMaterDetail_TableRowProductCN);
                        EditText ExpiryText = (EditText) tablecontainerMastersDetail.getChildAt(i).findViewById(R.id.ViewRecMaterDetail_TableRowExpiry);
                        EditText BatchnoEditText = (EditText) tablecontainerMastersDetail.getChildAt(i).findViewById(R.id.ViewRecMaterDetail_TableRowBatchNo);
                        EditText RecievedQuanitityEditText = (EditText) tablecontainerMastersDetail.getChildAt(i).findViewById(R.id.ViewRecMaterDetail_TableRowRecievedQuanitity);

                        try {
                            detailsData.put("FullItemName", ReceiptInvoicesMasterDetail.get(i).getFullItemName());
                            detailsData.put("ID", ReceiptInvoicesMasterDetail.get(i).getID());
                            detailsData.put("ProductCN", ReceiptInvoicesMasterDetail.get(i).getProductCN());
                            detailsData.put("Unit", ReceiptInvoicesMasterDetail.get(i).getUnit());
                            detailsData.put("Manufacturer", ReceiptInvoicesMasterDetail.get(i).getManufacturer());
                            detailsData.put("ManufacturerId", ReceiptInvoicesMasterDetail.get(i).getManufacturerId());
                            detailsData.put("BatchNo", BatchnoEditText.getText().toString());
                            detailsData.put("ExpDate", ExpiryText.getText().toString());
                            detailsData.put("InvoicedQuantity", InvoicedQuantityEditText.getText().toString());
                            detailsData.put("Quantity", RecievedQuanitityEditText.getText().toString());
                            headerDetailData.put(detailsData);
                            Receipt.put("ReceiveDocs", headerDetailData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("JSON Body2", Receipt.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        model19 = GlobalVariables.getRImodel19();
        return view;
    }

    public void LoadMasterData(int ReceiptID) {
        if (Helper.isNetworkAvailable((Activity) getContext())) {
            // new DataServiceTask(View_Receipt.this).execute("Receipt/GetReceiptDetail?ReceiptID="+ReceiptID, "");
            new DataServiceTask(RIDetailInput.this, 2).execute("Receipt/GetReceiptDetail", "");
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
            Log.d("requestCode2", String.valueOf(requestCode));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                detailitem = new ReceiveInvoiceMastersDetailModel();
                Log.d("FullName", jo.getString("FullItemName"));
                detailitem.setID(jo.getInt("ID"));
                detailitem.setFullItemName(jo.getString("FullItemName"));
                detailitem.setProductCN(jo.getString("ProductCN"));
                detailitem.setUnit(jo.getString("Unit"));
                detailitem.setManufacturer(jo.getString("Manufacturer"));
                detailitem.setManufacturerId(jo.getInt("ManufacturerId"));
                detailitem.setBatchNo(jo.getString("BatchNo"));
                detailitem.setModel19(jo.getString("Model19"));
                detailitem.setExpDate(jo.getString("ExpDate"));
                detailitem.setInvoicedQuantity(jo.getDouble("InvoicedQuantity"));
                detailitem.setQuantity(jo.getDouble("Quantity"));
                ReceiptInvoicesMasterDetail.add(detailitem);
            }
            adapter = new Adapter_ReceiptDetail_Master_Detail(getContext(), ReceiptInvoicesMasterDetail, tablecontainerMastersDetail);
            tablecontainerMastersDetail.setAdapter(adapter);
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
