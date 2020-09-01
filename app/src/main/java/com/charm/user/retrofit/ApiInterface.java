package com.charm.user.retrofit;

import com.charm.user.responseModel.EmployeeLoginResponse;
import com.charm.user.responseModel.EmployeeLogoutResponse;
import com.charm.user.responseModel.ResetEmployeePasswordResponse;
import com.charm.user.responseModel.EmployeeStatusResponse;
import com.charm.user.responseModel.RegisterOwnerResponse;
import com.charm.user.responseModel.VerificationCodeResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("checkuser")
    Call<EmployeeStatusResponse> checkUser(@Header("Authorization") String loginToken);

    @POST("verificationcode")
    Call<VerificationCodeResponse> verificationCode(@Body JsonObject jsonObject);

    @POST("register")
    Call<RegisterOwnerResponse> register(@Body JsonObject jsonObject);

    @POST("resetemployeepassword")
    Call<ResetEmployeePasswordResponse> resetEmployeePassword(@Body JsonObject jsonObject);

    @POST("login")
    Call<EmployeeLoginResponse> login(@Body JsonObject jsonObject);

    @POST("employeelogout")
    Call<EmployeeLogoutResponse> employeeLogout(@Body JsonObject jsonObject);

}