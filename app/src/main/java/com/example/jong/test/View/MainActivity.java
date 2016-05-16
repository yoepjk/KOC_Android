package com.example.jong.test.View;

import android.app.Activity;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.jong.test.Util.ViewPagerAdapter;
import com.example.jong.test.R;
import com.example.jong.test.Data.Data;
import com.example.jong.test.Data.SaleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{
    // Tab 관련 변수 선언
    TabHost tabHost;
    View saleTabView, wantTabView, settingTabView;
    TextView saleTabTextView, wantTabTextView, settingTabTextView;
    // Tab Name
    private final String SALE_MENU = "할인 메뉴";
    private final String WANT_YOU_MENU = "원츄!메뉴";
    private final String SETTING = "설정";
    // Tab background color
    private final String ACTIVE_TAB = "#FF0000";
    private final String INACTIVE_TAB = "#FFA07A";
    // 상품목록 데이터 저장하는데 사용되는 변수
    private String urlString;

    private final String HOST_NAME = "http://192.168.0.14:8080/KOC/get_sale_items";

    // ViewPager 관련 변수 선언
    private ViewPager viewPager;
    private ViewPage salePage, wantYouPage, settingPage;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        initTabView();

        addTab(SALE_MENU, R.id.view1, saleTabView);
        addTab(WANT_YOU_MENU, R.id.view2, wantTabView);
        addTab(SETTING, R.id.view3, settingTabView);

        tabHost.setOnTabChangedListener(this);
        tabHost.setCurrentTab(0);

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        responseString();
        convertJson();

        initViewPager();
    }

    private void addTab(String tabName, int tabId, View indicator) {
        TabHost.TabSpec spec = tabHost.newTabSpec(tabName);
        spec.setContent(tabId);
        spec.setIndicator(indicator);
        tabHost.addTab(spec);
    }

    private void initTabView() {
        saleTabView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_view, null);
        saleTabTextView = (TextView)saleTabView.findViewById(R.id.tabViewTextView);
        saleTabTextView.setText(SALE_MENU);

        wantTabView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_view, null);
        wantTabTextView = (TextView)wantTabView.findViewById(R.id.tabViewTextView);
        wantTabTextView.setText(WANT_YOU_MENU);

        settingTabView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_view, null);
        settingTabTextView = (TextView)settingTabView.findViewById(R.id.tabViewTextView);
        settingTabTextView.setText(SETTING);

        saleTabView.setBackgroundColor(Color.parseColor(ACTIVE_TAB));
        wantTabView.setBackgroundColor(Color.parseColor(INACTIVE_TAB));
        settingTabView.setBackgroundColor(Color.parseColor(INACTIVE_TAB));
    }

    @Override
    public void onTabChanged(String tabId) {

        if (tabId.equals(SALE_MENU)) {
            saleTabView.setBackgroundColor(Color.parseColor(ACTIVE_TAB));
            wantTabView.setBackgroundColor(Color.parseColor(INACTIVE_TAB));
            settingTabView.setBackgroundColor(Color.parseColor(INACTIVE_TAB));
            viewPager.setCurrentItem(0);

        }else if(tabId.equals(WANT_YOU_MENU)) {
            saleTabView.setBackgroundColor(Color.parseColor(INACTIVE_TAB));
            wantTabView.setBackgroundColor(Color.parseColor(ACTIVE_TAB));
            settingTabView.setBackgroundColor(Color.parseColor(INACTIVE_TAB));
            viewPager.setCurrentItem(1);

        }else if(tabId.equals(SETTING)) {
            saleTabView.setBackgroundColor(Color.parseColor(INACTIVE_TAB));
            wantTabView.setBackgroundColor(Color.parseColor(INACTIVE_TAB));
            settingTabView.setBackgroundColor(Color.parseColor(ACTIVE_TAB));
            viewPager.setCurrentItem(2);
        }
    }

    // Web Server에서 상품 목록 데이터 받아오기
    private void responseString() {
        String urlstring = new String(HOST_NAME);

        try {
            URL url = new URL(urlstring);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            readStream(con.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 상품목록 데이터 추출
    private void readStream(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = "";
            String result= "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            urlString = new String(result);
//            Log.d("MyApp", urlString);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 상품목록 데이터 변환
    private void convertJson() {
        ArrayList<SaleItem> saleItemList = new ArrayList<SaleItem>();
        try {
            JSONArray jArray = new JSONArray(urlString);
            String no, name, price, conven, type, img_url;

            for (int i=0; i<jArray.length(); i++) {
                JSONObject arr = jArray.getJSONObject(i);
                no = String.valueOf(arr.getInt("no"));
                name = arr.getString("name");
                price = arr.getString("price");
                conven = arr.getString("convenience");
                type = arr.getString("type");
                img_url = arr.getString("img_url");

                SaleItem item = new SaleItem(no, name, price, conven, type, img_url);
                saleItemList.add(item);
            }

            Data.setSaleItems(saleItemList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // ViewPager 함수
    private void initViewPager()
    {
        salePage    = new SaleViewPage(this, R.layout.sale_view_page);
        wantYouPage = new WantViewPage(this, R.layout.want_view_page);
        settingPage = new SettingViewPage(this, R.layout.setting_view_page);

        viewPager = (ViewPager)findViewById(R.id.mainViewPager);
        adapter = new ViewPagerAdapter(salePage, wantYouPage, settingPage);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(int position)
    {
        if (position == 0 && SaleViewPage.newFavoriteItem == true)
            salePage.updatePage();

        // 키보드 입력기 숨기기
        ViewPage.hideSoftKeyboard(this, viewPager.getRootView());
        // 탭 연동
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
    @Override
    public void onPageScrollStateChanged(int state) { }

    public void startQuickVIew() {
        salePage.updatePage(Data.getSaleItems());
        tabHost.setCurrentTab(0);
        viewPager.setCurrentItem(0);
        SaleViewPage.newFavoriteItem = false;
        adapter.notifyDataSetChanged();
    }
}




























