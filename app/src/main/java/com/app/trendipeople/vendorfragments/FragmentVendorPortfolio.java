package com.app.trendipeople.vendorfragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.trendipeople.R;
import com.app.trendipeople.adapter.AdapterVendorPortfolio;
import com.app.trendipeople.aynctask.CommonAsyncTaskAquery;
import com.app.trendipeople.aynctask.CommonAsyncTaskHashmap;
import com.app.trendipeople.iclasses.HeaderViewManager;
import com.app.trendipeople.iclasses.InternalStorageContentProvider;
import com.app.trendipeople.interfaces.ApiResponse;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.interfaces.JsonApiHelper;
import com.app.trendipeople.interfaces.OnCustomItemClicListener;
import com.app.trendipeople.models.ModelCategory;
import com.app.trendipeople.userfragments.BaseFragment;
import com.app.trendipeople.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import eu.janmuller.android.simplecropimage.CropImage;

public class FragmentVendorPortfolio extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    private RecyclerView recycler_portfolio;
    private Activity mActivity;
    private FloatingActionButton fab_add;
    private AdapterVendorPortfolio adapterVendorPortfolio;
    private ArrayList<ModelCategory> arrayList;
    String path = "", selectedPath1 = "";
    private File mFileTemp, selectedFilePath;
    public static FragmentVendorPortfolio fragmentVendorPortfolio;
    private Bitmap bitmap = null;
    private Bundle b;
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 7;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    private ArrayList<ModelCategory> imagelist;
    private View view;
    private int deletePosition;
    private Spinner spinner_services;
    private View addImageView;
    private TextView text_add_image;
    private EditText edt_comment;
    private Button btn_submit;
    private String selectedServiceId = "";
    private ImageView image_cross;
    ArrayAdapter<String> adapterService;
    private ArrayList<String> serviceId = new ArrayList<>();
    private ArrayList<String> serviceName = new ArrayList<>();


    public static FragmentVendorPortfolio getInstance() {
        if (fragmentVendorPortfolio == null)
            fragmentVendorPortfolio = new FragmentVendorPortfolio();
        return fragmentVendorPortfolio;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        fragmentVendorPortfolio = this;
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();
        manageHeaderView();
        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(mActivity.getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        b = getArguments();
        arrayList = new ArrayList<>();
        imagelist = new ArrayList<>();

        spinner_services = (Spinner) view.findViewById(R.id.spinner_services);
        text_add_image = (TextView) view.findViewById(R.id.text_add_image);
        edt_comment = (EditText) view.findViewById(R.id.edt_comment);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        addImageView = view.findViewById(R.id.addImageView);
        recycler_portfolio = (RecyclerView) view.findViewById(R.id.recycler_portfolio);
        recycler_portfolio.setLayoutManager(new GridLayoutManager(mActivity, 2));
        fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add);
        image_cross = (ImageView) view.findViewById(R.id.image_cross);
        adapterVendorPortfolio = new AdapterVendorPortfolio(mActivity, this, imagelist);
        recycler_portfolio.setAdapter(adapterVendorPortfolio);

        setListener();
        getPortfolio();
        getServiceList();
    }

    private void getPortfolio() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            //http://dev.stackmindz.com/trendi/api/getGalleryList.php?user_id=200
            String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_GALLERY + "user_id=" + AppUtils.getUserId(mActivity);
            new CommonAsyncTaskHashmap(2, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    private void getServiceList() {
        // http://dev.stackmindz.com/trendi/api/userservice.php?user_id=200
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.USER_SRVICE + "user_id=" + AppUtils.getUserId(mActivity);
            new CommonAsyncTaskHashmap(11, mActivity, this).getqueryNoProgress(url);

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
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.portfolio));
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
                if (addImageView.getVisibility() == View.VISIBLE) {
                    addImageView.setVisibility(View.GONE);
                    return;
                } else {
                    mActivity.onBackPressed();
                }
            }

            @Override
            public void onClickOfHeaderRightView() {
                Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setListener() {
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageView.setVisibility(View.VISIBLE);
            }
        });

        image_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageView.setVisibility(View.GONE);
            }
        });

        text_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage1();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFilePath != null && !selectedServiceId.equalsIgnoreCase("")
                        && !edt_comment.getText().toString().equalsIgnoreCase("")) {
                    uploadPhoto();
                } else {
                    if (selectedServiceId.equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please select Service", Toast.LENGTH_SHORT).show();
                    } else if (selectedFilePath == null) {
                        Toast.makeText(mActivity, "Please select Image", Toast.LENGTH_SHORT).show();
                    } else if (edt_comment.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(mActivity, "Please enter Hashtag", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        spinner_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedServiceId = serviceId.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        Log.e("color upload 88888", +requestCode + "");
        switch (requestCode) {

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
                text_add_image.setText("Image Selected Successfully");
                //    uploadPhoto();
                //     profile_image.setImageBitmap(bitmap);
                break;


        }
    }

    public void uploadPhoto() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("gallery_image", selectedFilePath);
            hm.put("service_id", selectedServiceId);
            hm.put("comment", edt_comment.getText().toString());
            hm.put("user_id", AppUtils.getUserId(mActivity));

            // http://dev.stackmindz.com/trendi/api/addGallery.php?user_id=200&gallery_image=

            String url = JsonApiHelper.BASEURL + JsonApiHelper.ADD_GALLERY;
            new CommonAsyncTaskAquery(4, mActivity, FragmentVendorPortfolio.this).getquery(url, hm);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            deletePosition = position;
            deleteImage(position);
        }
    }

    private void deleteImage(int position) {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/trendi/api/deleteGallery.php?user_id=200&gallery_id=8
            String url = JsonApiHelper.BASEURL + JsonApiHelper.DELETE_GALLERY + "user_id=" + AppUtils.getUserId(mActivity)
                    + "&gallery_id=" + imagelist.get(position).getImageId();
            new CommonAsyncTaskHashmap(3, mActivity, this).getquery(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPostSuccess(int method, JSONObject jObject) {
        try {
            Log.e("onPostRequestSucess", "*" + method);
            if (method == 4) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    ModelCategory serviceDetail = new ModelCategory();
                    serviceDetail.setPorfolioImage(data.getString("galleryurl"));
                    serviceDetail.setImageId(data.getString("id"));
                    serviceDetail.setComment(edt_comment.getText().toString());

                    imagelist.add(serviceDetail);
                    adapterVendorPortfolio.notifyDataSetChanged();
                    addImageView.setVisibility(View.GONE);

                    edt_comment.setText("");
                    text_add_image.setText("Add Image");
                }
            } else if (method == 11) {
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    arrayList = new ArrayList<>();

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray services = data.getJSONArray("Services");
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);
                        serviceId.add(jo.getString("ServiceId"));
                        serviceName.add(jo.getString("ServiceName"));
                    }
                    adapterService = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, serviceName);
                    spinner_services.setAdapter(adapterService);
                }
            } else if (method == 2) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    imagelist.clear();
                    JSONArray images = data.getJSONArray("Gallery");
                    for (int i = 0; i < images.length(); i++) {

                        JSONObject jo = images.getJSONObject(i);
                        ModelCategory serviceDetail = new ModelCategory();
                        serviceDetail.setPorfolioImage(jo.getString("galleryurl"));
                        serviceDetail.setImageId(jo.getString("id"));
                        serviceDetail.setComment(jo.getString("comment"));

                        imagelist.add(serviceDetail);
                    }
                    adapterVendorPortfolio.notifyDataSetChanged();
                }
            } else if (method == 3) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    //   JSONObject data = commandResult.getJSONObject("data");
                    imagelist.remove(deletePosition);
                    adapterVendorPortfolio.notifyDataSetChanged();
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
