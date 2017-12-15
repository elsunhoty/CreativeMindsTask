package com.tmoo7.creativemindstask.Helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by othello on 12/15/2017.
 */

public class MyProgressDialog {

    public static ProgressDialog CustomProgressDialog(Context context)
    {
        ProgressDialog  progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        return progressDialog;

    }
}
