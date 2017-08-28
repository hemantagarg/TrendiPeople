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
import com.app.trendipeople.adapter.AdapterSubCategoryList;
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


public class SubCategoryFragment extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    public static SubCategoryFragment subCategoryFragment;
    private Activity mActivity;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<ModelCategory> arrayList;
    ModelCategory modelCategory;
    private AdapterSubCategoryList adapterSubCategoryList;
    private ImageView image_user;
    private TextView text_name;
    private final String TAG = SubCategoryFragment.class.getSimpleName();
    private String Categoryid = "", categoryImage = "";

    public static SubCategoryFragment getInstance() {
        if (subCategoryFragment == null)
            subCategoryFragment = new SubCategoryFragment();
        return subCategoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.subcategory_fragment, container, false);
        mActivity = getActivity();
        subCategoryFragment = this;
        initViews();
        getBundle();
        getCategoryList();
        return view;
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        manageHeaderView();
        image_user = (ImageView) view.findViewById(R.id.image_user);
        text_name = (TextView) view.findViewById(R.id.text_name);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserDashboard.currentFragment instanceof SubCategoryFragment) {

        }
    }

    private void getCategoryList() {

        // http://onlineworkpro.com/trendi/api/subcategory.php?type=1&cat_id=3
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.SUBCAEGORY + "type=" + AppUtils.getGender(mActivity) + "&cat_id=" + Categoryid;
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
            Categoryid = bundle.getString(AppConstant.CATEGORYID);
            text_name.setText(bundle.getString(AppConstant.CATEGORYNAME));
            categoryImage = bundle.getString(AppConstant.CATEGORYIMAGE);
            Picasso.with(mActivity).load(categoryImage).transform(new CircleTransform()).into(image_user);

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
                    JSONArray array = data.getJSONArray("subCategories");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        modelCategory = new ModelCategory();

                        modelCategory.setSubCategoryId(jo.getString("SubCategoryId"));

                        modelCategory.setSubCategoryName(jo.getString("SubCategoryName"));
                        modelCategory.setRowType(1);
                        arrayList.add(modelCategory);

                    }
                    adapterSubCategoryList = new AdapterSubCategoryList(mActivity, this, arrayList);
                    recyclerView.setAdapter(adapterSubCategoryList);

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
            ServiceTypeFragment serviceTypeFragment = new ServiceTypeFragment();
            Bundle b = new Bundle();
            b.putString(AppConstant.SUBCATEGORYID, arrayList.get(position).getSubCategoryId());
            b.putString(AppConstant.SUBCATEGORYNAME, arrayList.get(position).getSubCategoryName());
            b.putString(AppConstant.CATEGORYID, Categoryid);
            b.putString(AppConstant.CATEGORYNAME, text_name.getText().toString());
            b.putString(AppConstant.CATEGORYIMAGE, categoryImage);
            serviceTypeFragment.setArguments(b);
            setFragment(serviceTypeFragment);

        }

    }
}
