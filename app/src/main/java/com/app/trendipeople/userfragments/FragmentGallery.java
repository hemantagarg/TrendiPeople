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
import com.app.trendipeople.adapter.AdapterVendorPortfolio;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentGallery extends Fragment implements OnCustomItemClicListener {

    private RecyclerView recycler_portfolio;
    private ArrayList<ModelCategory> imagelist;
    private AdapterVendorPortfolio adapterVendorPortfolio;
    private Context context;
    private Bundle b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        imagelist = new ArrayList<>();
        recycler_portfolio = (RecyclerView) view.findViewById(R.id.recycler_portfolio);
        recycler_portfolio.setLayoutManager(new GridLayoutManager(context, 2));
        b = getArguments();
        setData();
    }

    private void setData() {
        if (b != null) {
            String data = b.getString("portfolio");

            try {

                JSONArray images = new JSONArray(data);

                for (int i = 0; i < images.length(); i++) {

                    JSONObject jo = images.getJSONObject(i);

                    ModelCategory serviceDetail = new ModelCategory();
                    serviceDetail.setPorfolioImage(jo.getString("image"));
                   // serviceDetail.setImageId(jo.getString("ImageId"));
                    imagelist.add(serviceDetail);
                }
                adapterVendorPortfolio = new AdapterVendorPortfolio(context, FragmentGallery.this, imagelist);
                recycler_portfolio.setAdapter(adapterVendorPortfolio);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }
}
