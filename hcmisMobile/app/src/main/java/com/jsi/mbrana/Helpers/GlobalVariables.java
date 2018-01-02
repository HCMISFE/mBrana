package com.jsi.mbrana.Helpers;

import android.app.Application;

import com.jsi.mbrana.Models.AddNewReceiptInvoiceDetailModel;
import com.jsi.mbrana.Models.ApiIssueModel;
import com.jsi.mbrana.Models.ItemModel;
import com.jsi.mbrana.Models.PicklistModel;
import com.jsi.mbrana.Models.ReceiveInvoiceDetailModel;
import com.jsi.mbrana.Models.ReceiveInvoiceMastersDetailModel;

import java.util.ArrayList;

/**
 * Created by pc-6 on 6/14/2016.
 */
public class GlobalVariables extends Application {
    private static String Token = null;
    private static ArrayList<ItemModel> DraftOrderItems;
    private static ArrayList<ItemModel> DraftPOItems;
    private static ArrayList<ItemModel> items;
    private static ArrayList<ItemModel> ordersToBeApporved;
    private static ArrayList<ItemModel> ordersToBeIssued;
    private static ArrayList<ApiIssueModel> SavedIssues;
    private static ArrayList<AddNewReceiptInvoiceDetailModel> ReceiptInvoiceDetail;
    private static ArrayList<PicklistModel> picklistsToBeSaved;
    private static String pendingOrderType;
    private static String RImodel19;
    private static String VVMCode;
    private static String snackBarMessage;
    private static int VVMID;
    private static int Position;
    private static ReceiveInvoiceDetailModel RIMaster;
    private static ReceiveInvoiceMastersDetailModel RIDetail;
    private static String selectedEnvironmentCode;
    private static int selectedEnvironmentID;
    private static String selectedEnvironment;
    private static String activityCode;
    private static int receiptID;
    private static boolean IsWidgetUpdateRunning = false;
    private static boolean SignalValueReceived = false;

    public static boolean isSignalValueReceived() {
        return SignalValueReceived;
    }

    public static void setSignalValueReceived(boolean signalValueReceived) {
        SignalValueReceived = signalValueReceived;
    }

    public static void setIsWidgetUpdateRunning(boolean isWidgetUpdateRunning) {
        IsWidgetUpdateRunning = isWidgetUpdateRunning;
    }

    public static boolean isWidgetUpdateRunning() {
        return IsWidgetUpdateRunning;
    }

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static String getSelectedEnvironment() {
        return selectedEnvironment;
    }

    public static void setSelectedEnvironment(String selectedEnvironment) {
        GlobalVariables.selectedEnvironment = selectedEnvironment;
    }

    public static int getSelectedEnvironmentID() {
        return selectedEnvironmentID;
    }

    public static void setSelectedEnvironmentID(int selectedEnvironmentID) {
        GlobalVariables.selectedEnvironmentID = selectedEnvironmentID;
    }

    public static ArrayList<ApiIssueModel> getSavedIssues() {
        return SavedIssues;
    }

    public static void setSavedIssues(ArrayList<ApiIssueModel> savedIssues) {
        SavedIssues = savedIssues;
    }

    public static ArrayList<PicklistModel> getPicklistsToBeSaved() {
        return picklistsToBeSaved;
    }

    public static void setPicklistsToBeSaved(ArrayList<PicklistModel> picklistsToBeSaved) {
        GlobalVariables.picklistsToBeSaved = picklistsToBeSaved;
    }

    public static ArrayList<ItemModel> getOrdersToBeIssued() {
        return ordersToBeIssued;
    }

    public static void setOrdersToBeIssued(ArrayList<ItemModel> ordersToBeIssued) {
        GlobalVariables.ordersToBeIssued = ordersToBeIssued;
    }

    public static String getPendingOrderType() {
        return pendingOrderType;
    }

    public static void setPendingOrderType(String pendingOrderType) {
        GlobalVariables.pendingOrderType = pendingOrderType;
    }

    public static ArrayList<ItemModel> getOrdersToBeApporved() {
        return ordersToBeApporved;
    }

    public static void setOrdersToBeApporved(ArrayList<ItemModel> ordersToBeApporved) {
        GlobalVariables.ordersToBeApporved = ordersToBeApporved;
    }

    public static ArrayList<ItemModel> getItems() {
        return items;
    }

    public static void setItems(ArrayList<ItemModel> items) {
        GlobalVariables.items = items;
    }

    public static String getRImodel19() {
        return RImodel19;
    }

    public static void setRImodel19(String RImodel19) {
        GlobalVariables.RImodel19 = RImodel19;
    }

    public static ReceiveInvoiceDetailModel getRIMaster() {
        return RIMaster;
    }

    public static void setRIMaster(ReceiveInvoiceDetailModel RIMaster) {
        GlobalVariables.RIMaster = RIMaster;
    }

    public static ReceiveInvoiceMastersDetailModel getRIDetail() {
        return RIDetail;
    }

    public static void setRIDetail(ReceiveInvoiceMastersDetailModel RIDetail) {
        GlobalVariables.RIDetail = RIDetail;
    }

    public static ArrayList<AddNewReceiptInvoiceDetailModel> getReceiptInvoiceDetail() {
        return ReceiptInvoiceDetail;
    }

    public static void setReceiptInvoiceDetail(ArrayList<AddNewReceiptInvoiceDetailModel> receiptInvoiceDetail) {
        ReceiptInvoiceDetail = receiptInvoiceDetail;
    }

    public static ArrayList<ItemModel> getDraftOrderItems() {
        return DraftOrderItems;
    }

    public static void setDraftOrderItems(ArrayList<ItemModel> draftOrderItems) {
        DraftOrderItems = draftOrderItems;
    }

    public static ArrayList<ItemModel> getDraftPOItems() {
        return DraftPOItems;
    }

    public static void setDraftPOItems(ArrayList<ItemModel> draftPOItems) {
        DraftPOItems = draftPOItems;
    }

    public static String getSelectedEnvironmentCode() {
        return selectedEnvironmentCode;
    }

    public static void setSelectedEnvironmentCode(String selectedEnvironmentCode) {
        GlobalVariables.selectedEnvironmentCode = selectedEnvironmentCode;
    }

    public static String getVVMCode() {
        return VVMCode;
    }

    public static void setVVMCode(String VVMCode) {
        GlobalVariables.VVMCode = VVMCode;
    }

    public static int getVVMID() {
        return VVMID;
    }

    public static void setVVMID(int VVMID) {
        GlobalVariables.VVMID = VVMID;
    }

    public static int getPosition() {
        return Position;
    }

    public static void setPosition(int position) {
        Position = position;
    }

    public static String getSnackBarMessage() {
        return snackBarMessage;
    }

    public static void setSnackBarMessage(String snackBarMessage) {
        GlobalVariables.snackBarMessage = snackBarMessage;
    }

    public static int getReceiptID() {
        return receiptID;
    }

    public static void setReceiptID(int receiptID) {
        GlobalVariables.receiptID = receiptID;
    }

    public static String getActivityCode() {
        return activityCode;
    }

    public static void setActivityCode(String activityCode) {
        GlobalVariables.activityCode = activityCode;
    }
}
