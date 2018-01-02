package com.jsi.mbrana.Models;

import java.util.ArrayList;

/**
 * Created by user on 21/3/2017.
 */

public class ApiRRFModel {
    private RRFHeaderModel VrfHeader;
    private ArrayList<RRFModel> VrfDetailModels;

    public RRFHeaderModel getVrfHeader() {
        return VrfHeader;
    }

    public void setVrfHeader(RRFHeaderModel vrfHeader) {
        VrfHeader = vrfHeader;
    }

    public ArrayList<RRFModel> getVrfDetailModels() {
        return VrfDetailModels;
    }

    public void setVrfDetailModels(ArrayList<RRFModel> vrfDetailModels) {
        VrfDetailModels = vrfDetailModels;
    }
}
