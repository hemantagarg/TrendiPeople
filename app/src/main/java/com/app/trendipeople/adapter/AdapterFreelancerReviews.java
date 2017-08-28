package com.app.trendipeople.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
public class AdapterFreelancerReviews extends RecyclerView.Adapter<AdapterFreelancerReviews.CustomViewHolder> {

    ArrayList<ModelCategory> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterFreelancerReviews(Context context, OnCustomItemClicListener lis, ArrayList<ModelCategory> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vendor_review, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        Picasso.with(mContext).load(detail.get(i).getUserImage()).into(customViewHolder.image_user);
        customViewHolder.text_name.setText(detail.get(i).getUsername());
        customViewHolder.text_review.setText(detail.get(i).getReview());
        try {
            customViewHolder.ratingbar.setRating(Float.parseFloat(detail.get(i).getFreelancer_rating()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView image_user;
        RelativeLayout rl_main;
        RatingBar ratingbar;
        TextView text_name, text_review;

        public CustomViewHolder(View view) {
            super(view);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
            this.image_user = (ImageView) view.findViewById(R.id.image_user);
            this.ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_review = (TextView) view.findViewById(R.id.text_review);
        }

    }


}