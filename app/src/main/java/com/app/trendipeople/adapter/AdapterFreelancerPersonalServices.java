package com.app.trendipeople.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.trendipeople.R;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hemanta on 21-04-2017.
 */
public class AdapterFreelancerPersonalServices extends RecyclerView.Adapter<AdapterFreelancerPersonalServices.CustomViewHolder> {

    ArrayList<ModelCategory> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterFreelancerPersonalServices(Context context, OnCustomItemClicListener lis, ArrayList<ModelCategory> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_freelancer_myservices, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        Log.e("portfolioimage", "*" + detail.get(i).getPorfolioImage());
        Picasso.with(mContext).load(detail.get(i).getServiceImage()).into(customViewHolder.image_service);
        customViewHolder.text_servicename.setText(detail.get(i).getServiceName());
        customViewHolder.text_servicesubcat.setText(" $ " +detail.get(i).getServicePrice());
        customViewHolder.text_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(i,11);
            }
        });customViewHolder.text_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(i,12);
            }
        });

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView image_service;
        RelativeLayout rl_main;
TextView text_servicename,text_servicesubcat,text_serviceprice,text_share,text_book;
        public CustomViewHolder(View view) {
            super(view);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
              this.image_service = (ImageView) view.findViewById(R.id.image_service);
              this.text_servicename = (TextView) view.findViewById(R.id.text_servicename);
              this.text_share = (TextView) view.findViewById(R.id.text_share);
              this.text_servicesubcat = (TextView) view.findViewById(R.id.text_servicesubcat);
              this.text_serviceprice = (TextView) view.findViewById(R.id.text_serviceprice);
              this.text_book = (TextView) view.findViewById(R.id.text_book);
        }

    }


}