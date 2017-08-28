package com.app.trendipeople.vendorfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.adapter.AdapterTimeList;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.NotificationModel;
import com.app.trendipeople.userfragments.BaseFragment;
import com.app.trendipeople.utils.AppUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentVenderDateTime extends BaseFragment implements ApiResponse, OnCustomItemClicListener, TimePickerDialog.OnTimeSetListener {


    public static FragmentVenderDateTime alertFragment;
    private Activity mActivity;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<NotificationModel> arrayList;
    NotificationModel notificationModel;
    private AdapterTimeList adapterTimeList;
    private MaterialCalendarView calendarView;
    private TextView mTvStartTime, mTvEndTime;
    private Button btnAdd, btn_save;
    private boolean isStartTime = false;
    private
    String endTime = "", startTime = "";
    private final String TAG = FragmentVenderDateTime.class.getSimpleName();

    public static FragmentVenderDateTime getInstance() {
        if (alertFragment == null)
            alertFragment = new FragmentVenderDateTime();
        return alertFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendor_datetime, container, false);
        mActivity = getActivity();
        alertFragment = this;
        initViews();
        setListener();

        adapterTimeList = new AdapterTimeList(mActivity, this, arrayList);
        recyclerView.setAdapter(adapterTimeList);

        //  getNotificationList();
        return view;
    }

    private void setListener() {


        mTvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isStartTime = true;
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        FragmentVenderDateTime.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(mActivity.getFragmentManager(), "startTime");
            }
        });


        mTvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartTime = false;
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        FragmentVenderDateTime.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(mActivity.getFragmentManager(), "endTime");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!startTime.equalsIgnoreCase("") && !endTime.equalsIgnoreCase("")) {
                    if (isTimeAfter(startTime, endTime)) {
                        NotificationModel model = new NotificationModel();
                        model.setStartTime(startTime);
                        model.setEndTime(endTime);
                        model.setRowType(1);
                        arrayList.add(model);
                        adapterTimeList.notifyDataSetChanged();

                        mTvEndTime.setText("");
                        mTvStartTime.setText("");
                        startTime = "";
                        endTime = "";
                    } else {
                        Toast.makeText(mActivity, "Start time cannot be greater than end time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (startTime.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please select Start time", Toast.LENGTH_SHORT).show();
                    } else if (endTime.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please select End time", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (arrayList.size() > 0) {
                    // saveData();
                    if (calendarView.getSelectedDates().size() > 0) {
                        List<CalendarDay> selectedDates = calendarView.getSelectedDates();
                        saveData(selectedDates);

                    } else {
                        Toast.makeText(mActivity, "Please select date and time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, "Please add start time and end time", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initViews() {
        arrayList = new ArrayList<>();
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        Calendar calendar = Calendar.getInstance();
        calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS)
                .setMinimumDate(calendar).commit();
        mTvEndTime = (TextView) view.findViewById(R.id.mTvEndTime);
        mTvStartTime = (TextView) view.findViewById(R.id.mTvStartTime);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_services);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView.setNestedScrollingEnabled(false);
        manageHeaderView();

    }

    boolean isTimeAfter(String strStartTime, String strEndTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = sdf.parse(strStartTime);
            endTime = sdf.parse(strEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (endTime.before(startTime)) { //Same way you can check with after() method also.
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserDashboard.currentFragment instanceof FragmentVenderDateTime) {

        }
    }

    private String convertDateToString(CalendarDay date) {
        String date1 = "";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        date1 = dateFormat.format(date.getDate());

        return date1;
    }

    private void saveData(List<CalendarDay> selectedDates) {
        try {
            JSONObject main = new JSONObject();

            JSONArray dates = new JSONArray();

            for (int i = 0; i < selectedDates.size(); i++) {

                JSONObject data = new JSONObject();
                data.put("date", convertDateToString(selectedDates.get(i)));

                JSONArray times = new JSONArray();
                for (int j = 0; j < arrayList.size(); j++) {

                    JSONObject timeObject = new JSONObject();
                    timeObject.put("start_time", arrayList.get(j).getStartTime());
                    timeObject.put("end_time", arrayList.get(j).getEndTime());

                    times.put(timeObject);
                }
                data.put("time", times);
                dates.put(data);
            }
            main.put("dates", dates);

            // http:dev.stackmindz.com/trendi/api/save-service-date-time.php?date={%22dates%22:[{%22date%22:%2220-08-2017%22,%22time%22:[{%22start_time%22:%2216:44%22,%22end_time%22:%2217:66%22},{%22start_time%22:%2217:44%22,%22end_time%22:%2218:66%22},{%22start_time%22:%2212:44%22,%22end_time%22:%2213:66%22}]},{%22date%22:%2221-08-2017%22,%22time%22:[{%22start_time%22:%2209:44%22,%22end_time%22:%2210:66%22},{%22start_time%22:%2211:44%22,%22end_time%22:%2212:66%22},{%22start_time%22:%2220:44%22,%22end_time%22:%2220:66%22}]}]}
            // &user_id=201
            if (AppUtils.isNetworkAvailable(mActivity)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.SAVE_SERVICE_DATETIME + "user_id=" + AppUtils.getUserId(mActivity)
                        + "&date=" + main;
                new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

            } else {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Save Date Time");
        HeaderViewManager.getInstance().setLeftSideHeaderView(false, R.drawable.left_arrow);
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

                    // JSONObject data = commandResult.getJSONObject("data");
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    mActivity.onBackPressed();
                } else {
                    if (getUserVisibleHint()) {
                        Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    }
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
            arrayList.remove(position);
            adapterTimeList.notifyDataSetChanged();
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String AM_PM;
        if (hourOfDay < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }

        if (isStartTime) {
            startTime = hourOfDay + ":" + minute;
            mTvStartTime.setText(startTime + " " + AM_PM);
        } else {
            endTime = hourOfDay + ":" + minute;
            mTvEndTime.setText(endTime + " " + AM_PM);
        }
    }
}
