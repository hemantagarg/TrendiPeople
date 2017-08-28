package com.app.trendipeople.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.trendipeople.R;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterSelectCategoryList extends RecyclerView.Adapter<AdapterSelectCategoryList.CustomViewHolder> {
    ArrayList<ModelCategory> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterSelectCategoryList(Context context, OnCustomItemClicListener lis, ArrayList<ModelCategory> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_selectcategorylist, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        customViewHolder.text_title.setText("Category : "+detail.get(position).getCategoryName());
        customViewHolder.text_serviceName.setText("Service : "+detail.get(position).getServiceName());
        customViewHolder.text_subcatName.setText("SubCategory : "+detail.get(position).getSubCategoryName());
        customViewHolder.text_gender.setText(detail.get(position).getGenderType());

        customViewHolder.checkbox.setChecked(detail.get(position).isSelected());

        if (detail.get(position).getGenderType().equalsIgnoreCase("Male")) {
            customViewHolder.ll_row.setBackgroundColor(ContextCompat.getColor(mContext, R.color.row_color));
        }else {
            customViewHolder.ll_row.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }

        customViewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(position, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_background;
        TextView text_title,text_subcatName,text_serviceName,text_gender;
        CheckBox checkbox;
        LinearLayout ll_row;


        public CustomViewHolder(View view) {
            super(view);

            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
            this.ll_row = (LinearLayout) view.findViewById(R.id.ll_row);
            this.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            this.text_gender= (TextView) view.findViewById(R.id.text_gender);
            this.text_serviceName= (TextView) view.findViewById(R.id.text_serviceName);
            this.text_subcatName= (TextView) view.findViewById(R.id.text_subcatName);
            this.text_title= (TextView) view.findViewById(R.id.text_title);
        }
    }


}