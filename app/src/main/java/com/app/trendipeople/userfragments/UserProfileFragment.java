package com.app.trendipeople.userfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.activities.SelctCategoryListActivity;
import com.app.trendipeople.activities.UserDashboard;
import com.app.trendipeople.activities.VendorDashboard;
import com.app.trendipeople.aynctask.CommonAsyncTaskAquery;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.iclasses.InternalStorageContentProvider;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.GlobalConstants;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.utils.AppConstant;
import com.app.trendipeople.utils.AppUtils;
import com.app.trendipeople.utils.CircleTransform;
import com.app.trendipeople.utils.GPSTracker;
import com.app.trendipeople.vendorfragments.FragmentVendorPortfolio;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import eu.janmuller.android.simplecropimage.CropImage;

import static com.app.trendipeople.R.id.edtName;


public class UserProfileFragment extends BaseFragment implements ApiResponse {


    public static UserProfileFragment userProfileFragment;
    private Activity mActivity;
    private View view;
    private final String TAG = UserProfileFragment.class.getSimpleName();
    private EditText user_name, edtmobilenumber, edtEmailId, edtWebsite, edtProfileDescription,
            edtbusinessname, edtAddress, edtbusinessEmail, edtbusinessAddress, edtAbout,
            edtbusinessDesc, edtlanguage, edtQualification;
    private TextView text_select_category;
    private RadioGroup radioGender, radioExperience;
    private RelativeLayout rl_category;
    private ImageView image_edit, image_user, image_banner;
    private Button btn_updateprofile;
    private AppCompatCheckBox freelancerCheckbox;
    private LinearLayout llFrelancer;
    private String selectedGender = "", selectedServiceId = "", selectedserviceName = "";
    String latitude = "0.0", longitude = "0.0";
    String path = "", selectedPath1 = "";
    private File mFileTemp, selectedFilePath, seletedImageUser, seletedBanner;
    public static FragmentVendorPortfolio fragmentVendorPortfolio;
    private Bitmap bitmap = null;
    private Bundle b;
    RelativeLayout rl_banner;
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    private boolean isBannerImage;
    private View view_bg;
    RadioButton radio_male, radio_female, radio_junior, radio_intermediate, radio_professional;
    String expLevel = "";

    public static UserProfileFragment getInstance() {
        if (userProfileFragment == null)
            userProfileFragment = new UserProfileFragment();
        return userProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_profile_fragment, container, false);
        mActivity = getActivity();
        userProfileFragment = this;
        initViews();

        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(mActivity.getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        manageHeaderView();
        setListener();
        getProfile();

        return view;
    }


