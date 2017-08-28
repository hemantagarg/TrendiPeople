package com.app.trendipeople.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class ActivityRating extends AppCompatActivity implements ApiResponse {

    ImageView image_smile;
    TextView text_ratingtype;
    RatingBar ratingbar;
    EditText edtAddress_edtcomment;
    Button submit;
    String service_id = "", orderid = "", searchId = "";
    Toolbar toolbar;
    Context context;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        context = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rating & Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent in = getIntent();
        orderid = in.getExtras().getString("orderid");
        setupui();
        ratingbar.setRating(5);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void setupui() {
        submit = (Button) findViewById(R.id.submit);
        text_ratingtype = (TextView) findViewById(R.id.text_ratingtype);
        image_smile = (ImageView) findViewById(R.id.image_smile);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        edtAddress_edtcomment = (EditText) findViewById(R.id.edtAddress_edtcomment);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAddress_edtcomment.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(context, "Please give your review", Toast.LENGTH_SHORT).show();

                } else {
                    submitRating();

                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                finish();

            }

        });

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Log.e("rating", "*" + rating);
                Log.e("Math.floor(rating)", "*" + Math.floor(rating));
                if (rating == 1 || rating < 1) {
                    image_smile.setImageResource(R.drawable.very_bad);
                    text_ratingtype.setText("Very Dissatisfied");
                }
                if (rating > 1 || rating == 2) {
                    image_smile.setImageResource(R.drawable.bad);
                    text_ratingtype.setText("Dissatisfied");
                }
                if (rating > 2 || rating == 3) {
                    image_smile.setImageResource(R.drawable.okay);
                    text_ratingtype.setText("Neutral");
                }
                if (rating > 3 || rating == 4) {
                    image_smile.setImageResource(R.drawable.good_job);
                    text_ratingtype.setText("Satisfied");
                }
                if (rating > 4 || rating == 5) {
                    image_smile.setImageResource(R.drawable.excellent_job);
                    text_ratingtype.setText("Very Satisfied");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void submitRating() {

        try {
            if (AppUtils.isNetworkAvailable(context)) {

                //  http://dev.stackmindz.com/trendi/api/sendRating?order_id=3&rating=4&review=aadadas

                String url = JsonApiHelper.BASEURL + JsonApiHelper.SEND_RATING + "order_id=" + "" + "&rating=" + ratingbar.getRating()
                        + "&review=" + edtAddress_edtcomment.getText().toString();
                new CommonAsyncTaskHashmap(1, context, this).getquery(url);

            } else {
                Toast.makeText(context, getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {

            if (method == 1) {

                JSONObject commandResult = response
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    //    JSONObject data = commandResult.getJSONObject("data");
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, UserDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
