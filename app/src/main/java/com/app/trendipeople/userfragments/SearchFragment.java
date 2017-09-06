package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.ActivityChat;
import com.app.trendipeople.activities.LoginActivity;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.adapter.AdapterSearch;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class SearchFragment extends BaseFragment implements ApiResponse, OnCustomItemClicListener, OnDateSelectedListener {


    public static SearchFragment serviceTypeFragment;
    private Activity mActivity;
    private View view;
    private EditText edtSearch;
    private ImageView image_search;
    private RecyclerView recyclerView;
    private ArrayList<ModelCategory> arrayList;
    ModelCategory modelCategory;
    private AdapterSearch adapterSearch;
    private EditText edtAddress;
    private TextView text_continue_date;
    DateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
    private String seleted_date = "";
    private String seleted_time;
    String latitude = "0.0", longitude = "0.0";
    private MaterialCalendarView calendarView;
    private TimePicker timePicker;
    private final String TAG = SearchFragment.class.getSimpleName();
    private RelativeLayout rl_date;
    private int selectedId;

    public static SearchFragment getInstance() {
        if (serviceTypeFragment == null)
            serviceTypeFragment = new SearchFragment();
        return serviceTypeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.search_fragment, container, false);
        mActivity = getActivity();
        serviceTypeFragment = this;
        initViews();
        setListener();
        return view;
    }

    private void setListener() {

        text_continue_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!seleted_date.equalsIgnoreCase("") && !seleted_time.equalsIgnoreCase("")) {
                    //   text_datetime.setText(seleted_date + " " + seleted_time);
                    orderVendor();
                } else {
                    if (seleted_date.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please select date", Toast.LENGTH_SHORT).show();
                    } else if (seleted_time.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please select time", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void initViews() {
        arrayList = new ArrayList<>();
        text_continue_date = (TextView) view.findViewById(R.id.text_continue_date);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        edtAddress.setVisibility(View.GONE);
        image_search = (ImageView) view.findViewById(R.id.image_search);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        manageHeaderView();
        rl_date = (RelativeLayout) view.findViewById(R.id.rl_date);
        rl_date.setVisibility(View.GONE);
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(this);
        Calendar calendar = Calendar.getInstance();
        calendarView.setSelectedDate(calendar);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS)
                .setMinimumDate(calendar).commit();
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date1 = dateFormat.format(calendar.getTime());
        seleted_date = date1;

        seleted_time = timeFormat.format(calendar.getTime());
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtSearch.getText().toString().equalsIgnoreCase("")) {
                    searchService();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserDashboard.currentFragment instanceof SearchFragment) {

        }
    }

    private void searchService() {

        if (AppUtils.isNetworkAvailable(mActivity)) {
            // http://dev.stackmindz.com/trendi/api/searchService.php?search_data=nail
            String url = JsonApiHelper.BASEURL + JsonApiHelper.SEARCH_SERVICE + "search_data=" + edtSearch.getText().toString();
            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

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
        HeaderViewManager.getInstance().setHeading(true, "Search Service");
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
                    JSONArray array = data.getJSONArray("Search");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        modelCategory = new ModelCategory();
                        modelCategory.setServiceId(jo.getString("ServiceId"));
                        modelCategory.setServiceName(jo.getString("ServiceName"));
                        modelCategory.setVendor_Id(jo.getString("Vendor_Id"));
                        modelCategory.setVendorName(jo.getString("Vendor_name"));
                        modelCategory.setComment(jo.getString("Comment"));
                        modelCategory.setServiceImage(jo.getString("ServiceImage"));
                        modelCategory.setRowType(1);

                        arrayList.add(modelCategory);
                    }
                    adapterSearch = new AdapterSearch(mActivity, this, arrayList);
                    recyclerView.setAdapter(adapterSearch);
                } else {
                    Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 2) {
                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {
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

                selectedId = position;
                showOrderConfirmation(arrayList.get(position).getFreelancer_id());
            }
        } else if (flag == 3) {

            Intent in = new Intent(mActivity, ActivityChat.class);
            in.putExtra("reciever_id", arrayList.get(position).getVendor_Id());
            in.putExtra("name", arrayList.get(position).getVendorName());
            in.putExtra("image", arrayList.get(position).getFreelancer_image());
            in.putExtra("serviceId", arrayList.get(position).getServiceId());
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

                        rl_date.setVisibility(View.VISIBLE);
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

    private void orderVendor() {
        //  http://dev.stackmindz.com/trendi/api/order.php?user_id=201&freelancer_id=200&service_id=2
        // &service_date=5-8-2017&service_time=10:30
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.ORDER + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&service_id=" + arrayList.get(selectedId).getServiceId() + "&service_date=" + seleted_date + "&freelancer_id=" + arrayList.get(selectedId).getVendor_Id() + "&service_time=" + seleted_time;
            new CommonAsyncTaskHashmap(2, mActivity, this).getquery(url);
        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }


}
