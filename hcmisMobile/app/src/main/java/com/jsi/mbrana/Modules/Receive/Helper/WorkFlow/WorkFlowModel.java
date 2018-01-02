package com.jsi.mbrana.Modules.Receive.Helper.WorkFlow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Surafel Nigussie on 12/22/2017.
 */

public class WorkFlowModel {
    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("Remark")
    @Expose
    private String remark;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
