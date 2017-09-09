package com.app.trendipeople.userfragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.LoginActivity;
import com.app.trendipeople.adapter.AdapterFreelancerPersonalServices;

import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragementFreelancerServices extends Fragment implements OnCustomItemClicListener {

    private RecyclerView recycler_service;
    private ArrayList<ModelCategory> imagelist;
    private AdapterFreelancerPersonalServices adapterFreelancerPersonalServices;
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
            String data = b.getString("services");

            try {

                JSONArray images = new JSONArray(data);

                for (int i = 0; i < images.length(); i++) {

                    JSONObject jo = images.getJSONObject(i);

                    ModelCategory serviceDetail = new ModelCategory();
                    serviceDetail.setServiceName(jo.getString("ServiceName"));
                    serviceDetail.setServiceImage(jo.getString("ServiceImage"));
                    serviceDetail.setServicePrice(jo.getString("ServicePrice"));
                    // serviceDetail.setImageId(jo.getString("ImageId"));
                    imagelist.add(serviceDetail);
                }
                adapterFreelancerPersonalServices = new AdapterFreelancerPersonalServices(context, FragementFreelancerServices.this, imagelist);
                recycler_service.setAdapter(adapterFreelancerPersonalServices);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClickListener(int position, int flag) {
      if (flag == 11) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
//            ModelData modelData = listServices.get(position);
//            String text = "Service Request : " + "\nUsername : " + modelData.getFull_name() + "\nEmail : " + modelData.getEmail()
//                    + "\nMobile no : " + modelData.getMobile() + "\nAddress : " + modelData.getAddress() + "\nProduct Name : " + modelData.getProduct()
//                    + "\nCompany name : " + modelData.getCompany_name() + "\nPurchase Date : " + modelData.getPurchase_date()
//                    + "\nComment : " + modelData.getComment()
//                    + "\nCurrent Location : " + modelData.getAutolocation();

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Test");
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
       /* if (flag == 1) {
            VendorProfile subCategoryFragment = new VendorProfile();
            Bundle b = new Bundle();
            b.putString(AppConstant.VENDORID, arrayList.get(position).getFreelancer_id());

            subCategoryFragment.setArguments(b);
            setFragment(subCategoryFragment);
        } else if (flag == 2) {

            if (AppUtils.getUserId(mActivity).equalsIgnoreCase("")) {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
            } else {
                showOrderConfirmation(arrayList.get(position).getFreelancer_id());

            }
        }*/
    }
}
