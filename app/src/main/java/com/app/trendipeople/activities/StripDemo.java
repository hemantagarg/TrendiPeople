package com.app.trendipeople.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.trendipeople.R;

public class StripDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strip_demo);

      /*  PaymentMethodTokenizationParameters parameters =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.PAYMENT_GATEWAY)
                        .addParameter("gateway", "stripe")
                        .addParameter("stripe:publishableKey", publishableKey)
                        .addParameter("stripe:version", version)
                        .build();*/

    }
}
