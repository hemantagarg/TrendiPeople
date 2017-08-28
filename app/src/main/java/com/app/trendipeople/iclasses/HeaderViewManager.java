package com.app.trendipeople.iclasses;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.trendipeople.R;
import com.app.trendipeople.interfaces.HeaderViewClickListener;
import com.app.trendipeople.utils.AppUtils;


public class HeaderViewManager {

    /**
     * Instance of this class
     */
    public static HeaderViewManager mHeaderManagerInstance;
    /**
     * Debugging TAG
     */
    private String TAG = HeaderViewManager.class.getSimpleName();

    /**
     * Header View Instance
     */
    private Toolbar toolbar;
    private TextView textHeaderTitle;
    private ImageView headerLeftImage, headerRightImage;

    /**
     * Instance of Header View Manager
     *
     * @return
     */
    public static HeaderViewManager getInstance() {
        if (mHeaderManagerInstance == null) {
            mHeaderManagerInstance = new HeaderViewManager();
        }

        return mHeaderManagerInstance;
    }

    /**
     * Initialize Header View
     *
     * @param mActivity
     * @param mView
     * @param headerViewClickListener
     */
    public void InitializeHeaderView(Activity mActivity, View mView,
                                     HeaderViewClickListener headerViewClickListener) {
        if (mActivity != null) {
            toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);

            textHeaderTitle = (TextView) mActivity.findViewById(R.id.textHeaderTitle);

            headerLeftImage = (ImageView) mActivity.findViewById(R.id.headerLeftImage);
            headerRightImage = (ImageView) mActivity.findViewById(R.id.headerRightImage);
        } else if (mView != null) {
            toolbar = (Toolbar) mView.findViewById(R.id.toolbar);

            textHeaderTitle = (TextView) mView.findViewById(R.id.textHeaderTitle);

            headerLeftImage = (ImageView) mView.findViewById(R.id.headerLeftImage);
            headerRightImage = (ImageView) mView.findViewById(R.id.headerRightImage);
        }
        manageClickOnViews(headerViewClickListener);
    }


    /**
     * ManageClickOn Header view
     *
     * @param headerViewClickListener
     */
    private void manageClickOnViews(
            final HeaderViewClickListener headerViewClickListener) {
        // Click on Header Left View
        headerLeftImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                headerViewClickListener.onClickOfHeaderLeftView();
            }
        });
        // Click on Header Right View
        headerRightImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                headerViewClickListener.onClickOfHeaderRightView();
            }
        });
    }

    /**
     * Set Heading View Text
     *
     * @param isVisible
     * @param headingStr
     */
    public void setHeading(boolean isVisible, String headingStr) {
        if (textHeaderTitle != null) {
            if (isVisible) {
                textHeaderTitle.setVisibility(View.VISIBLE);
                textHeaderTitle.setText(headingStr);
            } else {
                textHeaderTitle.setVisibility(View.GONE);
            }
        } else {
            Log.e(TAG,
                    "Header Heading Text View is null");
        }
    }

    /**
     * Manage Header Left View
     *
     * @param isVisibleImage
     * @param ImageId
     */
    public void setLeftSideHeaderView(boolean isVisibleImage,
                                      int ImageId) {
        if (!isVisibleImage) {
            headerLeftImage.setVisibility(View.INVISIBLE);
        } else if (headerLeftImage == null) {
            AppUtils.showErrorLog(TAG, "Header Left View is null");
        } else if (isVisibleImage) {

            headerLeftImage.setVisibility(View.VISIBLE);
            if (ImageId > 0) {
                headerLeftImage.setImageResource(ImageId);
            } else {
                AppUtils.showErrorLog(TAG,
                        "Header left image id is null");
            }
        }

    }

    /**
     * Set Header Right Side View
     *
     * @param isVisibleImage
     * @param ImageId
     */
    public void setRightSideHeaderView(boolean isVisibleImage,
                                       int ImageId) {
        if (!isVisibleImage) {
            headerRightImage.setVisibility(View.INVISIBLE);
        } else if (headerRightImage == null) {
            AppUtils.showErrorLog(TAG, "Header Right View is null");
        } else if (isVisibleImage) {
            headerRightImage.setVisibility(View.VISIBLE);
            if (ImageId > 0) {
                headerRightImage.setImageResource(ImageId);
            } else {
                headerRightImage.setVisibility(View.GONE);
            }

        }
    }


    /**
     * Method used to enable disable right header view
     *
     * @param isEnabled
     */
    public void setHeaderRightViewEnabled(boolean isEnabled) {
        if (isEnabled) {
            headerRightImage.setEnabled(true);
        } else {
            headerRightImage.setEnabled(false);
        }
    }

    /**
     * Method used to change the header background color
     *
     * @param color
     */
    public void setHeaderBackgroundColor(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }
}

