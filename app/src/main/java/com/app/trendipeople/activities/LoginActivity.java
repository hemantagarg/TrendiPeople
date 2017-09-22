package com.app.trendipeople.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.aynctask.CommonAsyncTask;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.utils.GPSTracker;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements ApiResponse {

    private Activity mActivity;
    private EditText edtEmail, edtPassword;
    private Button btn_login;
    private TextView createAccount, forgotPassword;
    private ImageView image_facebook, image_instagram, image_gmail;
    String latitude = "0.0", longitude = "0.0";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA,};
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    String sName = "", sEmail = "", sId = "", sSocialType = "";
    private CallbackManager callbackManager;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mActivity = LoginActivity.this;
        initViews();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        registerFacebookCallback();

        setListener();
        GPSTracker gps = new GPSTracker(mActivity);
        if (gps.canGetLocation()) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();
        } else {
            showSettingsAlert();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
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
                            finish();
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
        image_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onFacebookLoginClick();

            }
        });
        image_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtEmail.getText().toString().equalsIgnoreCase("") && !edtPassword.getText().toString().equalsIgnoreCase("")) {

                    if (AppUtils.isEmailValid(edtEmail.getText().toString())) {

                        loginUser();
                    } else {
                        edtEmail.setError(getString(R.string.enter_valid_emailid));
                        edtEmail.requestFocus();
                    }
                } else {
                    if (edtEmail.getText().toString().equalsIgnoreCase("")) {
                        edtEmail.setError(getString(R.string.enter_email));
                        edtEmail.requestFocus();
                    } else if (edtPassword.getText().toString().equalsIgnoreCase("")) {
                        edtPassword.setError(getString(R.string.enter_password));
                        edtPassword.requestFocus();
                    }
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(mActivity, SignupActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(mActivity, ForgotPassword.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            Bundle b = data.getExtras();
            Log.e("Data", "&&&&&&&&&" + b.getString("name"));
        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e("googleresult", "**" + result.isSuccess());
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(mActivity, acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            Log.e("nameFromgoogle", acct.getDisplayName());
            Log.e("emailFromgoogle", acct.getEmail());
            Log.e("IDFromgoogle", acct.getId());

            try {
                sName = acct.getDisplayName();
                sEmail = acct.getEmail();
                sSocialType = "1";
                sId = acct.getId();

                social_login("3", sName, sEmail, "", sId);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
        }
    }

    private void social_login(String socialtype, String name, String email, String number, String social_id) {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/trendi/api/login-check.php?mobile=1234567890&password=123456&gcm=fsfsdfsdfdsfsfsdf
            // &device_type=1&device_id=&social_type=&social_id=

         String   url = JsonApiHelper.BASEURL + JsonApiHelper.LOGIN_CHECK
                 + "email=" + email + "&first_name=" + name + "&mobile=" + number
                    + "&password=" + edtPassword.getText().toString() + "&login_type=" + socialtype + "&social_id=" + social_id+"&social_type="+socialtype
                    + "&gcm=" + AppUtils.getGcmRegistrationKey(mActivity) + "&device_type=" + AppConstant.DEVICE_TYPE;

            new CommonAsyncTask(2, mActivity, LoginActivity.this).getquery(url);

        } else {
            Toast.makeText(mActivity, getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }



    private void loginUser() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            //http://onlineworkpro.com/trendi/api/login.php?mobile=1234567890&password=123456&gcm=fsfsdfsdfdsfsfsdf&device_type=1&device_id=
            String url = JsonApiHelper.BASEURL + JsonApiHelper.LOGIN + "mobile=" + edtEmail.getText().toString() + "&password=" + edtPassword.getText().toString()
                    + "&latitude=" + latitude + "&longitude=" + longitude + "&gcm=" + AppUtils.getGcmRegistrationKey(mActivity) + "&device_type=" + AppConstant.DEVICE_TYPE + "&device_id=" + "";
            new CommonAsyncTaskHashmap(1, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    private void onFacebookLoginClick() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void registerFacebookCallback() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        //  Toast.makeText(SignInActivity.this, "accesstoken****" + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
                        getProfileFromFacebook(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    private void getProfileFromFacebook(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("fb data  ", "fb data  " + object.toString());
                        try {
                            //  Toast.makeText(context, "F Login" + object.toString(), Toast.LENGTH_LONG).show();
                            String email = "", number = "";
                            if (object.has("email")) {
                                email = object.getString("email");
                            } else {
                                email = "";
                            }
                            if (object.has("mobile_phone")) {
                                number = object.getString("mobile_phone");
                            } else {
                                number = "";
                            }
                            social_login("2", object.getString("name"), email, number, object.getString("id"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //finish();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,first_name,last_name,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void initViews() {

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        createAccount = (TextView) findViewById(R.id.createAccount);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        image_facebook = (ImageView) findViewById(R.id.image_facebook);
        image_instagram = (ImageView) findViewById(R.id.image_instagram);
        image_gmail = (ImageView) findViewById(R.id.image_gmail);

    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {

                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    AppUtils.setUserId(mActivity, data.getString("UserId"));
                    AppUtils.setUserRole(mActivity, data.getString("UserType"));
                    AppUtils.setUserName(mActivity, data.getString("Name"));
                    AppUtils.setUseremail(mActivity, data.getString("Email"));
                    AppUtils.setUserMobile(mActivity, data.getString("Mobile"));
                    AppUtils.setUserImage(mActivity, data.getString("ProfilePic"));
                    AppUtils.setCategories(mActivity, data.getJSONArray("services").toString());

                    if (data.has("Business_profile")) {
                        JSONObject business_profile = data.getJSONObject("Business_profile");
                        if (business_profile.has("Id")) {
                            AppUtils.setBusinessId(mActivity, business_profile.getString("Id"));
                        }
                    }

                    if (AppUtils.getUserRole(mActivity).equalsIgnoreCase(AppConstant.FREELANCER)) {
                        startActivity(new Intent(mActivity, VendorDashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    } else if (AppUtils.getUserRole(mActivity).equalsIgnoreCase(AppConstant.USER)) {
                        startActivity(new Intent(mActivity, UserDashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }

                } else {

                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }else   if (method == 2) {

                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    AppUtils.setUserId(mActivity, data.getString("UserId"));
                    AppUtils.setUserRole(mActivity, data.getString("UserType"));
                    AppUtils.setUserName(mActivity, data.getString("Name"));
                    AppUtils.setUseremail(mActivity, data.getString("Email"));
                    AppUtils.setUserMobile(mActivity, data.getString("Mobile"));
                    AppUtils.setUserImage(mActivity, data.getString("ProfilePic"));
                    AppUtils.setCategories(mActivity, data.getJSONArray("services").toString());

                    if (data.has("Business_profile")) {
                        JSONObject business_profile = data.getJSONObject("Business_profile");
                        if (business_profile.has("Id")) {
                            AppUtils.setBusinessId(mActivity, business_profile.getString("Id"));
                        }
                    }

                    if (AppUtils.getUserRole(mActivity).equalsIgnoreCase(AppConstant.FREELANCER)) {
                        startActivity(new Intent(mActivity, VendorDashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    } else if (AppUtils.getUserRole(mActivity).equalsIgnoreCase(AppConstant.USER)) {
                        startActivity(new Intent(mActivity, UserDashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }

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

    }
}
