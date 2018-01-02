package com.jsi.mbrana.Models;

import java.util.ArrayList;

/**
 * Created by pc-6 on 6/21/2016.
 */
public class API_PicklistDetailModel {
    PicklistHeaderModel PickList;
    ArrayList<PicklistModel> PickListDetails ;

    public void setPickList(PicklistHeaderModel pickList) {
        this.PickList = pickList;
    }

    public PicklistHeaderModel getPickList() {
        return PickList;
    }

    public void setPickListDetails(ArrayList<PicklistModel> pickListDetails) {
        PickListDetails = pickListDetails;
    }

    public ArrayList<PicklistModel> getPickListDetails() {
        return PickListDetails;
    }
}
