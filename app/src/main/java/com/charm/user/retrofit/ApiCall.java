package com.charm.user.retrofit;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

import com.charm.user.Constants;
import com.charm.user.DialogForgotPassword;
import com.charm.user.DialogProgressBar;
import com.charm.user.DialogVerificationCode;
import com.charm.user.EmployeeRegisterActivity;
import com.charm.user.HomeActivity;
import com.charm.user.LoginActivity;
import com.charm.user.MainActivity;
import com.charm.user.OwnerRegisterActivity;
import com.charm.user.PrefManager;
import com.charm.user.RegistrationStepsActivity;
import com.charm.user.SplashActivity;
import com.charm.user.Utility;
import com.charm.user.responseModel.EmployeeLoginResponse;
import com.charm.user.responseModel.EmployeeLogoutResponse;
import com.charm.user.responseModel.RegisterEmployeeResponse;
import com.charm.user.responseModel.ResetEmployeePasswordResponse;
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
                            Utility.startActivity(activity, MainActivity.class, false);
                        } else {
                            Utility.startActivity(activity, LoginActivity.class, false);
                        }
                    }
                } else {
                    Utility.showDialog(activity, Constants.KEY_ALERT,response.message());
                }
                //((SplashActivity) activity).hideProgressBar();
                ((SplashActivity) activity).dialogProgressBarDiamiss();
            }

            @Override
            public void onFailure(@NotNull Call<EmployeeStatusResponse> call, @NotNull Throwable t) {
                ((SplashActivity) activity).hideProgressBar();
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
                            DialogVerificationCode code = new DialogVerificationCode(activity, response.body().getVerificationCode());
                            code.show();
                            code.setCanceledOnTouchOutside(false);
                        } else {
                            Utility.showDialog(activity, Constants.KEY_ALERT,response.body().getMessage());
                        }
                    }
                } else {
                    Utility.showDialog(activity, Constants.KEY_ALERT,response.message());
                }
                ((EmployeeRegisterActivity) activity).hideProgressBar();
            }

            @Override
            public void onFailure(@NotNull Call<VerificationCodeResponse> call, @NotNull Throwable t) {
                ((EmployeeRegisterActivity) activity).hideProgressBar();
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
                            prefManager.setString(PrefManager.KEY_LOGIN_TOKEN, response.body().getLoginToken());
                            Utility.startActivity(activity, MainActivity.class, false);
                        } else {
                            Utility.showDialog(activity, Constants.KEY_ERROR,response.body().getMessage());
                        }
                    }
                } else {
                    Utility.showDialog(activity, Constants.KEY_ALERT,response.message());
                }

                ((EmployeeRegisterActivity) activity).hideProgressBar();
            }

            @Override
            public void onFailure(@NotNull Call<RegisterOwnerResponse> call, @NotNull Throwable t) {
                ((EmployeeRegisterActivity) activity).hideProgressBar();
                Utility.printLog(TAG, "register:onFailure:Error:" + t.getMessage());
            }
        });
    }

    public static void resetEmployeePassword(final Activity activity, JsonObject jsonObject) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResetEmployeePasswordResponse> verificationCodeCall = apiInterface.resetEmployeePassword(jsonObject);
        verificationCodeCall.enqueue(new Callback<ResetEmployeePasswordResponse>() {
            @Override
            public void onResponse(@NotNull Call<ResetEmployeePasswordResponse> call, @NotNull Response<ResetEmployeePasswordResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Utility.printLog(TAG, "resetEmployeePassword:onResponse:ResponseCode:" + response.body().getCode());
                        if (response.body().getCode().equals("100")) {
                            ((LoginActivity) activity).forgotPasswordDialogDismiss();
                        } else {
                            Utility.showDialog(activity, Constants.KEY_ALERT,response.body().getMessage());
                            ((LoginActivity) activity).forgotPasswordDialogDismiss();
                        }
                    }
                } else {
                    Utility.showDialog(activity, Constants.KEY_ALERT,response.message());
                }
                ((LoginActivity) activity).hideProgressBar();
            }

            @Override
            public void onFailure(@NotNull Call<ResetEmployeePasswordResponse> call, @NotNull Throwable t) {
                ((LoginActivity) activity).hideProgressBar();
                Utility.printLog(TAG, "resetEmployeePassword:onFailure:Error:" + t.getMessage());
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
                            prefManager.setString(PrefManager.KEY_LOGIN_TOKEN, response.body().getLoginToken());
                            if ((!prefManager.getString(PrefManager.KEY_LOGIN_TOKEN, "").isEmpty())){
                                Utility.startActivity(activity, MainActivity.class, false);
                            }else {
                                Utility.startActivity(activity, LoginActivity.class, false);
                            }

                        } else {
                            Toast.makeText(activity,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            if (response.body().getCode().equals("1014")) {
                                Utility.startActivity(activity, LoginActivity.class, false);
                            }
                        }
                    }
                } else {
                    Utility.showDialog(activity, Constants.KEY_ALERT,response.message());
                }
                ((LoginActivity) activity).hideProgressBar();
            }

            @Override
            public void onFailure(@NotNull Call<EmployeeLoginResponse> call, @NotNull Throwable t) {
                ((LoginActivity) activity).hideProgressBar();
                Utility.printLog(TAG, "login:onFailure:Error:" + t.getMessage());
            }
        });
    }

    public static void employeeLogout(final Activity activity, JsonObject jsonObject) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EmployeeLogoutResponse> employeeLogoutCall = apiInterface.employeeLogout(jsonObject);
        employeeLogoutCall.enqueue(new Callback<EmployeeLogoutResponse>() {
            @Override
            public void onResponse(@NotNull Call<EmployeeLogoutResponse> call, @NotNull Response<EmployeeLogoutResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Utility.printLog(TAG, "employeeLogout:onResponse:" + response.body());
                        if (response.body().getCode().equals("100")) {
                            Utility.startActivity(activity, HomeActivity.class, false);
                        } else {
                            if (response.body().getCode().equals("1014")) {
                                Utility.startActivity(activity, HomeActivity.class, false);
                            }
                        }
                    }
                } else {
                    Utility.showDialog(activity, Constants.KEY_ALERT,response.message());
                }
                ((RegistrationStepsActivity) activity).hideProgressBar();

            }

            @Override
            public void onFailure(@NotNull Call<EmployeeLogoutResponse> call, @NotNull Throwable t) {
                ((RegistrationStepsActivity) activity).hideProgressBar();
                Utility.printLog(TAG, "employeeLogout:onFailure:Error:" + t.getMessage());
            }
        });
    }

}
