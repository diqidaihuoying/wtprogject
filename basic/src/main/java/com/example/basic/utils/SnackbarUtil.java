package com.example.basic.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by ${wantao} on 2016/8/23.
 */
public class SnackbarUtil {
    public static void showSnackBar(View coordinatorLayout,String text)
    {
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT).show();
    }

}
