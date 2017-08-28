package com.app.trendipeople.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.trendipeople.R;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;

public class SelectGender extends AppCompatActivity {

    private Activity mActivity;
    private RelativeLayout rl_men, rl_women;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);

        mActivity = this;
        init();
        setListener();

    }

    private void setListener() {

        rl_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_men.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
                AppUtils.setGender(mActivity, AppConstant.MALE);
                startActivity(new Intent(mActivity, UserDashboard.class));
                finish();

            }
        });
        rl_women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_women.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bottom_layout_selector));
                AppUtils.setGender(mActivity, AppConstant.FEMALE);
                startActivity(new Intent(mActivity, UserDashboard.class));
                finish();
            }
        });


    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        rl_men = (RelativeLayout) findViewById(R.id.rl_men);
        rl_women = (RelativeLayout) findViewById(R.id.rl_women);
    }

}
