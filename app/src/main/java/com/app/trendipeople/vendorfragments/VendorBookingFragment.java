package com.app.trendipeople.vendorfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.userfragments.BaseFragment;
import com.app.trendipeople.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import static com.app.trendipeople.R.id.rl_logout;


public class VendorBookingFragment extends BaseFragment implements View.OnClickListener {


    public static VendorBookingFragment vendorBookingFragment;
    private Activity mActivity;
    private View view;
    private final String TAG = VendorBookingFragment.class.getSimpleName();
    private TabLayout tabLayout;

    private ViewPager viewPager;

    public static VendorBookingFragment getInstance() {
        if (vendorBookingFragment == null)
            vendorBookingFragment = new VendorBookingFragment();
        return vendorBookingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vendor_booking_fragment, container, false);
        mActivity = getActivity();
        vendorBookingFragment = this;
        initViews();
        getBundle();
        manageHeaderView();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return view;
    }



    private void getBundle() {

        Bundle bundle = getArguments();
        if (bundle != null) {

      //      vendorId = bundle.getString(AppConstant.VENDORID);

        }
    }


    private void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);

      }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("Ongoing");
        tabLayout.getTabAt(1).setText("Completed");

        tabLayout.setTabTextColors(getResources().getColor(R.color.textcolordark), getResources().getColor(R.color.text_blue));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        FragementVendorOngoingBooking tab2 = new FragementVendorOngoingBooking();
        adapter.addFrag(tab2, "Ongoing");

        FragementVendorCompleteBooking tab1 = new FragementVendorCompleteBooking();
        adapter.addFrag(tab1, "Completed");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (UserDashboard.currentFragment instanceof VendorBookingFragment) {

        }
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.booking));
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
        UserDashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, fragment, true);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case rl_logout:


                break;


        }

    }

}
