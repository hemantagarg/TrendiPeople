package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.ChangePassword;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.activities.VendorDashboard;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONObject;


public class UserSettingFragment extends BaseFragment implements View.OnClickListener, ApiResponse {


    public static UserSettingFragment settingFragment;
    private Activity mActivity;
    private View view;
    private final String TAG = UserSettingFragment.class.getSimpleName();
    private RelativeLayout rl_profile, rl_changePassword, rl_my_booking, rl_switch_freelancer, rl_share_us, rl_rate_us, rl_logout;

    public static UserSettingFragment getInstance() {
        if (settingFragment == null)
            settingFragment = new UserSettingFragment();
        return settingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.setting_fragment, container, false);
        mActivity = getActivity();
        settingFragment = this;
        initViews();
        manageHeaderView();
        return view;
    }

    private void initViews() {

        rl_profile = (RelativeLayout) view.findViewById(R.id.rl_profile);
        rl_changePassword = (RelativeLayout) view.findViewById(R.id.rl_changePassword);
        rl_my_booking = (RelativeLayout) view.findViewById(R.id.rl_my_booking);
        rl_switch_freelancer = (RelativeLayout) view.findViewById(R.id.rl_switch_freelancer);
        rl_share_us = (RelativeLayout) view.findViewById(R.id.rl_share_us);
        rl_rate_us = (RelativeLayout) view.findViewById(R.id.rl_rate_us);
        rl_logout = (RelativeLayout) view.findViewById(R.id.rl_logout);

        rl_profile.setOnClickListener(this);
        rl_changePassword.setOnClickListener(this);
        rl_my_booking.setOnClickListener(this);
        rl_switch_freelancer.setOnClickListener(this);
        rl_share_us.setOnClickListener(this);
        rl_rate_us.setOnClickListener(this);
        rl_logout.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();

        if (UserDashboard.currentFragment instanceof UserSettingFragment) {

        }
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.setting));
        HeaderViewManager.getInstance().setLeftSideHeaderView(false, R.drawable.left_arrow);
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


    protected void setFragment(Fragment fragment) {
        UserDashboard.getInstance().pushFragments(GlobalConstants.TAB_PROFILE_BAR, fragment, true);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_logout:
                showLogoutBox();
                break;

            case R.id.rl_rate_us:
                Uri uri = Uri.parse("market://details?id=" + mActivity.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + mActivity.getPackageName())));
                }

                break;
            case R.id.rl_share_us:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "TrendiPeople ,a fastest growing App ,It is very useful for all" +
                        ", So why are you waiting Get it now." + "http://play.google.com/store/apps/details?id=" + mActivity.getPackageName());
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.rl_profile:

                UserProfileFragment userProfileFragment = new UserProfileFragment();
                setFragment(userProfileFragment);
                break;
            case R.id.rl_my_booking:

                UserBookingFragment userBookingFragment = new UserBookingFragment();
                setFragment(userBookingFragment);
                break;

            case R.id.rl_switch_freelancer:
                if (AppUtils.getBusinessId(mActivity).equalsIgnoreCase("")) {
                    UserDashboard.getInstance().pushFragments(GlobalConstants.TAB_PROFILE_BAR, new UserProfileFragment(), true);
                } else {
                    submitRequest();
                }
                break;

            case R.id.rl_changePassword:

                ChangePassword changePassword = new ChangePassword();
                setFragment(changePassword);
                break;
        }
    }

    private void showLogoutBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                mActivity);

        alertDialog.setTitle("LOG OUT !");
        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AppUtils.setUserId(mActivity, "");
                        UserDashboard.getInstance().onResume();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    private void submitRequest() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/trendi/api/switch-freelancer.php?user_id=200&user_role=3
            String url = JsonApiHelper.BASEURL + JsonApiHelper.SWITCH_FREELANCER + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&user_role=" + AppConstant.FREELANCER;
            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {

                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    Intent intent = new Intent(mActivity, VendorDashboard.class);
                    AppUtils.setUserRole(mActivity, AppConstant.FREELANCER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

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
