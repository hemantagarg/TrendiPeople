package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.adapter.AdapterServiceTypeList;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.utils.CircleTransform;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ServiceTypeFragment extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    public static ServiceTypeFragment serviceTypeFragment;
    private Activity mActivity;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<ModelCategory> arrayList;
    ModelCategory modelCategory;
    private AdapterServiceTypeList adapterServiceTypeList;
    private ImageView image_user;
    private TextView text_name, text_subCategoryName;
    private final String TAG = ServiceTypeFragment.class.getSimpleName();
    private String categoryId = "", subCategoryId = "";

    public static ServiceTypeFragment getInstance() {
        if (serviceTypeFragment == null)
            serviceTypeFragment = new ServiceTypeFragment();
        return serviceTypeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.servcetype_fragment, container, false);
        mActivity = getActivity();
        serviceTypeFragment = this;
        initViews();
        getBundle();
        getCategoryList();
        return view;
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        manageHeaderView();
        image_user = (ImageView) view.findViewById(R.id.image_user);
        text_name = (TextView) view.findViewById(R.id.text_name);
        text_subCategoryName = (TextView) view.findViewById(R.id.text_subCategoryName);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserDashboard.currentFragment instanceof ServiceTypeFragment) {

        }
    }

    private void getCategoryList() {

        // http://onlineworkpro.com/trendi/api/service.php?type=1&cat_id=3&subcat_id=1
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.SERVICE + "type=" + AppUtils.getGender(mActivity) + "&cat_id=" + categoryId
                    + "&subcat_id=" + subCategoryId;
            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.trendi_people));
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

    private void getBundle() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            text_name.setText(bundle.getString(AppConstant.CATEGORYNAME));
            text_subCategoryName.setText(bundle.getString(AppConstant.SUBCATEGORYNAME));
            Picasso.with(mActivity).load(bundle.getString(AppConstant.CATEGORYIMAGE)).transform(new CircleTransform()).into(image_user);
            categoryId = bundle.getString(AppConstant.CATEGORYID);
            subCategoryId = bundle.getString(AppConstant.SUBCATEGORYID);

        }
    }


    protected void setFragment(Fragment fragment) {
        UserDashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, fragment, true);
    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("services");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        modelCategory = new ModelCategory();
                        modelCategory.setServiceId(jo.getString("ServiceId"));
                        modelCategory.setServiceName(jo.getString("ServiceName"));
                        modelCategory.setServiceImage(jo.getString("ServiceImage"));
                        modelCategory.setRowType(1);

                        arrayList.add(modelCategory);

                    }
                    adapterServiceTypeList = new AdapterServiceTypeList(mActivity, this, arrayList);
                    recyclerView.setAdapter(adapterServiceTypeList);

                } else {
                    Toast.makeText(mActivity, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onPostFail(int method, String response) {
        Toast.makeText(mActivity, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 1) {

            VendorListFragment vendorListFragment = new VendorListFragment();
            Bundle b = new Bundle();
            b.putString(AppConstant.SERVICEID, arrayList.get(position).getServiceId());
            b.putString(AppConstant.CATEGORYNAME, arrayList.get(position).getServiceName());

            vendorListFragment.setArguments(b);
            setFragment(vendorListFragment);
        }

    }
}
