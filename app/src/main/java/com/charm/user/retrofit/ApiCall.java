package com.charm.user.retrofit;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

import com.charm.user.Constants;
import com.charm.user.DialogVerificationCode;
import com.charm.user.LoginActivity;
import com.charm.user.MainActivity;
import com.charm.user.PrefManager;
import com.charm.user.R;
import com.charm.user.Utility;
import com.charm.user.responseModel.EmployeeLoginResponse;
import com.charm.user.responseModel.ResetPasswordResponse;
import com.charm.user.responseModel.VerificationCodeResponse;
import com.charm.user.responseModel.EmployeeStatusResponse;
import com.charm.user.responseModel.RegisterOwnerResponse;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.RequiresApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ApiCall {

    private static final String TAG = "ApiCall";
    private static ApiInterface apiInterface;

    private static PrefManager prefManager;

    public static void checkUser(final Activity activity, String loginToken) {
        prefManager = new PrefManager(activity);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EmployeeStatusResponse> employeeStatusCall = apiInterface.checkUser(loginToken);
        employeeStatusCall.enqueue(new Callback<EmployeeStatusResponse>() {
            @Override
            public void onResponse(@NotNull Call<EmployeeStatusResponse> call, @NotNull Response<EmployeeStatusResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Utility.printLog(TAG, "checkUser:onResponse:" + response.body());
                        if (response.body().getCode().equals("100")) {
                            Utility.startActivity(activity, MainActivity.class, true);
                        } else {
                            Utility.startActivity(activity, LoginActivity.class, true);
                        }
                    }
                } else {
                    if (response.code() == 502 || response.code() == 500){
                        Utility.showDialog(activity, "",activity.getResources().getString(R.string.internal_server_error));
                    }else {
                        Utility.showDialog(activity, activity.getResources().getString(R.string.KEY_ALERT),response.message());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<EmployeeStatusResponse> call, @NotNull Throwable t) {
                Utility.printLog(TAG, "checkUser:onFailure:Error:" + t.getMessage());
            }
        });
    }

    public static void verificationCode(final Activity activity, JsonObject jsonObject) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<VerificationCodeResponse> verificationCodeCall = apiInterface.verificationCode(jsonObject);
        verificationCodeCall.enqueue(new Callback<VerificationCodeResponse>() {
            @Override
            public void onResponse(@NotNull Call<VerificationCodeResponse> call, @NotNull Response<VerificationCodeResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Utility.printLog(TAG, "verificationCode:onResponse:" + response.body());
                        if (response.body().getCode().equals("100")) {
                            Utility.progressBarDialogDismiss();
                            DialogVerificationCode code = new DialogVerificationCode(activity, response.body().getVerificationCode(), R.style.DialogRounded);
                            code.show();
                        } else {
                            Utility.progressBarDialogDismiss();
                            Utility.showDialog(activity, activity.getResources().getString(R.string.KEY_ALERT), response.body().getMessage());
                        }
                    }
                } else {
                    Utility.progressBarDialogDismiss();
                    if (response.code() == 502 || response.code() == 500){
                        Utility.showDialog(activity, "",activity.getResources().getString(R.string.internal_server_error));
                    }else {
                        Utility.showDialog(activity, activity.getResources().getString(R.string.KEY_ALERT),response.message());
                    }
                }
                Utility.progressBarDialogDismiss();
            }

            @Override
            public void onFailure(@NotNull Call<VerificationCodeResponse> call, @NotNull Throwable t) {
                Utility.progressBarDialogDismiss();
                Utility.printLog(TAG, "verificationCode:onFailure:Error:" + t.getMessage());
            }
        });
    }

    public static void register(final Activity activity, JsonObject jsonObject) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterOwnerResponse> registerOwnerCall = apiInterface.register(jsonObject);
        registerOwnerCall.enqueue(new Callback<RegisterOwnerResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterOwnerResponse> call, @NotNull Response<RegisterOwnerResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Utility.printLog(TAG, "register:onResponse:" + response.body());
                        if (response.body().getCode().equals("100")) {
                            Utility.progressBarDialogDismiss();
                            prefManager.setString(PrefManager.KEY_LOGIN_TOKEN, response.body().getLoginToken());
                            Utility.startActivity(activity, MainActivity.class, false);
                        } else {
                            Utility.progressBarDialogDismiss();
                            Utility.showDialog(activity, activity.getResources().getString(R.string.KEY_ERROR), response.body().getMessage());
                        }
                    }
                } else {
                    Utility.progressBarDialogDismiss();
                    if (response.code() == 502 || response.code() == 500){
                        Utility.showDialog(activity, "",activity.getResources().getString(R.string.internal_server_error));
                    }else {
                        Utility.showDialog(activity, activity.getResources().getString(R.string.KEY_ALERT),response.message());
                    }
                }

                Utility.progressBarDialogDismiss();
            }

            @Override
            public void onFailure(@NotNull Call<RegisterOwnerResponse> call, @NotNull Throwable t) {
                Utility.progressBarDialogDismiss();
                Utility.printLog(TAG, "register:onFailure:Error:" + t.getMessage());
            }
        });
    }

    public static void resetPassword(final Activity activity, JsonObject jsonObject) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResetPasswordResponse> verificationCodeCall = apiInterface.resetPassword(jsonObject);
        verificationCodeCall.enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(@NotNull Call<ResetPasswordResponse> call, @NotNull Response<ResetPasswordResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Utility.printLog(TAG, "resetPassword:onResponse:ResponseCode:" + response.body().getCode());
                        if (response.body().getCode().equals("100")) {
                            Utility.progressBarDialogDismiss();
                            ((LoginActivity) activity).forgotPasswordDialogDismiss();
                        } else {
                            Utility.progressBarDialogDismiss();
                            Utility.showDialog(activity, activity.getResources().getString(R.string.KEY_ALERT), response.body().getMessage());
                            ((LoginActivity) activity).forgotPasswordDialogDismiss();
                        }
                    }
                } else {
                    Utility.progressBarDialogDismiss();
                    if (response.code() == 502 || response.code() == 500){
                        Utility.showDialog(activity, "",activity.getResources().getString(R.string.internal_server_error));
                    }else {
                        Utility.showDialog(activity, activity.getResources().getString(R.string.KEY_ALERT),response.message());
                    }
                }
                Utility.progressBarDialogDismiss();
            }

            @Override
            public void onFailure(@NotNull Call<ResetPasswordResponse> call, @NotNull Throwable t) {
                Utility.progressBarDialogDismiss();
                Utility.printLog(TAG, "resetPassword:onFailure:Error:" + t.getMessage());
            }
        });
    }

    public static void login(final Activity activity, JsonObject jsonObject) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EmployeeLoginResponse> employeeLoginCall = apiInterface.login(jsonObject);
        employeeLoginCall.enqueue(new Callback<EmployeeLoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<EmployeeLoginResponse> call, @NotNull Response<EmployeeLoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Utility.printLog(TAG, "login:onResponse:" + response.body());
                        if (response.body().getCode().equals("100")) {
                            Utility.progressBarDialogDismiss();
                            prefManager.setString(PrefManager.KEY_LOGIN_TOKEN, response.body().getLoginToken());
                            if ((!prefManager.getString(PrefManager.KEY_LOGIN_TOKEN, "").isEmpty())) {
                                Utility.startActivity(activity, MainActivity.class, false);
                            } else {
                                Utility.startActivity(activity, LoginActivity.class, false);
                            }

                        } else {
                            Utility.progressBarDialogDismiss();
                            Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.body().getCode().equals("1014")) {
                                Utility.startActivity(activity, LoginActivity.class, false);
                            }
                        }
                    }
                } else {
                    Utility.progressBarDialogDismiss();
                    if (response.code() == 502 || response.code() == 500){
                        Utility.showDialog(activity, "",activity.getResources().getString(R.string.internal_server_error));
                    }else {
                        Utility.showDialog(activity, activity.getResources().getString(R.string.KEY_ALERT),response.message());
                    }
                }
                Utility.progressBarDialogDismiss();
            }

            @Override
            public void onFailure(@NotNull Call<EmployeeLoginResponse> call, @NotNull Throwable t) {
                Utility.progressBarDialogDismiss();
                Utility.printLog(TAG, "login:onFailure:Error:" + t.getMessage());
            }
        });
    }

}
