package com.app.trendipeople.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.userfragments.BaseFragment;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONObject;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by hemanta on 02-08-2017.
 */

public class ChangePassword extends BaseFragment implements ApiResponse {

    public static ChangePassword changePassword;
    private Button btnSubmit;
    private EditText edtold_password, edt_newpassword, edtconfirmpassword;
    private Activity mActivity;
    private View view;


    public static ChangePassword getInstance() {
        if (changePassword == null)
            changePassword = new ChangePassword();
        return changePassword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_change_password, container, false);
        mActivity = getActivity();
        changePassword = this;
        init();
        manageHeaderView();
        return view;
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.change_password));
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.search);
    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        return new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                AppUtils.showLog(TAG, "onClickOfHeaderLeftView");
                mActivity.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void submitRequest() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/trendi/api/change-password.php?user_id=199&current_pwd=admin&new_pwd=123456&confirm_pwd=123456
            String url = JsonApiHelper.BASEURL + JsonApiHelper.CHANGEPASSWORD + "user_id=" + 199 + "&current_pwd=" + edtold_password.getText().toString()
                    + "&new_pwd=" + edt_newpassword.getText().toString() + "&confirm_pwd=" + edtconfirmpassword.getText().toString();


            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    private void init() {

        edtold_password = (EditText) view.findViewById(R.id.edtold_password);
        edt_newpassword = (EditText) view.findViewById(R.id.edt_newpassword);
        edtconfirmpassword = (EditText) view.findViewById(R.id.edtconfirmpassword);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtold_password.getText().toString().equalsIgnoreCase("") && !edt_newpassword.getText().toString().equalsIgnoreCase("") && !edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {

                    if (edt_newpassword.getText().toString().equals(edtconfirmpassword.getText().toString())) {
                        submitRequest();
                    } else {
                        Toast.makeText(mActivity, "Password does not match", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    if (edtold_password.getText().toString().equalsIgnoreCase("")) {
                        edtold_password.requestFocus();
                        edtold_password.setError("Enter Old Password");
                    } else if (edt_newpassword.getText().toString().equalsIgnoreCase("")) {
                        edt_newpassword.requestFocus();
                        edt_newpassword.setError("Enter New Password");
                    } else if (edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {
                        edtconfirmpassword.requestFocus();
                        edtconfirmpassword.setError("Confirm password");
                    }
                }
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
                    mActivity.onBackPressed();
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


