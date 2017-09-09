package com.app.trendipeople.vendorfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.adapter.AdapterFreelancerOngoingBookings;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragementVendorOngoingBooking extends Fragment implements OnCustomItemClicListener, ApiResponse {

    private RecyclerView recycler_service;
    private ArrayList<ModelCategory> imagelist;
    private AdapterFreelancerOngoingBookings adapterFreelancerOngoingBookings;
    private Activity mActivity;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.freelancer_ongoing_booking, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();
        imagelist = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        recycler_service = (RecyclerView) view.findViewById(R.id.recycler_services);
        recycler_service.setLayoutManager(new LinearLayoutManager(mActivity));
        getData();
        setListener();
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });

    }

    private void refreshData() {
        http:
//dev.stackmindz.com/trendi/api/mybooking.php?user_id=200&user_role=3

        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.MYBOOKING + "user_id=" + AppUtils.getUserId(mActivity) + "&user_role=" + AppUtils.getUserRole(mActivity);
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    private void getData() {
        //  http://dev.stackmindz.com/trendi/api/mybooking.php?user_id=200&user_role=3


        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.MYBOOKING + "user_id=" + AppUtils.getUserId(mActivity) + "&user_role=" + AppUtils.getUserRole(mActivity);
            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("Booking");
                    imagelist.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);

                        ModelCategory serviceDetail = new ModelCategory();
                        serviceDetail.setServiceName(jo.getString("ServiceName"));
                        serviceDetail.setServiceImage(jo.getString("ServiceImage"));
                        serviceDetail.setOrderId(jo.getString("OrderId"));
                        serviceDetail.setServiceDate(jo.getString("ServiceDate"));
                        serviceDetail.setServiceTime(jo.getString("ServiceTime"));
                        serviceDetail.setUsername(jo.getString("Username"));
                        serviceDetail.setUsermobile(jo.getString("Usermobile"));
                        serviceDetail.setUserEmail(jo.getString("UserEmail"));
                        serviceDetail.setUserImage(jo.getString("UserImage"));

                        imagelist.add(serviceDetail);
                    }
                    adapterFreelancerOngoingBookings = new AdapterFreelancerOngoingBookings(mActivity, FragementVendorOngoingBooking.this, imagelist);
                    recycler_service.setAdapter(adapterFreelancerOngoingBookings);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

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
