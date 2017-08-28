package com.app.trendipeople.userfragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trendipeople.R;
import com.app.trendipeople.adapter.AdapterFreelancerReviews;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragementFreelancerReviews extends Fragment implements OnCustomItemClicListener {

    private RecyclerView recycler_service;
    private ArrayList<ModelCategory> imagelist;
    private AdapterFreelancerReviews adapterFreelancerReviews;
    private Context context;
    private Bundle b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragement_freelancer_services, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        imagelist = new ArrayList<>();
        recycler_service = (RecyclerView) view.findViewById(R.id.recycler_services);
        recycler_service.setLayoutManager(new GridLayoutManager(context,1));
        b = getArguments();
        setData();
    }

    private void setData() {
        if (b != null) {
            String data = b.getString("reviews");

            try {
                JSONArray images = new JSONArray(data);

                for (int i = 0; i < images.length(); i++) {

                    JSONObject jo = images.getJSONObject(i);

                    ModelCategory serviceDetail = new ModelCategory();
                    serviceDetail.setServiceName(jo.getString("ServiceName"));
                    serviceDetail.setUserImage(jo.getString("Image"));
                    serviceDetail.setUsername(jo.getString("UserName"));
                    serviceDetail.setFreelancer_rating(jo.getString("Rating"));
                    serviceDetail.setReview(jo.getString("Review"));

                    imagelist.add(serviceDetail);
                }
                adapterFreelancerReviews = new AdapterFreelancerReviews(context, FragementFreelancerReviews.this, imagelist);
                recycler_service.setAdapter(adapterFreelancerReviews);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }
}
