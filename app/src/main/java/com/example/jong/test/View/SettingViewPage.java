package com.example.jong.test.View;


import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.jong.test.R;

public class SettingViewPage extends ViewPage implements View.OnClickListener {
    ToggleButton alarmToggle;
    TextView syncView, infoView, manualView;
    public SettingViewPage(MainActivity activity, int rscLayoutId) {
        super(activity, rscLayoutId);

        alarmToggle = (ToggleButton)viewPage.findViewById(R.id.alarmToggle);
        alarmToggle.setOnClickListener(this);
        syncView = (TextView)viewPage.findViewById(R.id.appSync);
        infoView = (TextView)viewPage.findViewById(R.id.info);
        infoView.setOnClickListener(this);
        manualView = (TextView)viewPage.findViewById(R.id.manual);
        manualView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.alarmToggle:
                if (alarmToggle.isChecked())
                    alarmToggle.setTextColor(Color.parseColor("#FF0000"));
                else
                    alarmToggle.setTextColor(Color.parseColor("#000000"));
                break;

            case R.id.info:
                intent = new Intent(myActivity.getApplicationContext(), InfoActivity.class);
                myActivity.startActivity(intent);
                break;

            case R.id.manual:
                intent = new Intent(myActivity.getApplicationContext(), ManualActivity.class);
                myActivity.startActivity(intent);
                break;
        }
    }
}
