package com.jsi.mbrana.DataAccessLayer;

import com.jsi.mbrana.CommonModels.LoginModel;
import com.jsi.mbrana.CommonModels.UserIdentityModel;
import com.jsi.mbrana.CommonModels.VVMModel;
import com.jsi.mbrana.Modules.Receive.Helper.WorkFlow.WorkFlowModel;
import com.jsi.mbrana.Modules.Receive.Model.ReceiptInvoice.ReceiveInvoiceModel;
import com.jsi.mbrana.Modules.Receive.Model.Receive.ReceiveModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Surafel Nigussie on 12/11/2017.
 */

public interface DataServiceInterface {
    @GET("Lookups/VVMs")
    Call<List<VVMModel>> GetVVMs();

    @POST("Account/Authenticate")
    Call<UserIdentityModel> Authenticate(@Body LoginModel model);

    @GET("Environments/{environmentId}/ReceiveInvoices")
    Call<List<ReceiveInvoiceModel>> GetReceiveInvoices(@Path("environmentId") int environmentId);

    @GET("Environments/{environmentId}/ReceiveInvoices/{receiveInvoiceId}")
    Call<ReceiveInvoiceModel> GetReceiveInvoiceById(@Path("environmentId") int environmentId, @Path("receiveInvoiceId") int receiveInvoiceId);

    @GET("Environments/{environmentId}/ReceiveInvoices/{receiveInvoiceId}/Receives")
    Call<List<ReceiveModel>> GetReceives(@Path("environmentId") int environmentId, @Path("receiveInvoiceId") int receiveInvoiceId);

    @GET("Environments/{environmentId}/ReceiveInvoices/{receiveInvoiceId}/Receives/{receiveId}")
    Call<ReceiveModel> GetReceive(@Path("environmentId") int environmentId, @Path("receiveInvoiceId") int receiveInvoiceId, @Path("receiveId") int receiveId);

    @POST("Environments/{environmentId}/ReceiveInvoices/{receiveInvoiceId}/Receives")
    Call<ReceiveModel> SaveReceiveInvoice(@Path("environmentId") int environmentId, @Path("receiveInvoiceId") int receiveInvoiceId, @Body ReceiveModel receiveModel);

    @PUT("Environments/{environmentId}/ReceiveInvoices/{receiveInvoiceId}/Receives/{receiveId}")
    Call<Void> UpdateReceiveInvoice(@Path("environmentId") int environmentId, @Path("receiveInvoiceId") int receiveInvoiceId, @Path("receiveId") int receiveId, @Body ReceiveModel receiveModel);

    @POST("Environments/{environmentId}/ReceiveInvoices/{receiveInvoiceId}/Receives/{receiveId}/Workflow")
    Call<Void> setReceiveWorkFlow(@Path("environmentId") int environmentId, @Path("receiveInvoiceId") int receiveInvoiceId, @Path("receiveId") int receiveId, @Body WorkFlowModel model);
}