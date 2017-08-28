package com.app.trendipeople.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.trendipeople.R;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppConstant;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterUpdateServieList extends RecyclerView.Adapter<AdapterUpdateServieList.CustomViewHolder> {

    ArrayList<ModelCategory> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterUpdateServieList(Context context, OnCustomItemClicListener lis, ArrayList<ModelCategory> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_update_pricelist, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view, new MyCustomEditTextListener());
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        customViewHolder.text_title.setText("Category : " + detail.get(position).getCategoryName());
        customViewHolder.text_serviceName.setText("Service : " + detail.get(position).getServiceName());
        customViewHolder.text_subcatName.setText("SubCategory : " + detail.get(position).getSubCategoryName());

        customViewHolder.myCustomEditTextListener.updatePosition(customViewHolder.getAdapterPosition());
        customViewHolder.edt_price.setText(detail.get(position).getServicePrice());
        customViewHolder.checkbox.setChecked(detail.get(position).isSelected());

        if (detail.get(position).getGenderType().equalsIgnoreCase(AppConstant.MALE)) {
            customViewHolder.text_gender.setText("Male");
            customViewHolder.ll_row.setBackgroundColor(ContextCompat.getColor(mContext, R.color.row_color));
        } else {
            customViewHolder.text_gender.setText("Female");
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
        TextView text_title, text_subcatName, text_serviceName, text_gender;
        CheckBox checkbox;
        EditText edt_price;
        LinearLayout ll_row;
        public MyCustomEditTextListener myCustomEditTextListener;

        public CustomViewHolder(View view, MyCustomEditTextListener myCustomEditTextListener) {
            super(view);

            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
            this.ll_row = (LinearLayout) view.findViewById(R.id.ll_row);
            this.edt_price = (EditText) view.findViewById(R.id.edt_price);
            this.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            this.text_gender = (TextView) view.findViewById(R.id.text_gender);
            this.text_serviceName = (TextView) view.findViewById(R.id.text_serviceName);
            this.text_subcatName = (TextView) view.findViewById(R.id.text_subcatName);
            this.text_title = (TextView) view.findViewById(R.id.text_title);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.edt_price.addTextChangedListener(myCustomEditTextListener);
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            detail.get(position).setServicePrice(charSequence.toString());

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }


}