package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.interfaces.JsonApiHelper;

import org.json.JSONObject;

/**
 * Created by hemanta on 02-08-2017.
 */

public class ChangePasswordFragment extends AppCompatActivity implements ApiResponse {


    private Button btnSubmit;
    private EditText edtEmail;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mActivity = this;
        init();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtEmail.getText().toString().equalsIgnoreCase("")) {

                    if (AppUtils.isEmailValid(edtEmail.getText().toString())) {
                        submitRequest();

                    } else {
                        edtEmail.setError(getString(R.string.enter_valid_emailid));
                        edtEmail.requestFocus();
                    }
                } else {
                    if (edtEmail.getText().toString().equalsIgnoreCase("")) {
                        edtEmail.setError(getString(R.string.enter_email));
                        edtEmail.requestFocus();
                    }
                }
            }
        });


    }

    private void submitRequest() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/trendi/api/forget-password.php?mobile=aaa@gmail.com
            String url = JsonApiHelper.BASEURL + JsonApiHelper.FORGOTPASSWORD + "mobile=" + edtEmail.getText().toString();
            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    private void init() {

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {

                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
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


