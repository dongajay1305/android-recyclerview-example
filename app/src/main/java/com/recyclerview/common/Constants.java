package com.recyclerview.common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Constants {

    //public static final String IMAGE_URL_PATH = "https://image.tmdb.org/t/p/original";
    public static final String IMAGE_URL_PATH = "https://image.tmdb.org/t/p/w780";


    //Sharedprefrence
    public static final String PREF_NAME = "app";
    public static final String PREF_SORT = "sort";


    public static void showAboutApp(Context context){
        new AlertDialog.Builder(context)
                .setTitle("Leanagri Assignment")
                .setMessage("This app is for Leanagri assignment and developed by Jaykumar Donga as a part of interview process")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
