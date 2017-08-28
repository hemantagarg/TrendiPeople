package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.app.trendipeople.adapter.AdapterAlertList;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.NotificationModel;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class AlertFragment extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    public static AlertFragment alertFragment;
    private Activity mActivity;
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<NotificationModel> arrayList;
    NotificationModel notificationModel;
    private AdapterAlertList adapterAlertList;
    private ImageView image_user;
    private TextView text_name;
    private final String TAG = AlertFragment.class.getSimpleName();
    private String Categoryid = "", categoryImage = "";

    public static AlertFragment getInstance() {
        if (alertFragment == null)
            alertFragment = new AlertFragment();
        return alertFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alert, container, false);
        mActivity = getActivity();
        alertFragment = this;
        initViews();
        getBundle();
        getNotificationList();
        return view;
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_services);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        manageHeaderView();
        image_user = (ImageView) view.findViewById(R.id.image_user);
        text_name = (TextView) view.findViewById(R.id.text_name);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNotificationList();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserDashboard.currentFragment instanceof AlertFragment) {

        }
    }

    private void refreshNotificationList() {

        // http://dev.stackmindz.com/trendi/api/notification-list.php?user_id=201
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.NOTIFICATION_LIST + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&user_role=" + AppUtils.getUserRole(mActivity);
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private void getNotificationList() {

        // http://onlineworkpro.com/trendi/api/subcategory.php?type=1&cat_id=3
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.NOTIFICATION_LIST + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&user_role=" + AppUtils.getUserRole(mActivity);
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
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.text_alert));
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
                    JSONArray array = data.getJSONArray("notifications");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        notificationModel = new NotificationModel();

                        notificationModel.setNotificationId(jo.getString("NotificationId"));
                        notificationModel.setSenderId(jo.getString("SenderId"));
                        notificationModel.setServiceId(jo.getString("ServiceId"));
                        notificationModel.setSenderName(jo.getString("SenderName"));
                        notificationModel.setReceiverId(jo.getString("ReceiverId"));
                        notificationModel.setReceiverName(jo.getString("ReceiverName"));
                        notificationModel.setCreateDate(jo.getString("CreateDate"));
                        notificationModel.setServiceName(jo.getString("ServiceName"));
                        notificationModel.setMessage(jo.getString("Message"));
                        notificationModel.setRowType(1);

                        arrayList.add(notificationModel);

                    }
                    adapterAlertList = new AdapterAlertList(mActivity, this, arrayList);
                    recyclerView.setAdapter(adapterAlertList);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    if (getUserVisibleHint()) {
                        Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    }
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

    }
}
