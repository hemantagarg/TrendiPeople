package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.ActivityChat;
import com.app.trendipeople.activities.LoginActivity;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.adapter.AdapterVendorList;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.utils.GPSTracker;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class VendorListFragment extends BaseFragment implements ApiResponse, OnCustomItemClicListener, OnDateSelectedListener {


    public static VendorListFragment vendorListFragment;
    private Activity mActivity;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<ModelCategory> arrayList;
    ModelCategory modelCategory;
    private AdapterVendorList adapterVendorList;
    private final String TAG = VendorListFragment.class.getSimpleName();
    String latitude = "0.0", longitude = "0.0";
    private String serviceId = "";
    private String serviceName = "";
    private EditText edtAddress;
    private TextView text_continue_date, text_location, text_date, text_time;
    private View date_view;
    private MaterialCalendarView calendarView;
    private TimePicker timePicker;
    private String seleted_date = "";
    private String seleted_time, selectedShowTime = "";
    private RelativeLayout rl_date, rl_location;
    private LinearLayout lldate;
    private SwipeRefreshLayout swipeRefreshLayout;
    DateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
    DateFormat timeShowFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

    public static VendorListFragment getInstance() {
        if (vendorListFragment == null)
            vendorListFragment = new VendorListFragment();
        return vendorListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendor_list, container, false);
        mActivity = getActivity();
        vendorListFragment = this;
        init();
        setListener();
        getBundle();
        manageHeaderView();

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date1 = dateFormat.format(calendar.getTime());
        seleted_date = date1;

        seleted_time = timeFormat.format(calendar.getTime());
        selectedShowTime = timeShowFormat.format(calendar.getTime());

        return view;
    }


    private void init() {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(this);
        Calendar calendar = Calendar.getInstance();
        calendarView.setSelectedDate(calendar);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS)
                .setMinimumDate(calendar).commit();
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        rl_date = (RelativeLayout) view.findViewById(R.id.rl_date);
        rl_location = (RelativeLayout) view.findViewById(R.id.rl_location);
        lldate = (LinearLayout) view.findViewById(R.id.lldate);
        text_continue_date = (TextView) view.findViewById(R.id.text_continue_date);
        text_time = (TextView) view.findViewById(R.id.text_time);
        text_date = (TextView) view.findViewById(R.id.text_date);
        text_location = (TextView) view.findViewById(R.id.text_location);
        date_view = view.findViewById(R.id.date_view);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);

        if (AppUtils.getGender(mActivity).equalsIgnoreCase("")) {
            AppUtils.setGender(mActivity, AppConstant.MALE);
        }
        GPSTracker gps = new GPSTracker(mActivity);
        if (gps.canGetLocation()) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();
        } else {
            showSettingsAlert();
        }

        setCurrentLocation();
    }

    private void setCurrentLocation() {

        // TODO Auto-generated method stub
        GPSTracker gps = new GPSTracker(mActivity);
        if (gps.canGetLocation) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();

            GetAddressFromURLTask1 task1 = new GetAddressFromURLTask1();
            task1.execute(new String[]{latitude, longitude});

        } else {
            /*Toast.makeText(context, "Could not found lat long",
                    Toast.LENGTH_LONG).show();*/
        }

    }

    private class GetAddressFromURLTask1 extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... urls) {

            String response = "";
            HttpResponse response2 = null;
            StringBuilder stringBuilder = new StringBuilder();

            try {

                HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + urls[0] + "," + urls[1] + "&ln=en");

                HttpClient client = new DefaultHttpClient();
                Log.e("Url ", "http://maps.google.com/maps/api/geocode/json?ln=en&latlng=" + urls[0] + "," + urls[1]);


                try {
                    response2 = client.execute(httpGet);

                    HttpEntity entity = response2.getEntity();

                    char[] buffer = new char[2048];
                    Reader reader = new InputStreamReader(entity.getContent(), "UTF-8");

                    while (true) {
                        int n = reader.read(buffer);
                        if (n < 0) {
                            break;
                        }
                        stringBuilder.append(buffer, 0, n);
                    }

                    Log.e("Url response1", stringBuilder.toString());

                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(stringBuilder.toString());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Error 2 :>>", "error in doINBackground OUTER");
                //infowindow.setText("Error in connecting to Google Server... try again later");
            }
            return stringBuilder.toString();
            //return jsonObject;
        }


        protected void onPostExecute(String result) {

            try {
                if (result != null) {
                    //result=	Html.fromHtml(result).toString();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultsObject = jsonObject.getJSONArray("results");
                    JSONObject formattedAddress = (JSONObject) resultsObject.get(0);
                    String formatted_address = formattedAddress.getString("formatted_address");

                    Log.e("formatted Adss from>>", formatted_address);
                    edtAddress.setText(formatted_address);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }


    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            mActivity.onBackPressed();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVendorListRefresh();
            }
        });
        lldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_date.setVisibility(View.VISIBLE);
            }
        });
        rl_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_date.setVisibility(View.VISIBLE);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
                seleted_time = hourOfDay + ":" + minute;
                selectedShowTime = hourOfDay + ":" + minute + " " + AM_PM;
            }
        });

        text_continue_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!seleted_date.equalsIgnoreCase("") && !seleted_time.equalsIgnoreCase("") && !edtAddress.getText().toString().equalsIgnoreCase("")) {
                    //   text_datetime.setText(seleted_date + " " + seleted_time);

                    text_date.setText(seleted_date);
                    text_time.setText(selectedShowTime);
                    text_location.setText(edtAddress.getText().toString());
                    rl_date.setVisibility(View.GONE);
                    getVendorList();

                } else {
                    if (seleted_date.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please select date", Toast.LENGTH_SHORT).show();
                    } else if (seleted_time.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please select time", Toast.LENGTH_SHORT).show();
                    } else if (edtAddress.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please fill address", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (UserDashboard.currentFragment instanceof VendorListFragment) {
            // getVendorListRefresh();
        }
    }

    private void getBundle() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            serviceId = bundle.getString(AppConstant.SERVICEID);
            serviceName = bundle.getString(AppConstant.CATEGORYNAME);

        }
    }

    private void getVendorListRefresh() {

        // http://onlineworkpro.com/trendi/api/category.php?type=1
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.VENDORLIST + "service_id=" + serviceId + "&date=" + seleted_date + "&time=" + seleted_time + "&latitude=" + latitude + "&longitude=" + longitude;
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    private void getVendorList() {

        // http://onlineworkpro.com/trendi/api/category.php?type=1
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.VENDORLIST + "service_id=" + serviceId + "&date=" + seleted_date + "&time=" + seleted_time + "&latitude=" + latitude + "&longitude=" + longitude;
            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private void orderVendor(String freelancer_id) {

        //  http://dev.stackmindz.com/trendi/api/order.php?user_id=201&freelancer_id=200&service_id=2
        // &service_date=5-8-2017&service_time=10:30
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.ORDER + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&service_id=" + serviceId + "&service_date=" + seleted_date + "&freelancer_id=" + freelancer_id + "&service_time=" + seleted_time;
            new CommonAsyncTaskHashmap(2, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, serviceName);
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.search);

    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        return new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                AppUtils.showLog(TAG, "onClickOfHeaderLeftView");
                mActivity.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }


    protected void setFragment(Fragment fragment) {

        UserDashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, fragment, true);
    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {

                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("vendorlist");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        modelCategory = new ModelCategory();

                        modelCategory.setFreelancer_id(jo.getString("freelancer_id"));
                        modelCategory.setFreelancer_name(jo.getString("freelancer_name"));
                        modelCategory.setFreelancer_rating(jo.getString("freelancer_rating"));
                        modelCategory.setFreelancer_service_rate(jo.getString("freelancer_service_rate"));
                        modelCategory.setFreelancer_mile(jo.getString("freelancer_mile"));
                        modelCategory.setFreelancer_image(jo.getString("freelancer_image"));
                        modelCategory.setConverId(jo.getString("ConverId"));
                        modelCategory.setRowType(1);
                        arrayList.add(modelCategory);
                    }
                    adapterVendorList = new AdapterVendorList(mActivity, this, arrayList);
                    recyclerView.setAdapter(adapterVendorList);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 2) {
                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    //  JSONObject data = commandResult.getJSONObject("data");
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    UserDashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, new HomeFragment(), true);

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {
        Toast.makeText(mActivity, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 1) {
            VendorProfileFragment subCategoryFragment = new VendorProfileFragment();
            Bundle b = new Bundle();
            b.putString(AppConstant.VENDORID, arrayList.get(position).getFreelancer_id());

            subCategoryFragment.setArguments(b);
            setFragment(subCategoryFragment);
        } else if (flag == 2) {

            if (AppUtils.getUserId(mActivity).equalsIgnoreCase("")) {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
            } else {
                showOrderConfirmation(arrayList.get(position).getFreelancer_id());

            }
        } else if (flag == 3) {

            Intent in = new Intent(mActivity, ActivityChat.class);

            in.putExtra("reciever_id", arrayList.get(position).getFreelancer_id());
            in.putExtra("name", arrayList.get(position).getFreelancer_name());
            in.putExtra("image", arrayList.get(position).getFreelancer_image());
            in.putExtra("serviceId", serviceId);
            in.putExtra("conver_id", arrayList.get(position).getConverId());
            startActivity(in);

        }

    }

    private void showOrderConfirmation(final String id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                mActivity);

        alertDialog.setTitle("Order !");
        alertDialog.setMessage("Are you sure you want to confirm?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        orderVendor(id);
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date1 = dateFormat.format(date.getDate());
        seleted_date = date1;
    }
}
