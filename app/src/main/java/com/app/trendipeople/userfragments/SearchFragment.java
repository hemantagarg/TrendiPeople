package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.adapter.AdapterSearch;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchFragment extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    public static SearchFragment serviceTypeFragment;
    private Activity mActivity;
    private View view;
    private EditText edtSearch;
    private ImageView image_search;
    private RecyclerView recyclerView;
    private ArrayList<ModelCategory> arrayList;
    ModelCategory modelCategory;
    private AdapterSearch adapterSearch;
    private final String TAG = SearchFragment.class.getSimpleName();
    private String categoryId = "", subCategoryId = "";

    public static SearchFragment getInstance() {
        if (serviceTypeFragment == null)
            serviceTypeFragment = new SearchFragment();
        return serviceTypeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.search_fragment, container, false);
        mActivity = getActivity();
        serviceTypeFragment = this;
        initViews();
        return view;
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        image_search = (ImageView) view.findViewById(R.id.image_search);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        manageHeaderView();

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtSearch.getText().toString().equalsIgnoreCase("")) {
                    searchService();
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserDashboard.currentFragment instanceof SearchFragment) {

        }
    }

    private void searchService() {

        if (AppUtils.isNetworkAvailable(mActivity)) {
            // http://dev.stackmindz.com/trendi/api/searchService.php?search_data=nail
            String url = JsonApiHelper.BASEURL + JsonApiHelper.SEARCH_SERVICE + "search_data=" + edtSearch.getText().toString();
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
        HeaderViewManager.getInstance().setHeading(true,"Search Service");
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
                    JSONArray array = data.getJSONArray("Search");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        modelCategory = new ModelCategory();
                        modelCategory.setServiceId(jo.getString("ServiceId"));
                        modelCategory.setServiceName(jo.getString("ServiceName"));
                        modelCategory.setServiceName(jo.getString("Vendor_Id"));
                        modelCategory.setVendor_Id(jo.getString("Vendor_name"));
                        modelCategory.setComment(jo.getString("Comment"));
                        modelCategory.setServiceImage(jo.getString("ServiceImage"));
                        modelCategory.setRowType(1);

                        arrayList.add(modelCategory);
                    }
                    adapterSearch = new AdapterSearch(mActivity, this, arrayList);
                    recyclerView.setAdapter(adapterSearch);
                } else {
                    Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_SHORT).show();
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
