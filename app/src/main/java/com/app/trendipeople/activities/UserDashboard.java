package com.app.trendipeople.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.trendipeople.R;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.userfragments.AlertFragment;
import com.app.trendipeople.userfragments.BaseFragment;
import com.app.trendipeople.userfragments.Fragment_Chat;
import com.app.trendipeople.userfragments.HomeFragment;
import com.app.trendipeople.userfragments.ServiceTypeFragment;
import com.app.trendipeople.userfragments.UserSettingFragment;
import com.app.trendipeople.userfragments.SubCategoryFragment;
import com.app.trendipeople.userfragments.VendorListFragment;
import com.app.trendipeople.userfragments.WalletFragment;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;

import java.util.HashMap;
import java.util.Stack;

public class UserDashboard extends AppCompatActivity implements View.OnClickListener {

    private Activity mActivity;
    private RelativeLayout rl_home, rl_alert, rl_wallet, rl_profile, rl_message, rlLoginSignup;
    private FrameLayout home_container, alert_container, wallet_container, profile_container, message_container;
    private LinearLayout ll_bottom;
    private TextView textLoginSignup;
    private static UserDashboard mInstance;
    private static final String TAG = UserDashboard.class.getSimpleName();
    private String mCurrentTab;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA,
    };
    /*
    * Fragment instance
    * */
    public static volatile Fragment currentFragment;

    private HashMap<String, Stack<Fragment>> mStacks;

    /***********************************************
     * Function Name : getInstance
     * Description : This function will return the instance of this activity.
     *
     * @return
     */
    public static UserDashboard getInstance() {
        if (mInstance == null)
            mInstance = new UserDashboard();
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_dashboard);

        init();
        AppUtils.setUserRole(mActivity, AppConstant.USER);
        mStacks = new HashMap<>();
        mStacks.put(GlobalConstants.TAB_HOME_BAR, new Stack<Fragment>());
        mStacks.put(GlobalConstants.TAB_ALERT_BAR, new Stack<Fragment>());
        mStacks.put(GlobalConstants.TAB_WALLET_BAR, new Stack<Fragment>());
        mStacks.put(GlobalConstants.TAB_PROFILE_BAR, new Stack<Fragment>());
        mStacks.put(GlobalConstants.TAB_MESSAGE_BAR, new Stack<Fragment>());

        if (!AppUtils.getUserId(mActivity).equalsIgnoreCase("")) {
            pushFragments(GlobalConstants.TAB_ALERT_BAR, new AlertFragment(), true);
            pushFragments(GlobalConstants.TAB_WALLET_BAR, new WalletFragment(), true);
            pushFragments(GlobalConstants.TAB_MESSAGE_BAR, new Fragment_Chat(), true);
            pushFragments(GlobalConstants.TAB_PROFILE_BAR, new UserSettingFragment(), true);
        }
        pushFragments(GlobalConstants.TAB_HOME_BAR, new HomeFragment(), true);
    }

    @Override
    public void onResume() {
        super.onResume();
        manageFooterVisibitlity();
        // activeHomeFragment();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void init() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        mActivity = UserDashboard.this;
        mInstance = UserDashboard.this;
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_alert = (RelativeLayout) findViewById(R.id.rl_alert);
        rl_wallet = (RelativeLayout) findViewById(R.id.rl_wallet);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        rlLoginSignup = (RelativeLayout) findViewById(R.id.rlLoginSignup);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        home_container = (FrameLayout) findViewById(R.id.home_container);
        alert_container = (FrameLayout) findViewById(R.id.alert_container);
        wallet_container = (FrameLayout) findViewById(R.id.wallet_container);
        message_container = (FrameLayout) findViewById(R.id.message_container);
        profile_container = (FrameLayout) findViewById(R.id.profile_container);
        textLoginSignup = (TextView) findViewById(R.id.textLoginSignup);

        rlLoginSignup.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        rl_alert.setOnClickListener(this);
        rl_wallet.setOnClickListener(this);
        rl_profile.setOnClickListener(this);
        rl_message.setOnClickListener(this);
    }

    public void manageFooterVisibitlity() {

        if (AppUtils.getUserId(mActivity).equalsIgnoreCase("")) {
            rlLoginSignup.setVisibility(View.VISIBLE);
            ll_bottom.setVisibility(View.GONE);
            activeHomeFragment();
        } else {
            rlLoginSignup.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.VISIBLE);
        }
    }


    /*********************************************************************************
     * Function Name - activeHomeFragment
     * <p/>
     * Description - active the view of home tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeHomeFragment() {

        mCurrentTab = GlobalConstants.TAB_HOME_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        home_container.setVisibility(View.VISIBLE);
        alert_container.setVisibility(View.GONE);
        message_container.setVisibility(View.GONE);
        profile_container.setVisibility(View.GONE);
        wallet_container.setVisibility(View.GONE);
        makeFragmentSelected(HomeFragment.class.getSimpleName());

    }

    /*********************************************************************************
     * Function Name - activeAlertFragment
     * <p/>
     * Description - active the view of alert tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeAlertFragment() {

        mCurrentTab = GlobalConstants.TAB_ALERT_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        home_container.setVisibility(View.GONE);
        alert_container.setVisibility(View.VISIBLE);
        message_container.setVisibility(View.GONE);
        profile_container.setVisibility(View.GONE);
        wallet_container.setVisibility(View.GONE);
        makeFragmentSelected(AlertFragment.class.getSimpleName());

    }

    /*********************************************************************************
     * Function Name - activeWalletFragment
     * <p/>
     * Description - active the view of wallet tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeWalletFragment() {

        mCurrentTab = GlobalConstants.TAB_WALLET_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        home_container.setVisibility(View.GONE);
        alert_container.setVisibility(View.GONE);
        message_container.setVisibility(View.GONE);
        profile_container.setVisibility(View.GONE);
        wallet_container.setVisibility(View.VISIBLE);
        makeFragmentSelected(WalletFragment.class.getSimpleName());

    }

    /*********************************************************************************
     * Function Name - activeProfileFragment
     * <p/>
     * Description - active the view of Profile tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeProfileFragment() {

        mCurrentTab = GlobalConstants.TAB_PROFILE_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        home_container.setVisibility(View.GONE);
        alert_container.setVisibility(View.GONE);
        message_container.setVisibility(View.GONE);
        profile_container.setVisibility(View.VISIBLE);
        wallet_container.setVisibility(View.GONE);
        makeFragmentSelected(UserSettingFragment.class.getSimpleName());

    }

    /*********************************************************************************
     * Function Name - activeMessageFragment
     * <p/>
     * Description - active the view of home tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeMessageFragment() {

        mCurrentTab = GlobalConstants.TAB_MESSAGE_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        home_container.setVisibility(View.GONE);
        alert_container.setVisibility(View.GONE);
        message_container.setVisibility(View.VISIBLE);
        profile_container.setVisibility(View.GONE);
        wallet_container.setVisibility(View.GONE);
        makeFragmentSelected(Fragment_Chat.class.getSimpleName());

    }

    /**
     * Method Name: makeFragmentSelected
     * Description: make the footer selected on selecting the fragment
     */
    private void makeFragmentSelected(String mFragmentName) {

        rl_home.setBackgroundColor(ContextCompat.getColor(mActivity, android.R.color.transparent));
        rl_alert.setBackgroundColor(ContextCompat.getColor(mActivity, android.R.color.transparent));
        rl_message.setBackgroundColor(ContextCompat.getColor(mActivity, android.R.color.transparent));
        rl_profile.setBackgroundColor(ContextCompat.getColor(mActivity, android.R.color.transparent));
        rl_wallet.setBackgroundColor(ContextCompat.getColor(mActivity, android.R.color.transparent));

        if (mFragmentName.equals(HomeFragment.class.getSimpleName())) {
            rl_home.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
        }
        if (mFragmentName.equals(UserSettingFragment.class.getSimpleName())) {
            rl_profile.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
        }
        if (mFragmentName.equals(AlertFragment.class.getSimpleName())) {
            rl_alert.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
        }
        if (mFragmentName.equals(WalletFragment.class.getSimpleName())) {
            rl_wallet.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
        }
        if (mFragmentName.equals(Fragment_Chat.class.getSimpleName())) {
            rl_message.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
        }
     /*    if (mFragmentName.equals(HomeFragment.class.getSimpleName())) {
            rl_message.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
        }
        if (mFragmentName.equals(HomeFragment.class.getSimpleName())) {
            rl_alert.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
        }*/
    }

    /*
         * To add fragment to a tab. tag -> Tab identifier fragment -> Fragment to
         * show, false when we switch tabs, or adding first fragment to a tab true
         * when we are pushing more fragment into navigation stack. shouldAdd ->
         * Should add to fragment navigation stack (mStacks.get(tag)). false when we
         * are switching tabs (except for the first time) true in all other cases.
         */
    public void pushFragments(String tag, Fragment fragment, boolean ShouldAdd) {
        if (fragment != null && currentFragment != fragment) {
            currentFragment = fragment;
            mCurrentTab = tag;
            if (ShouldAdd)
                mStacks.get(tag).add(fragment);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            if (tag.equals(GlobalConstants.TAB_HOME_BAR)) {
                ft.add(R.id.home_container, fragment);
                activeHomeFragment();
            } else if (tag.equals(GlobalConstants.TAB_ALERT_BAR)) {
                ft.add(R.id.alert_container, fragment);
                activeAlertFragment();
            } else if (tag.equals(GlobalConstants.TAB_MESSAGE_BAR)) {
                ft.add(R.id.message_container, fragment);
                activeMessageFragment();
            } else if (tag.equals(GlobalConstants.TAB_PROFILE_BAR)) {
                ft.add(R.id.profile_container, fragment);
                activeProfileFragment();
            } else if (tag.equals(GlobalConstants.TAB_WALLET_BAR)) {
                ft.add(R.id.wallet_container, fragment);
                activeWalletFragment();
            }
            ft.commitAllowingStateLoss();
        }

    }

    /*********************************************************************************
     * Function Name - popFragments
     * <p/>
     * Description - this function is used to remove the top fragment of a
     * specific tab on back press
     ********************************************************************************/
    private void popFragments() {
        /*
         * // * Select the last fragment in current tab's stack.. which will be
		 * shown after the fragment transaction given below
		 */
        Fragment fragment = mStacks.get(mCurrentTab).elementAt(
                mStacks.get(mCurrentTab).size() - 1);

        // Fragment fragment = getLastElement(mStacks.get(mCurrentTab));
        /* pop current fragment from stack.. */
        mStacks.get(mCurrentTab).remove(fragment);
        if (mStacks != null && mStacks.get(mCurrentTab) != null && !mStacks.get(mCurrentTab).isEmpty())
            currentFragment = mStacks.get(mCurrentTab).lastElement();
           /*
         * Remove the top fragment
		 */
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        // ft.add(R.id.realtabcontent, fragment);
        ft.detach(fragment);
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        AppUtils.showLog(TAG, " ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed() : " + ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed());
        if (mStacks.get(mCurrentTab).size() > 0 &&
                ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed() == false) {
            AppUtils.showErrorLog(TAG, "onBackPressed");
            /*
             * top fragment in current tab doesn't handles back press, we can do
			 * our thing, which is
			 *
			 * if current tab has only one fragment in stack, ie first fragment
			 * is showing for this tab. finish the activity else pop to previous
			 * fragment in stack for the same tab
			 */
            if (mStacks.get(mCurrentTab).size() == 1) {
                AppUtils.showLog(TAG, "mStacks.get(mCurrentTab).size() == 1");
                super.onBackPressed();
                finish();
            } else {
                AppUtils.showLog(TAG,
                        "mStacks.get(" + mCurrentTab + ").size() not equal to 1 : "
                                + mStacks.get(mCurrentTab).size());
                popFragments();
                if (mStacks.get(mCurrentTab).hashCode() != 0) {
                    // refresh screens
                    if (mStacks.get(mCurrentTab).size() > 0 && mStacks.get(mCurrentTab).lastElement() instanceof HomeFragment) {
                        AppUtils.showLog(TAG, " Current Fragment is Home Fragment");
                        //  refreshHomeFragment();
                    }
                    if (mStacks.get(mCurrentTab).size() > 0 && mStacks.get(mCurrentTab).lastElement() instanceof UserSettingFragment) {
                        AppUtils.showLog(TAG, " Current Fragment is Home Fragment");
                        //  refreshProfileFragment();
                    }
                    refreshFragments();
                }
            }
        } else {
            // do nothing.. fragment already handled back button press.

/*
            if (mStacks.get(mCurrentTab).size() > 0 && (mStacks.get(mCurrentTab).lastElement() instanceof HealthFragment ||
                    mStacks.get(mCurrentTab).lastElement() instanceof NetworksFragment ||
                    mStacks.get(mCurrentTab).lastElement() instanceof DevicesFragment ||
                    mStacks.get(mCurrentTab).lastElement() instanceof NotificationsFragment)) {
                AppUtils.showLog(TAG, " Main Dashboards Screens displayed");
            }
*/

        }
    }

    /*************************************************************
     * Function Name : refreshHomeFragment
     * Description : This function will refresh the home list screen.
     */
    private void refreshHomeFragment() {
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).onResume();
        }
    }

    private void refreshProfileFragment() {
        if (currentFragment instanceof UserSettingFragment) {
            ((UserSettingFragment) currentFragment).onResume();
        }
    }

    private void refreshMessageFragment() {
        if (currentFragment instanceof Fragment_Chat) {
            ((Fragment_Chat) currentFragment).onResume();
        }
    }

    /*************************************************************
     * Function Name : refreshFragment
     * Description : This function will refresh the current fragment.
     */
    private void refreshFragments() {
        if (currentFragment instanceof SubCategoryFragment) {
            ((SubCategoryFragment) currentFragment).onResume();
        }
        if (currentFragment instanceof ServiceTypeFragment) {
            ((ServiceTypeFragment) currentFragment).onResume();
        }if (currentFragment instanceof VendorListFragment) {
            ((VendorListFragment) currentFragment).onResume();
        }
     /*   if (currentFragment instanceof APInfo) {
            ((APInfo) currentFragment).onResume();
        }
        if (currentFragment instanceof NASInfo) {
            ((NASInfo) currentFragment).onResume();
        }*/

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_home:
                if (mStacks.get(GlobalConstants.TAB_HOME_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof HomeFragment))
                        refreshHomeFragment();
                    AppUtils.showErrorLog(TAG, "Home clicked");
                    activeHomeFragment();
                } else
                    pushFragments(GlobalConstants.TAB_HOME_BAR, new HomeFragment(), true);

                break;

            case R.id.rl_alert:
                if (mStacks.get(GlobalConstants.TAB_ALERT_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof AlertFragment))
                        AppUtils.showErrorLog(TAG, "Alert clicked");
                    activeAlertFragment();
                } else
                    pushFragments(GlobalConstants.TAB_ALERT_BAR, new AlertFragment(), true);

                break;

            case R.id.rl_wallet:
                if (mStacks.get(GlobalConstants.TAB_WALLET_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof WalletFragment))
                        AppUtils.showErrorLog(TAG, "WalletFragment clicked");
                    activeWalletFragment();
                } else
                    pushFragments(GlobalConstants.TAB_WALLET_BAR, new WalletFragment(), true);

                break;

            case R.id.rl_profile:
                if (mStacks.get(GlobalConstants.TAB_PROFILE_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof UserSettingFragment))
                        refreshProfileFragment();
                    activeProfileFragment();
                } else
                    pushFragments(GlobalConstants.TAB_PROFILE_BAR, new UserSettingFragment(), true);

                break;
            case R.id.rl_message:
                if (mStacks.get(GlobalConstants.TAB_MESSAGE_BAR).size() > 0) {
                    if (!(mStacks.get(mCurrentTab).lastElement() instanceof Fragment_Chat))
                        activeMessageFragment();
                    refreshMessageFragment();
                } else
                    pushFragments(GlobalConstants.TAB_MESSAGE_BAR, new Fragment_Chat(), true);

                break;

            case R.id.rlLoginSignup:
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
