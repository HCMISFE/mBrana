package com.jsi.mbrana.Modules.Receive.Helper.Status;

import com.jsi.mbrana.R;

/**
 * Created by Surafel Nigussie on 12/21/2017.
 */

public class StatusHelperModel {
    public static class ReceiveStatus {
        public static StatusModel DRAFT = new StatusModel("Draft", "DRFT", "D", R.color.material_primary);
        public static StatusModel SUBMITTED = new StatusModel("Submitted", "SBMT", "S", R.color.material_primary);
        public static StatusModel CONFIRMED = new StatusModel("Confirmed", "CNF", "C", R.color.material_primary);
        public static StatusModel CANCELED = new StatusModel("Canceled", "CNLD", "C", R.color.material_primary);
        public static StatusModel VOIDED = new StatusModel("Voided", "VOID", "V", R.color.material_primary);

        public static String getReceiveStatusFirstLetter(String status) {
            if (status.equals(DRAFT.getStatusCode()))
                return String.valueOf(DRAFT.getStatusName().charAt(0));
            else if (status.equals(SUBMITTED.getStatusCode()))
                return String.valueOf(SUBMITTED.getStatusName().charAt(0));
            else if (status.equals(CONFIRMED.getStatusCode()))
                return String.valueOf(CONFIRMED.getStatusName().charAt(0));
            else if (status.equals(CANCELED.getStatusCode()))
                return String.valueOf(CANCELED.getStatusName().charAt(0));
            else if (status.equals(VOIDED.getStatusCode()))
                return String.valueOf(VOIDED.getStatusName().charAt(0));
            else
                return "";
        }

        public static String getReceiveStatusName(String status){
            if (status.equals(DRAFT.getStatusCode()))
                return String.valueOf(DRAFT.getStatusName());
            else if (status.equals(SUBMITTED.getStatusCode()))
                return String.valueOf(SUBMITTED.getStatusName());
            else if (status.equals(CONFIRMED.getStatusCode()))
                return String.valueOf(CONFIRMED.getStatusName());
            else if (status.equals(CANCELED.getStatusCode()))
                return String.valueOf(CANCELED.getStatusName());
            else if (status.equals(VOIDED.getStatusCode()))
                return String.valueOf(VOIDED.getStatusName());
            else
                return "";
        }

        public static int getReceiveStatusBackground(String status){
            if (status.equals(DRAFT.getStatusCode()))
                return R.drawable.circle_orange;
            else if (status.equals(SUBMITTED.getStatusCode()))
                return R.drawable.circle_green;
            else if (status.equals(CONFIRMED.getStatusCode()))
                return R.drawable.circle_blue;
            else if (status.equals(CANCELED.getStatusCode()))
                return R.drawable.circle_red;
            else if (status.equals(VOIDED.getStatusCode()))
                return R.drawable.circle_red;
            else
                return R.drawable.circle_blue;
        }
    }
}