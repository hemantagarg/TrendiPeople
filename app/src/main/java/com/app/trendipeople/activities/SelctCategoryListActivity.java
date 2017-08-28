package com.app.trendipeople.activities;

import android.content.Context;
import android.content.Intent;
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
import com.app.trendipeople.adapter.AdapterSelectCategoryList;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelctCategoryListActivity extends AppCompatActivity implements OnCustomItemClicListener {

    private RecyclerView recycler_view;
    private Context context;

    private Toolbar toolbar;
    private TextView text_continue;
    private ArrayList<ModelCategory> arrayList;
    private AdapterSelectCategoryList adapterUniServiceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category_list);
        context = this;
        init();
        setListener();
        setData();

    }

    private void setData() {

        arrayList = new ArrayList<>();

        String data = AppUtils.getService(context);
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
            adapterUniServiceList = new AdapterSelectCategoryList(context, this, arrayList);
            recycler_view.setAdapter(adapterUniServiceList);

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
                try {
                    String selectedservices = "";
                    String selectedservicesName = "";

                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).isSelected()) {
                            Log.e("selectedservices", "*" + selectedservices);
                            if (selectedservices.equalsIgnoreCase("")) {
                                selectedservices = arrayList.get(i).getCategoryId();
                                selectedservicesName = arrayList.get(i).getServiceName();
                            } else {
                                selectedservices = selectedservices + "," + arrayList.get(i).getCategoryId();
                                selectedservicesName = selectedservicesName + "," + arrayList.get(i).getServiceName();
                            }
                        }
                    }
                    if (!selectedservices.equalsIgnoreCase("")) {

                        Intent intent = new Intent();
                        intent.putExtra("serviceId", selectedservices);
                        intent.putExtra("servicename", selectedservicesName);
                        setResult(52, intent);
                        finish();
                    } else {
                        Toast.makeText(context, "Please select Category", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Category");
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        text_continue = (TextView) findViewById(R.id.text_continue);

    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            if (arrayList.get(position).isSelected()) {
                arrayList.get(position).setSelected(false);
            } else {
                arrayList.get(position).setSelected(true);
            }
            adapterUniServiceList.notifyDataSetChanged();
        }
    }
}
