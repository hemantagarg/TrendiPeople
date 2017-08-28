package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.ActivityChat;
import com.app.trendipeople.adapter.AdapterChats;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.ConnectionDetector;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelChat;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Chat extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    RecyclerView list_request;
    Bundle b;
    Activity context;
    AdapterChats adapterChats;
    public static Fragment_Chat fragment_chat;
    ModelChat modelChat;
    ArrayList<ModelChat> arrayList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ConnectionDetector cd;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    private boolean loading = true;
    String maxlistLength = "";
    View view;

    public static Fragment_Chat getInstance() {
        if (fragment_chat == null)
            fragment_chat = new Fragment_Chat();
        return fragment_chat;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        view = inflater.inflate(R.layout.fragment_chat, container, false);
        fragment_chat = this;
        context = getActivity();
        arrayList = new ArrayList<>();
        b = getArguments();
        manageHeaderView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
            getServicelist();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout1);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        list_request = (RecyclerView) view.findViewById(R.id.list_request);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_request.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        setlistener();
       /* modelChat = new ModelChat();
        modelChat.setRowType(1);
        arrayList.add(modelChat);
        arrayList.add(modelChat);
        arrayList.add(modelChat);
        arrayList.add(modelChat);
        adapterChats=new AdapterChats(context,this,arrayList);
        list_request.setAdapter(adapterChats);
*/
        if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
            getServicelist();
        }
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, context.getResources().getString(R.string.text_message));
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
                context.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }


    private void setlistener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServicelistRefresh();
            }
        });
    }

    @Override
    public void onItemClickListener(int position, int flag) {

        Intent in = new Intent(context, ActivityChat.class);
        if (arrayList.get(position).getUserId().equalsIgnoreCase(AppUtils.getUserId(context))) {
            in.putExtra("reciever_id", arrayList.get(position).getSenderID());
        } else {
            in.putExtra("reciever_id", arrayList.get(position).getUserId());
        }
        in.putExtra("name", arrayList.get(position).getSenderName());
        in.putExtra("image", arrayList.get(position).getReceiverImage());
        in.putExtra("conver_id", arrayList.get(position).getSearchId());
        startActivity(in);
    }

    private void getServicelist() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                //     http://dev.stackmindz.com/trendi/api/getChatList.php?user_id=201&user_role=2

                String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_CHAT_LIST + "user_id=" + AppUtils.getUserId(context) + "&user_role="
                        + AppUtils.getUserRole(context);
                new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getServicelistRefresh() {

        try {
            skipCount = 0;
            if (AppUtils.isNetworkAvailable(context)) {

                String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_CHAT_LIST + "user_id=" + AppUtils.getUserId(context) + "&user_role="
                        + AppUtils.getUserRole(context);
                new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPostSuccess(int position, JSONObject jObject) {
        try {

            if (position == 1) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject object = commandResult.getJSONObject("data");
                    JSONArray data = object.getJSONArray("list");
                    arrayList.clear();
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);
                        modelChat = new ModelChat();

                        modelChat.setUserId(jo.getString("ReceiverId"));
                        modelChat.setSenderID(jo.getString("SenderId"));
                        modelChat.setUsername(jo.getString("ReceiverName"));
                        modelChat.setMessage(jo.getString("LastMessage"));
                        modelChat.setSearchId(jo.getString("conver_id"));
                        modelChat.setSenderName(jo.getString("SenderName"));
                        modelChat.setReceiverImage(jo.getString("SenderImage"));
                        modelChat.setRowType(1);
                        modelChat.setDate(jo.getString("LastMessageDateTime"));
                        modelChat.setUserImage(jo.getString("ReceiverImage"));
                        arrayList.add(modelChat);
                    }
                    adapterChats = new AdapterChats(getActivity(), this, arrayList);
                    list_request.setAdapter(adapterChats);

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                } else {

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }
            } else if (position == 4) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    maxlistLength = commandResult.getString("total");
                    JSONArray data = commandResult.getJSONArray("data");

                    arrayList.remove(arrayList.size() - 1);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jo = data.getJSONObject(i);

                        modelChat = new ModelChat();
                        modelChat.setUserId(jo.getString("receiverID"));
                        modelChat.setSenderID(jo.getString("senderID"));
                        modelChat.setUsername(jo.getString("receiverName"));
                        modelChat.setMessage(jo.getString("message"));
                        modelChat.setSearchId(jo.getString("searchID"));
                        modelChat.setSenderName(jo.getString("senderName"));
                        modelChat.setUnreadCount(jo.getString("unreadCount"));
                        modelChat.setIs_read(jo.getString("is_read"));
                        modelChat.setReceiverImage(jo.getString("receiverImage"));
                        modelChat.setRequestId(jo.getString("requestID"));
                        modelChat.setRowType(1);
                        modelChat.setDate(jo.getString("message_date"));
                        //   modelChat.setUserImage(getResources().getString(R.string.img_url) + jo.getString("userImage"));
                        arrayList.add(modelChat);
                    }
                    adapterChats.notifyDataSetChanged();
                    loading = true;
                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                } else {

                    adapterChats.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {
        if (context != null && isAdded())
            Toast.makeText(getActivity(), getResources().getString(R.string.problem_server), Toast.LENGTH_SHORT).show();
    }
}

