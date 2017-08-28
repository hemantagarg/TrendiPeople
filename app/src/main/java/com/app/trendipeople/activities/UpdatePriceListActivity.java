package com.app.trendipeople.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.adapter.AdapterUpdateServieList;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdatePriceListActivity extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {

    private RecyclerView recycler_view;
    private Activity mActivity;
    private Toolbar toolbar;
    private TextView text_continue;
    private ArrayList<ModelCategory> arrayList = new ArrayList<>();
    private AdapterUpdateServieList adapterUpdateServieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_price_list);
        mActivity = this;
        init();
        setListener();
        getServiceList();

    }

    private void getServiceList() {
        // http://dev.stackmindz.com/trendi/api/userservice.php?user_id=200
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.USER_SRVICE + "user_id=" + AppUtils.getUserId(mActivity);
            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    private void setData() {

        arrayList = new ArrayList<>();

        String data = AppUtils.getService(mActivity);
        Log.e("array", "**" + data);
        try {
            JSONArray services = new JSONArray(data);
            for (int i = 0; i < services.length(); i++) {

                JSONObject jo = services.getJSONObject(i);
                ModelCategory modelCategory = new ModelCategory();

                modelCategory.setCategoryId(jo.getString("ServiceId"));
                modelCategory.setGenderType(jo.getString("GenderType"));
                modelCategory.setSelected(false);
                modelCategory.setCategoryName(jo.getString("CategoryName"));
                modelCategory.setServiceName(jo.getString("ServiceName"));
                modelCategory.setSubCategoryName(jo.getString("SubCategoryName"));

                arrayList.add(modelCategory);
            }
            adapterUpdateServieList = new AdapterUpdateServieList(mActivity, this, arrayList);
            recycler_view.setAdapter(adapterUpdateServieList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.size() > 0) {
                    updateServicePrice();
                }
            }
        });
    }

    private void updateServicePrice() {

        //  http:
        //dev.stackmindz.com/trendi/api/save-service-price.php?
        // user_id=210&service_price={%22Services%22:[{%22ServiceId%22:%222%22,%22Price%22:%2245%22},{%22ServiceId%22:%223%22,%22Price%22:%2240%22}]}
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray main = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {

                JSONObject jo = new JSONObject();
                jo.put("ServiceId", arrayList.get(i).getCategoryId());
                jo.put("Price", arrayList.get(i).getServicePrice());
                main.put(jo);
            }
            jsonObject.put("Services", main);
            Log.e("jsonArray", "**" + main);

            if (AppUtils.isNetworkAvailable(mActivity)) {

                String url = JsonApiHelper.BASEURL + JsonApiHelper.SAVE_SERVICE_PRICE + "user_id=" + AppUtils.getUserId(mActivity)
                        + "&service_price=" + jsonObject.toString();
                new CommonAsyncTaskHashmap(2, mActivity, this).getquery(url);

            } else {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Price");
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        text_continue = (TextView) findViewById(R.id.text_continue);

    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {


        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    arrayList = new ArrayList<>();

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray services = data.getJSONArray("Services");
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);
                        ModelCategory modelCategory = new ModelCategory();

                        modelCategory.setCategoryId(jo.getString("ServiceId"));
                        modelCategory.setGenderType(jo.getString("GenderType"));
                        modelCategory.setServicePrice(jo.getString("Price"));
                        modelCategory.setCategoryName(jo.getString("CategoryName"));
                        modelCategory.setServiceName(jo.getString("ServiceName"));
                        modelCategory.setSubCategoryName(jo.getString("SubCategoryName"));

                        arrayList.add(modelCategory);
                    }
                    adapterUpdateServieList = new AdapterUpdateServieList(mActivity, this, arrayList);
                    recycler_view.setAdapter(adapterUpdateServieList);
                }
            } else if (method == 2) {

                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
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
