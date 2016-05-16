package com.example.jong.test.Util;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.jong.test.View.ViewPage;


public class ViewPagerAdapter extends PagerAdapter {
    public ViewPage[] viewPages = null;

    public ViewPagerAdapter(ViewPage... pages){

        super();

        viewPages = pages;
    }

    @Override
    public int getCount() {
        return viewPages.length;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View viewPage = viewPages[position].getViewPage();

        // viewPage의 parent가 남아있는 버그 해결
        ViewGroup parent = (ViewGroup)viewPage.getParent();
        if(parent != null)
            parent.removeView(viewPage);

        container.addView(viewPage);

        return viewPage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View)view);
    }

    @Override
    public boolean isViewFromObject(View pager, Object obj) {
        return pager == obj;
    }


}
