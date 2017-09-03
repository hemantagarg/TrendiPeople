package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.adapter.AdapterWalletList;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.NotificationModel;
import com.app.trendipeople.stripe.PaymentActivity;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class WalletFragment extends BaseFragment implements ApiResponse, OnCustomItemClicListener {

    public static WalletFragment walletFragment;
    private Activity mActivity;
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<NotificationModel> arrayList;
    NotificationModel notificationModel;
    private AdapterWalletList adapterWalletList;
    private ImageView image_user;
    private RelativeLayout rl_send_money;
    private TextView text_wallet_balance;
    private String Categoryid = "", categoryImage = "";
    private final String TAG = WalletFragment.class.getSimpleName();
    public static WalletFragment getInstance() {
        if (walletFragment == null)
            walletFragment = new WalletFragment();
        return walletFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        mActivity = getActivity();
        walletFragment = this;
        initViews();
        getBundle();
        getWalletList();
        return view;
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_services);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        rl_send_money=(RelativeLayout) view.findViewById(R.id.rl_send_money);
        manageHeaderView();
        image_user = (ImageView) view.findViewById(R.id.image_user);
        text_wallet_balance = (TextView) view.findViewById(R.id.text_wallet_balance);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWalletList();
            }
        });

        rl_send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mActivity, PaymentActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserDashboard.currentFragment instanceof WalletFragment) {

        }
    }

    private void refreshWalletList() {

        // http://dev.stackmindz.com/trendi/api/wallet.php?user_id=202&user_role=2
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.WALLET + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&user_role=" + AppUtils.getUserRole(mActivity);
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private void getWalletList() {

        // http://onlineworkpro.com/trendi/api/wallet.php?type=1&cat_id=3
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.WALLET + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&user_role=" + AppUtils.getUserRole(mActivity);
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
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.text_wallet));
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

    private void getBundle() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            Categoryid = bundle.getString(AppConstant.CATEGORYID);
            text_wallet_balance.setText(bundle.getString(AppConstant.CATEGORYNAME));
            categoryImage = bundle.getString(AppConstant.CATEGORYIMAGE);
            Picasso.with(mActivity).load(categoryImage).transform(new CircleTransform()).into(image_user);

        }
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
                    text_wallet_balance.setText(data.getString("Totalbal"));
                    JSONArray array = data.getJSONArray("CreditDebitBal");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        notificationModel = new NotificationModel();

                        notificationModel.setDebit(jo.getString("Debit"));
                        notificationModel.setSenderName(jo.getString("SenderName"));
                        notificationModel.setReceiverName(jo.getString("ReceiverName"));
                        notificationModel.setServiceName(jo.getString("ServiceName"));
                        notificationModel.setDate(jo.getString("Date"));
                        notificationModel.setCredit(jo.getString("Credit"));
                        notificationModel.setRowType(1);

                        arrayList.add(notificationModel);
                    }
                    adapterWalletList = new AdapterWalletList(mActivity, this, arrayList);
                    recyclerView.setAdapter(adapterWalletList);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                //    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
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

    }
}
