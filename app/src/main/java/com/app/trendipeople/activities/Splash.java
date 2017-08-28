package com.app.trendipeople.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class Splash extends AppCompatActivity implements ApiResponse {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        context = this;

        Log.e("token", "*" + AppUtils.getGcmRegistrationKey(context));
        getCategoryList();

    }


    private void getCategoryList() {

        // http://onlineworkpro.com/trendi/api/category.php?type=1
        if (AppUtils.isNetworkAvailable(context)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.ALLSERVICE_CATEGORYlIST;
            new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

        } else {

            if (AppUtils.getService(context).equalsIgnoreCase("")) {
                Toast.makeText(context, getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (AppUtils.getUserRole(context).equalsIgnoreCase(AppConstant.FREELANCER)) {
                            if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
                                startActivity(new Intent(context, LoginActivity.class));
                                finish();
                            }else {
                                startActivity(new Intent(context, VendorDashboard.class));
                                finish();
                            }
                        } else if (AppUtils.getUserRole(context).equalsIgnoreCase(AppConstant.USER)) {
                            startActivity(new Intent(context, UserDashboard.class));
                            finish();
                        }else{
                            startActivity(new Intent(context, UserDashboard.class));
                            finish();
                        }

                    }
                }, 1000);

            }
        }

    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {

                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("services");

                    AppUtils.setService(context, array.toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (AppUtils.getUserRole(context).equalsIgnoreCase(AppConstant.FREELANCER)) {
                                if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
                                    startActivity(new Intent(context, LoginActivity.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(context, VendorDashboard.class));
                                    finish();
                                }
                            } else if (AppUtils.getUserRole(context).equalsIgnoreCase(AppConstant.USER)) {
                                startActivity(new Intent(context, UserDashboard.class));
                                finish();
                            }else{
                                startActivity(new Intent(context, UserDashboard.class));
                                finish();
                            }
                        }
                    }, 100);


                } else {
                    Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
