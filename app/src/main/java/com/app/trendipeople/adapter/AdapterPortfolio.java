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
import com.app.trendipeople.models.Images;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hemanta on 21-04-2017.
 */
public class AdapterPortfolio extends RecyclerView.Adapter<AdapterPortfolio.CustomViewHolder> {

    ArrayList<Images> detail;
    Context mContext;

    public AdapterPortfolio(Context context, ArrayList<Images> list) {

        this.detail = list;
        this.mContext = context;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vendor_portfolio, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        Log.e("portfolioimage", "*" + detail.get(i).getImage());
        Picasso.with(mContext).load(detail.get(i).getImage()).into(customViewHolder.image_view);
        customViewHolder.img_delete.setVisibility(View.GONE);
        if (detail.get(i).getComment() != null) {
            customViewHolder.text_hashtag.setText(detail.get(i).getComment());
            customViewHolder.text_hashtag.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.text_hashtag.setVisibility(View.GONE);
        }
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