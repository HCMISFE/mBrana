package com.jsi.mbrana.DataAccessLayer;

import android.content.Context;

/**
 * Created by Surafel Nigussie on 12/11/2017.
 */

public class DataServiceAgent {
    public static DataServiceInterface getDataService(Context context){
        return DataServiceClient.getClient(context).create(DataServiceInterface.class);
    }
}