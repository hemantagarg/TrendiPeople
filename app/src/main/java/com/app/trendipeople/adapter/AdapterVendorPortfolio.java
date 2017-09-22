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
public class AdapterVendorPortfolio extends RecyclerView.Adapter<AdapterVendorPortfolio.CustomViewHolder> {

    ArrayList<ModelCategory> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterVendorPortfolio(Context context, OnCustomItemClicListener lis, ArrayList<ModelCategory> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vendor_portfolio, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        Log.e("portfolioimage", "*" + detail.get(i).getPorfolioImage());
        Picasso.with(mContext).load(detail.get(i).getPorfolioImage()).into(customViewHolder.image_view);
        if (detail.get(i).getComment() != null) {
            customViewHolder.text_hashtag.setText(detail.get(i).getComment());
            customViewHolder.text_hashtag.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.text_hashtag.setVisibility(View.GONE);
        }
        customViewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(i, 1);
            }
        });
        customViewHolder.image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(i, 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view, img_delete;
        TextView text_hashtag;
        RelativeLayout rl_main;

        public CustomViewHolder(View view) {
            super(view);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
            this.image_view = (ImageView) view.findViewById(R.id.image_view);
            this.img_delete = (ImageView) view.findViewById(R.id.img_delete);
            this.text_hashtag = (TextView) view.findViewById(R.id.text_hashtag);
        }

    }


}