package com.app.trendipeople.controller;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.app.trendipeople.R;
import com.app.trendipeople.dialog.ErrorDialogFragment;


/**
 * A convenience class to handle displaying error dialogs.
 */
public class ErrorDialogHandler {

    FragmentManager mFragmentManager;

    public ErrorDialogHandler(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void showError(String errorMessage) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(
                R.string.validationErrors, errorMessage);
        fragment.show(mFragmentManager, "error");
    }
}
