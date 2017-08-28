package com.app.trendipeople.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.trendipeople.R;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterSubCategoryList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelCategory> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterSubCategoryList(Context context, OnCustomItemClicListener lis, ArrayList<ModelCategory> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_subcategorylist, parent, false);

            vh = new CustomViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ModelCategory m1 = (ModelCategory) detail.get(position);

            ((CustomViewHolder) holder).text_name.setText(m1.getSubCategoryName());

            ((CustomViewHolder) holder).card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(position, 1);
                }
            });

            if (position % 2 != 0) {
                ((CustomViewHolder) holder).card_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.row_color));
            }


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_name;
        ImageView image_user;
        RelativeLayout card_view;

        public CustomViewHolder(View view) {
            super(view);

            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.image_user = (ImageView) view.findViewById(R.id.image_user);
            this.card_view = (RelativeLayout) view.findViewById(R.id.card_view);

        }


    }

    public void setFilter(ArrayList<ModelCategory> detailnew) {
        detail = new ArrayList<>();
        detail.addAll(detailnew);
        notifyDataSetChanged();
    }

    public ModelCategory getFilter(int i) {

        return detail.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        ModelCategory m1 = (ModelCategory) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }
}

