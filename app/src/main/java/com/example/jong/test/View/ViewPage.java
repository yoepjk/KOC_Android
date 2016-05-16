package com.example.jong.test.View;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.jong.test.Data.SaleItem;

import java.util.ArrayList;

public class ViewPage {
    protected View viewPage = null;
    protected MainActivity myActivity = null;

    public ViewPage(MainActivity activity, int rscLayoutId) {
        myActivity = activity;

        LayoutInflater inflater = LayoutInflater.from(myActivity.getApplicationContext());
        viewPage = inflater.inflate(rscLayoutId, null);
    }

    public View getViewPage() {
        return viewPage;
    }

    public void updatePage() { }
    public void updatePage(ArrayList<SaleItem> list) { }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
