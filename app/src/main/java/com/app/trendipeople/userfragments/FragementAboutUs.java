package com.app.trendipeople.userfragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.trendipeople.R;
import com.app.trendipeople.models.ModelCategory;

import org.json.JSONException;
import org.json.JSONObject;

public class FragementAboutUs extends Fragment {

    private Context context;
    private TextView freelanceraboutus, freelancerexperience, freelancerDesc, freelanceraddress, email, name;
    private Bundle b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragement_aboutus, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        freelanceraboutus = (TextView) view.findViewById(R.id.freelanceraboutus);
        freelancerexperience = (TextView) view.findViewById(R.id.freelancerexperience);
        email = (TextView) view.findViewById(R.id.email);
        name = (TextView) view.findViewById(R.id.name);
        freelanceraddress = (TextView) view.findViewById(R.id.freelanceraddress);
        freelancerDesc = (TextView) view.findViewById(R.id.freelancerDesc);

        b = getArguments();
        setData();
    }

    private void setData() {
        if (b != null) {
            String data = b.getString("business");

            try {
                JSONObject jo = new JSONObject(data);

                ModelCategory serviceDetail = new ModelCategory();
                serviceDetail.setFreelancer_name(jo.getString("Name"));

                String s = jo.getString("Name");
                freelanceraboutus.setText("About Me : "+jo.getString("About"));
               // name.setText(s);
                email.setText("EmailId : " + jo.getString("Email"));
                freelanceraddress.setText("Qualifictions : " + jo.getString("Qaulication"));
                freelancerDesc.setText("Languages : " + jo.getString("Language"));
                freelancerexperience.setText("Experience " + " : " + jo.getString("Experience") + "Year");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
