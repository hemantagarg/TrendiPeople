package com.app.trendipeople.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.trendipeople.R;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.Images;
import com.app.trendipeople.models.ModelCategory;

import java.util.ArrayList;

/**
 * Created by Hemanta on 21-04-2017.
 */
public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.CustomViewHolder> {

    ArrayList<ModelCategory> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    AdapterPortfolio adapterPortfolio;

    public AdapterGallery(Context context, OnCustomItemClicListener lis, ArrayList<ModelCategory> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_gallery, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        customViewHolder.text_serviceName.setText(detail.get(i).getServiceName());
        ArrayList<Images> imagelist = detail.get(i).getImagesArrayList();
        Log.e("imagelist", "**"+imagelist.size());
        adapterPortfolio = new AdapterPortfolio(mContext, imagelist);
        customViewHolder.recycler_view.setAdapter(adapterPortfolio);

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_serviceName;
        RecyclerView recycler_view;
        RelativeLayout rl_main;

        public CustomViewHolder(View view) {
            super(view);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
            this.text_serviceName = (TextView) view.findViewById(R.id.text_serviceName);
            recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
            recycler_view.setLayoutManager(new GridLayoutManager(mContext, 2));
        }
    }

}