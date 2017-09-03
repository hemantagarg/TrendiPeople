package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.trendipeople.R.id.rl_logout;


public class VendorProfileFragment extends BaseFragment implements View.OnClickListener, ApiResponse {


    public static VendorProfileFragment vendorProfileFragment;
    private Activity mActivity;
    private View view;
    private final String TAG = VendorProfileFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private TextView text_username, text_address;
    private ImageView image_back, imge_user, imge_banner;
    private Button btn_booknow;
    private ViewPager viewPager;
    private ArrayList<ModelCategory> arrayList;
    private String vendorId = "";

    public static VendorProfileFragment getInstance() {
        if (vendorProfileFragment == null)
            vendorProfileFragment = new VendorProfileFragment();
        return vendorProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vendor_profile_fragment, container, false);
        mActivity = getActivity();
        vendorProfileFragment = this;
        initViews();
        getBundle();

        getData();
        return view;
    }

    private void getData() {

        //  http://dev.stackmindz.com/trendi/api/getvendordetail.php?freelancer_id=200

        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/trendi/api/change-password.php?user_id=199&current_pwd=admin&new_pwd=123456&confirm_pwd=123456
            String url = JsonApiHelper.BASEURL + JsonApiHelper.GETVENDORDETAIL + "freelancer_id=" + vendorId;

            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private void getBundle() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            vendorId = bundle.getString(AppConstant.VENDORID);

        }
    }


    private void initViews() {
        image_back = (ImageView) view.findViewById(R.id.image_back);
        imge_user = (ImageView) view.findViewById(R.id.imge_user);
        imge_banner = (ImageView) view.findViewById(R.id.imge_banner);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        text_username = (TextView) view.findViewById(R.id.text_username);
        text_address = (TextView) view.findViewById(R.id.text_address);
        btn_booknow = (Button) view.findViewById(R.id.btn_booknow);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("My Services");
        tabLayout.getTabAt(1).setText("Reviews");
        tabLayout.getTabAt(2).setText("About");
        tabLayout.getTabAt(3).setText("Gallery");

        tabLayout.setTabTextColors(getResources().getColor(R.color.textcolordark), getResources().getColor(R.color.text_blue));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        FragementFreelancerServices tab2 = new FragementFreelancerServices();
        Bundle b = new Bundle();
        b.putString("services", arrayList.get(0).getServies());
        tab2.setArguments(b);
        adapter.addFrag(tab2, "services");


        FragementFreelancerReviews tab4 = new FragementFreelancerReviews();
        Bundle b3 = new Bundle();
        b3.putString("reviews", arrayList.get(0).getRatingArray());
        tab4.setArguments(b3);
        adapter.addFrag(tab4, "Reviews");


        FragementAboutUs tab1 = new FragementAboutUs();
        Bundle b1 = new Bundle();
        b1.putString("business", arrayList.get(0).getBussinessDetailsArray());

        tab1.setArguments(b1);
        adapter.addFrag(tab1, "About Us");

        FragmentGallery tab3 = new FragmentGallery();
        Bundle b2 = new Bundle();
        b2.putString("gallery", arrayList.get(0).getPortfolioDetailsArray());
        tab3.setArguments(b2);
        adapter.addFrag(tab3, "Portfolio");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {
                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                arrayList = new ArrayList<>();
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    text_username.setText(data.getString("Name"));
                    text_address.setText(data.getString("Address"));

                    if (!data.getString("ProfilePic").equalsIgnoreCase("")) {
                        Picasso.with(mActivity).load(data.getString("ProfilePic")).transform(new CircleTransform()).into(imge_user);
                    }

                    if (!data.getString("Banner").equalsIgnoreCase("")) {
                        Picasso.with(mActivity).load(data.getString("Banner")).into(imge_banner);
                    }
                    ModelCategory detaildata = new ModelCategory();
                    detaildata.setServies(data.getJSONArray("Service").toString());
                    detaildata.setBussinessDetailsArray(data.getJSONObject("Business_profile").toString());
                    detaildata.setPortfolioDetailsArray(data.getJSONArray("Gallery").toString());
                    detaildata.setRatingArray(data.getJSONArray("Rating").toString());
                    // detaildata.setAbout(data.getString("About").toString());

                    arrayList.add(detaildata);
                    Log.e("arraylistasize", "**" + arrayList.size());

                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                    setupTabIcons();

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

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
            case rl_logout:


                break;


        }

    }

}
