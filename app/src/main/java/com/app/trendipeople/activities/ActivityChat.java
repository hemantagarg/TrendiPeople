package com.app.trendipeople.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.adapter.AdapterChatDetail;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelChat;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;


public class ActivityChat extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {

    private Context mActivity;
    private RecyclerView chatList;
    private AdapterChatDetail adapterChatDetail;
    private ArrayList<ModelChat> chatListData;
    private EditText edtMessage;
    private Toolbar toolbar;
    private TextView username;
    private String reciever_id = "", conver_id = "";
    private ImageView imgSendMessage, image_user;
    private LinearLayoutManager layoutManager;
    private boolean loading = true, isActivityVisible = true;
    private String maxlistLength = "", serviceId = "";
    private ModelChat chatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_detail);
        mActivity = this;
        init();
        setListener();
        AppUtils.setIsChatVisible(mActivity, true);
        adapterChatDetail = new AdapterChatDetail(mActivity, this, chatListData);
        chatList.setAdapter(adapterChatDetail);
        SyncDataToServer();

    }

    private void SyncDataToServer() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_MESSAGE_LIST + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&user_role=" + AppUtils.getUserRole(mActivity) + "&conver_id=" + conver_id;
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {
        edtMessage = (EditText) findViewById(R.id.edit_message);
        imgSendMessage = (ImageView) findViewById(R.id.send_message);
        image_user = (ImageView) findViewById(R.id.image_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        chatList = (RecyclerView) findViewById(R.id.list_request);
        //==============================================
        username = (TextView) findViewById(R.id.username);
        chatListData = new ArrayList<>();
        Intent in = getIntent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //  getSupportActionBar().setTitle(in.getExtras().getString("name"));
        reciever_id = in.getExtras().getString("reciever_id");
        AppUtils.setChatUserId(mActivity, reciever_id);

        if (in.hasExtra("serviceId")) {
            serviceId = in.getStringExtra("serviceId");
        }
        conver_id = in.getExtras().getString("conver_id");
        //=============================
        username.setText(in.getExtras().getString("name"));

        if (!in.getExtras().getString("image").equalsIgnoreCase("")) {
            Picasso.with(mActivity)
                    .load(in.getExtras().getString("image"))
                    .placeholder(R.drawable.user)
                    .transform(new CircleTransform())
                    .into(image_user);
        }

        layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        chatList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edtMessage.getText().toString();
                if (!edtMessage.getText().toString().equalsIgnoreCase("")) {
                    try {
                        Calendar calander = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        Date date = calander.getTime();
                        String ISO_FORMAT = "dd-MM-yyyy HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
                        long time = System.currentTimeMillis();
                        ModelChat chatData = new ModelChat();
                        chatData.setSender_id(AppUtils.getUserId(mActivity));
                        chatData.setMessage(msg);
                        chatData.setSender_name(AppUtils.getUserName(mActivity));
                        chatData.setDate_time(sdf.format(date));
                        chatListData.add(chatData);
                        chatList.setAdapter(adapterChatDetail);

                        SendDataToServer();
                        edtMessage.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void SendDataToServer() {
        if (AppUtils.isNetworkAvailable(mActivity)) {
            //  dev.stackmindz.com/trendi/api/message.php?user_id=200&user_id_1=201&user_role=3&subject=test&service_id=2&msg=Hii&conver_id=

            String url = JsonApiHelper.BASEURL + JsonApiHelper.SEND_MESSAGE + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&user_id_1=" + reciever_id + "&user_role=" + AppUtils.getUserRole(mActivity) + "&conver_id=" + conver_id +
                    "&service_id=" + serviceId + "&msg=" + edtMessage.getText().toString();
            new CommonAsyncTaskHashmap(2, mActivity, this).getqueryNoProgress(url);
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
            if (response != null) {
                if (method == 1) {

                    JSONObject commandResult = response.getJSONObject("commandResult");

                    if (commandResult.getString("success").equalsIgnoreCase("1")) {

                        JSONObject object = commandResult.getJSONObject("data");
                        JSONArray messageList = object.getJSONArray("message");
                        chatListData.clear();

                        for (int i = chatListData.size(); i < messageList.length(); i++) {
                            JSONObject chat = messageList.getJSONObject(i);
                            ModelChat chatData = new ModelChat();
                            chatData.setSender_id(chat.getString("UserId"));
                            chatData.setRowType(1);
                            //     chatData.setReciever_id(chat.getString("receiverID"));
                            chatData.setMessage(chat.getString("Message"));
                            chatData.setSender_name(chat.getString("Name"));
                            //  chatData.setReceiverName(chat.getString("receiverName"));
                            chatData.setDate_time(chat.getString("Createdate"));
                            chatListData.add(chatData);
                        }

                        Collections.reverse(chatListData);
                        adapterChatDetail.notifyDataSetChanged();
                        chatList.smoothScrollToPosition(chatListData.size() - 1);
                    }
                    if (isActivityVisible) {
                        syncData();
                    }

                } else if (method == 2) {


                } else {
                    if (isActivityVisible) {
                        syncData();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (isActivityVisible) {
                syncData();
            }

        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }


    private void syncData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SyncDataToServer();

            }
        }, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityVisible = false;
        AppUtils.setIsChatVisible(mActivity, false);
    }
}
