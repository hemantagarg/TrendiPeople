package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.adapter.AdapterCategoryList;
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


public class HomeFragment extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    public static HomeFragment homeFargment;
    private Activity mActivity;
    private View view;
    private RecyclerView recyclerView;
    private EditText edtSearch;
    private ImageView image_search;
    private ArrayList<ModelCategory> arrayList;
    private ArrayList<ModelCategory> arrayListGender;
    private ModelCategory modelCategory;
    private TextView text_male, text_female, text_both;
    private AdapterCategoryList adapterCategoryList;
    private final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment getInstance() {
        if (homeFargment == null)
            homeFargment = new HomeFragment();
        return homeFargment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mActivity = getActivity();
        homeFargment = this;
        arrayList = new ArrayList<>();
        arrayListGender = new ArrayList<>();
        text_female = (TextView) view.findViewById(R.id.text_female);
        text_both = (TextView) view.findViewById(R.id.text_both);
        text_male = (TextView) view.findViewById(R.id.text_male);
        image_search = (ImageView) view.findViewById(R.id.image_search);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        manageHeaderView();
        setListener();
        if (AppUtils.getGender(mActivity).equalsIgnoreCase("")) {
            AppUtils.setGender(mActivity, AppConstant.MALE);
        } else {
            if (AppUtils.getGender(mActivity).equalsIgnoreCase(AppConstant.MALE)) {
                setGenderBg(1);
            } else if (AppUtils.getGender(mActivity).equalsIgnoreCase(AppConstant.BOTH)) {
                setGenderBg(3);
            } else {
                setGenderBg(2);
            }
        }
        getCategoryList();

        return view;
    }

    private void setGenderBg(int type) {
        if (type == 1) {
            text_male.setBackgroundDrawable(getResources().getDrawable(R.drawable.gentes_bg_blue));
            text_female.setBackgroundDrawable(getResources().getDrawable(R.drawable.ladies_bg_white));
            text_both.setBackgroundDrawable(getResources().getDrawable(R.drawable.both_bg_white));

            text_female.setTextColor(ContextCompat.getColor(mActivity, R.color.text_blue));
            text_male.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            text_both.setTextColor(ContextCompat.getColor(mActivity, R.color.text_blue));
        } else if (type == 2) {

            text_male.setBackgroundDrawable(getResources().getDrawable(R.drawable.gentes_bg_white));
            text_female.setBackgroundDrawable(getResources().getDrawable(R.drawable.ladies_bg_blue));
            text_both.setBackgroundDrawable(getResources().getDrawable(R.drawable.both_bg_white));

            text_female.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            text_male.setTextColor(ContextCompat.getColor(mActivity, R.color.text_blue));
            text_both.setTextColor(ContextCompat.getColor(mActivity, R.color.text_blue));
        } else if (type == 3) {

            text_male.setBackgroundDrawable(getResources().getDrawable(R.drawable.gentes_bg_white));
            text_female.setBackgroundDrawable(getResources().getDrawable(R.drawable.ladies_bg_white));
            text_both.setBackgroundDrawable(getResources().getDrawable(R.drawable.both_bg_blue));

            text_female.setTextColor(ContextCompat.getColor(mActivity, R.color.text_blue));
            text_male.setTextColor(ContextCompat.getColor(mActivity, R.color.text_blue));
            text_both.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        }
    }

    private void setListener() {


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        text_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setGenderBg(1);
                AppUtils.setGender(mActivity, AppConstant.MALE);
                setDataAccToGender(false);
            }
        });

        text_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setGenderBg(2);
                AppUtils.setGender(mActivity, AppConstant.FEMALE);
                setDataAccToGender(false);
            }
        });
        text_both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setGenderBg(3);
                AppUtils.setGender(mActivity, AppConstant.BOTH);
                setDataAccToGender(true);
            }
        });

    }

    void filter(String text) {
        ArrayList<ModelCategory> temp = new ArrayList();
        for (ModelCategory d : arrayListGender) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getCategoryName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        adapterCategoryList.updateList(temp);
    }

    private void setDataAccToGender(boolean isBoth) {
        arrayListGender.clear();
        if (isBoth) {
            for (int i = 0; i < arrayList.size(); i++) {
                arrayListGender.add(arrayList.get(i));
            }
        } else {
            for (int i = 0; i < arrayList.size(); i++) {

                if (AppUtils.getGender(mActivity).equalsIgnoreCase(arrayList.get(i).getGenderType())) {
                    arrayListGender.add(arrayList.get(i));
                }
            }
        }
        if (adapterCategoryList != null) {
            adapterCategoryList.updateList(arrayListGender);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserDashboard.currentFragment instanceof HomeFragment) {

        }
    }

    private void getCategoryList() {

        // http://onlineworkpro.com/trendi/api/category.php?type=1
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.CAEGORY;
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
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {

                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("categories");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        modelCategory = new ModelCategory();

                        modelCategory.setCategoryId(jo.getString("CategoryId"));
                        modelCategory.setGenderType(jo.getString("GenderType"));
                        modelCategory.setCategoryImage(jo.getString("CategoryImage"));
                        modelCategory.setCategoryName(jo.getString("CategoryName"));
                        modelCategory.setRowType(1);

                        arrayList.add(modelCategory);

                        if (AppUtils.getGender(mActivity).equalsIgnoreCase(modelCategory.getGenderType())) {
                            arrayListGender.add(modelCategory);
                        }
                    }
                    Log.e("arrayListGender", "***" + arrayListGender.size());
                    adapterCategoryList = new AdapterCategoryList(mActivity, this, arrayListGender);
                    recyclerView.setAdapter(adapterCategoryList);

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
            SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
            Bundle b = new Bundle();
            b.putString(AppConstant.CATEGORYID, arrayListGender.get(position).getCategoryId());
            b.putString(AppConstant.CATEGORYNAME, arrayListGender.get(position).getCategoryName());
            b.putString(AppConstant.CATEGORYIMAGE, arrayListGender.get(position).getCategoryImage());
            subCategoryFragment.setArguments(b);
            setFragment(subCategoryFragment);
        }

    }
}