    private void getProfile() {

        // http://dev.stackmindz.com/trendi/api/getProfile.php?user_id=210
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_PROFILE + "user_id=" + AppUtils.getUserId(mActivity);
            new CommonAsyncTaskHashmap(2, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    private void setListener() {

        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isBannerImage = false;
                selectImage1();
            }
        });
        image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isBannerImage = true;
                selectImage1();
            }
        });

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.radio_male) {

                    selectedGender = AppConstant.MALE;
                } else {
                    selectedGender = AppConstant.FEMALE;

                }

            }
        });

        radioExperience.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.radio_junior) {
                    expLevel = "1";
                } else if (checkedId == R.id.radio_intermediate) {
                    expLevel = "2";
                }else if (checkedId == R.id.radio_professional) {
                    expLevel = "3";
                }

            }
        });


        freelancerCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    llFrelancer.setVisibility(View.VISIBLE);
                } else {
                    llFrelancer.setVisibility(View.GONE);
                }
            }
        });

        rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mActivity, SelctCategoryListActivity.class);
                startActivityForResult(intent, 511);
            }
        });

        btn_updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidLoginDetails()) {
                    if (freelancerCheckbox.isChecked()) {
                        if (!text_select_category.getText().toString().equalsIgnoreCase("")) {
                            updateProfile(AppConstant.FREELANCER);
                        } else {
                            if (selectedServiceId.equalsIgnoreCase("")) {
                                Toast.makeText(mActivity, R.string.enterCategory, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        updateProfile(AppConstant.USER);
                    }
                }
            }
        });
    }


    private void initViews() {
        text_select_category = (TextView) view.findViewById(R.id.text_select_category);
        radioGender = (RadioGroup) view.findViewById(R.id.radioGender);
        btn_updateprofile = (Button) view.findViewById(R.id.btn_updateprofile);
        freelancerCheckbox = (AppCompatCheckBox) view.findViewById(R.id.freelancerCheckbox);
        user_name = (EditText) view.findViewById(edtName);
        radio_male = (RadioButton) view.findViewById(R.id.radio_male);
        radio_female = (RadioButton) view.findViewById(R.id.radio_female);
        radio_junior = (RadioButton) view.findViewById(R.id.radio_junior);
        radio_intermediate = (RadioButton) view.findViewById(R.id.radio_intermediate);
        radio_professional = (RadioButton) view.findViewById(R.id.radio_professional);
        radioExperience = (RadioGroup) view.findViewById(R.id.radioExperience);
        user_name.setText(AppUtils.getUserName(getContext()));
        edtmobilenumber = (EditText) view.findViewById(R.id.edtmobilenumber);
        edtmobilenumber.setText(AppUtils.getUserMobile(getContext()));
        edtEmailId = (EditText) view.findViewById(R.id.edtEmailId);
        view_bg = (View) view.findViewById(R.id.view_bg);
        edtWebsite = (EditText) view.findViewById(R.id.edtWebsite);
        edtbusinessEmail = (EditText) view.findViewById(R.id.edtbusinessEmail);
        edtbusinessAddress = (EditText) view.findViewById(R.id.edtbusinessAddress);
        edtAbout = (EditText) view.findViewById(R.id.edtAbout);

        edtbusinessDesc = (EditText) view.findViewById(R.id.edtbusinessDesc);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        edtlanguage = (EditText) view.findViewById(R.id.edtlanguage);
        edtQualification = (EditText) view.findViewById(R.id.edtQualification);
        edtProfileDescription = (EditText) view.findViewById(R.id.edtProfileDescription);
        edtbusinessname = (EditText) view.findViewById(R.id.edtbusinessname);
        rl_category = (RelativeLayout) view.findViewById(R.id.rl_category);
        edtEmailId.setText(AppUtils.getUseremail(getContext()));
        llFrelancer = (LinearLayout) view.findViewById(R.id.llFrelancer);
        rl_banner = (RelativeLayout) view.findViewById(R.id.rl_banner);
        image_banner = (ImageView) view.findViewById(R.id.image_banner);
        image_edit = (ImageView) view.findViewById(R.id.image_edit);
        image_user = (ImageView) view.findViewById(R.id.image_user);
        rl_banner.setVisibility(View.GONE);
       /* if (AppUtils.getUserRole(mActivity).equalsIgnoreCase(AppConstant.USER)) {
            rl_banner.setVisibility(View.GONE);
        } else {
            rl_banner.setVisibility(View.VISIBLE);
        }
*/
        GPSTracker gps = new GPSTracker(mActivity);
        if (gps.canGetLocation()) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();
        } else {
            showSettingsAlert();
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

    private void selectImage1() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                mActivity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    takePicture();

                } else if (items[item].equals("Choose from Library")) {

                    openGallery();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                    /*
                     * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
		        	 */
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }

    private void startCropImage() {

        Intent intent = new Intent(mActivity, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 511:
                if (data != null) {
                    selectedServiceId = data.getStringExtra("serviceId");
                    selectedserviceName = data.getStringExtra("servicename");
                    text_select_category.setText(selectedserviceName);
                }
                break;
            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;

            case REQUEST_CODE_GALLERY:
                try {
                    InputStream inputStream = mActivity.getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file", e);
                }
                //  upload_image.setText("Image upload successfully");
                break;

            case REQUEST_CODE_CROP_IMAGE:
                try {
                    path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                selectedFilePath = new File(path);
                Log.e("filepath", "**" + selectedFilePath);
                if (isBannerImage) {
                    seletedBanner = selectedFilePath;
                    Picasso.with(mActivity).load(selectedFilePath).into(image_banner);
                    uploadPhoto();
                } else {
                    seletedImageUser = selectedFilePath;
                    Picasso.with(mActivity).load(seletedImageUser).placeholder(R.drawable.user).transform(new CircleTransform()).into(image_user);
                }
                break;


        }
    }

    public void uploadPhoto() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            HashMap<String, Object> hm = new HashMap<>();

            hm.put("banner", seletedBanner);
            hm.put("user_id", AppUtils.getUserId(mActivity));

            // http://dev.stackmindz.com/trendi/api/banner.php?user_id=202&banner=

            String url = JsonApiHelper.BASEURL + JsonApiHelper.BANNER;
            new CommonAsyncTaskAquery(4, mActivity, this).getquery(url, hm);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    private void updateProfile(String userRole) {
        if (AppUtils.isNetworkAvailable(mActivity)) {

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("name", user_name.getText().toString());
            hm.put("mobile", edtmobilenumber.getText().toString());
            hm.put("address", edtAddress.getText().toString());
            hm.put("gender", selectedGender);
            hm.put("email", edtEmailId.getText().toString());
            hm.put("about", edtAbout.getText().toString());
            hm.put("business_lang", edtlanguage.getText().toString());
            hm.put("business_quali", edtQualification.getText().toString());
            hm.put("business_desc", edtbusinessDesc.getText().toString());
            hm.put("website_link", edtWebsite.getText().toString());
            hm.put("business_exp", expLevel);
            hm.put("business_name", edtbusinessname.getText().toString());
            hm.put("business_address", edtbusinessAddress.getText().toString());
            hm.put("business_email", edtbusinessEmail.getText().toString());
            hm.put("user_role", userRole);
            hm.put("profile_image", seletedImageUser);
            hm.put("latitude", latitude);
            hm.put("longitude", longitude);
            hm.put("service_id", selectedServiceId);
            hm.put("banner", seletedBanner);
            hm.put("business_id", AppUtils.getBusinessId(mActivity));
            hm.put("user_id", AppUtils.getUserId(mActivity));

            String url = JsonApiHelper.BASEURL + JsonApiHelper.UPDATE_PROFILE;
            new CommonAsyncTaskAquery(1, mActivity, this).getquery(url, hm);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        if (UserDashboard.currentFragment instanceof UserProfileFragment) {

        }
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.profile));
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


    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edtEmailId.getText().toString();
        String first_name = user_name.getText().toString();
        String business_name = edtbusinessname.getText().toString();
        String mobileno = edtmobilenumber.getText().toString();
        String profileDesc = edtProfileDescription.getText().toString();
        String website = edtWebsite.getText().toString();


        if (!first_name.equalsIgnoreCase("") && !emailaddress.equalsIgnoreCase("")
                && !mobileno.equalsIgnoreCase("")) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.mobileno_Length, Toast.LENGTH_SHORT).show();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (first_name.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.enterFirstName, Toast.LENGTH_SHORT).show();
            } else if (mobileno.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.enterPhone, Toast.LENGTH_SHORT).show();
            } else if (emailaddress.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.enterEmail, Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }


    protected void setFragment(Fragment fragment) {

        UserDashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, fragment, true);
    }

    private void submitRequest() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/trendi/api/switch-freelancer.php?user_id=200&user_role=3
            String url = JsonApiHelper.BASEURL + JsonApiHelper.SWITCH_FREELANCER + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&user_role=" + AppConstant.FREELANCER;
            new CommonAsyncTaskHashmap(11, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject jObject) {
        try {
            if (method == 1) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject data = commandResult.getJSONObject("data");

                    if (data.has("Business_profile")) {
                        JSONObject business_profile = data.getJSONObject("Business_profile");
                        if (business_profile.has("Id")) {
                            AppUtils.setBusinessId(mActivity, business_profile.getString("Id"));
                        }
                    }

                    mActivity.onBackPressed();

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 4) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    // JSONObject data = commandResult.getJSONObject("data");
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 2) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    edtWebsite.setText(data.getString("Website_Link"));
                    if (!data.getString("ProfilePic").equalsIgnoreCase("")) {
                        Picasso.with(mActivity).load(data.getString("ProfilePic")).placeholder(R.drawable.user).transform(new CircleTransform()).into(image_user);
                    }
                    user_name.setText(data.getString("Name"));
                    edtmobilenumber.setText(data.getString("Mobile"));
                    edtEmailId.setText(data.getString("Email"));
                    edtmobilenumber.setEnabled(false);
                    edtEmailId.setEnabled(false);

                    edtAddress.setText(data.getString("Address"));
                    //   edtProfileDescription.setText(data.getString("Address"));

                    if (data.getString("Gender").equalsIgnoreCase(AppConstant.MALE)) {
                        radio_male.setChecked(true);
                    } else {
                        radio_female.setChecked(true);
                    }
                    if (data.getString("IsVendor").equalsIgnoreCase("1")) {
                        freelancerCheckbox.setChecked(true);
                    } else {
                        freelancerCheckbox.setChecked(false);
                    }

                    JSONObject business_profile = data.getJSONObject("Business_profile");

                    if (business_profile.has("Id")) {
                        AppUtils.setBusinessId(mActivity, business_profile.getString("Id"));
                        freelancerCheckbox.setVisibility(View.GONE);
                        llFrelancer.setVisibility(View.VISIBLE);
                    }

                    if (business_profile.has("Name")) {
                        if (!business_profile.getString("Banner").equalsIgnoreCase("")) {
                            Picasso.with(mActivity).load(business_profile.getString("Banner")).into(image_user);
                        }
                        edtbusinessname.setText(business_profile.getString("Name"));
                        edtbusinessEmail.setText(business_profile.getString("Email"));
                        edtAbout.setText(business_profile.getString("About"));
                        edtbusinessDesc.setText(business_profile.getString("Description"));
                        edtbusinessAddress.setText(business_profile.getString("Address"));
                        edtlanguage.setText(business_profile.getString("Language"));
                        edtQualification.setText(business_profile.getString("Qualification"));
                    }

                    if (data.has("services")) {
                        String service = "", ServiceId = "";
                        JSONArray services = data.getJSONArray("services");
                        for (int i = 0; i < services.length(); i++) {
                            JSONObject object = services.getJSONObject(i);
                            if (service.equalsIgnoreCase("")) {
                                service = object.getString("ServiceName");
                                ServiceId = object.getString("ServiceId");
                            } else {
                                service = service + ", " + object.getString("ServiceName");
                                ServiceId = ServiceId + ", " + object.getString("ServiceId");
                            }

                        }
                        text_select_category.setText(service);
                        selectedserviceName = service;
                        selectedServiceId = ServiceId;
                    }


                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 11) {

                JSONObject commandResult = jObject.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Intent intent = new Intent(mActivity, VendorDashboard.class);
                    AppUtils.setUserRole(mActivity, AppConstant.FREELANCER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {

                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
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
