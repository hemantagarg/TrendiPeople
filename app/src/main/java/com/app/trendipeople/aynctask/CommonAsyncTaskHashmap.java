package com.app.trendipeople.aynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.trendipeople.R;
import com.app.trendipeople.iclasses.WebserviceAPIErrorHandler;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.models.VolleyErrorModel;

import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Hemanta on 11/7/2016.
 */
public class CommonAsyncTaskHashmap {

    private ProgressDialog pd;
    private RequestQueue queue;
    private Context context;
    private ApiResponse listener;
    int method;

    public CommonAsyncTaskHashmap(int method, Context context, ApiResponse response) {

        queue = Volley.newRequestQueue(context);
        this.context = context;
        listener = response;
        this.method = method;
        pd = new ProgressDialog(context);
        pd.setMessage("Processing ... ");
        pd.setCancelable(false);

    }


    public void getquery(String url) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url);
        pd.show();
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response.toString());
                        if (pd != null)
                            pd.cancel();
                        try {
                            if (response != null) {
                                JSONObject jo = new JSONObject(response);
                                if (listener != null)
                                    listener.onPostSuccess(method, jo);
                            } else {
                                if (listener != null)
                                    // listener.onPostRequestFailed(method, "Null data from server.");
                                    Toast.makeText(context,
                                            context.getResources().getString(R.string.problem_server),
                                            Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                if (pd != null)
                    pd.cancel();
                try {
                    if (listener != null) {
                        VolleyErrorModel mVolleyErrorModel = WebserviceAPIErrorHandler.getInstance()
                                .VolleyErrorHandlerReturningModel(error, context);
                        listener.onPostFail(method, mVolleyErrorModel.getStrMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

// Adding request to request queue
        queue.add(jsonObjReq);

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                GlobalConstants.ONE_SECOND_DELAY * 40, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void getqueryNoProgress(String url) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url);

        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response.toString());

                        try {
                            if (response != null) {
                                JSONObject jo = new JSONObject(response);
                                if (listener != null)
                                    listener.onPostSuccess(method, jo);
                            } else {
                                if (listener != null)
                                    // listener.onPostRequestFailed(method, "Null data from server.");
                                    Toast.makeText(context,
                                            context.getResources().getString(R.string.problem_server),
                                            Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog

                try {
                    if (listener != null) {

                        VolleyErrorModel mVolleyErrorModel = WebserviceAPIErrorHandler.getInstance()
                                .VolleyErrorHandlerReturningModel(error, context);
                        listener.onPostFail(method, mVolleyErrorModel.getStrMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

// Adding request to request queue
        queue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                GlobalConstants.ONE_SECOND_DELAY * 40, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

}
