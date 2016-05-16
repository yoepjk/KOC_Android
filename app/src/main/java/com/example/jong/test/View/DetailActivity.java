package com.example.jong.test.View;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jong.test.Data.MapInfo;
import com.example.jong.test.R;
import com.example.jong.test.Util.ImageDownloader;
import com.example.jong.test.Data.SaleItem;
import com.example.jong.test.Util.NMapCalloutBasicOverlay;
import com.example.jong.test.Util.NMapPOIflagType;
import com.example.jong.test.Util.NMapViewerResourceProvider;
import com.example.jong.test.Util.SearchArea;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

public class DetailActivity extends NMapActivity implements NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener,
        NMapOverlayManager.OnCalloutOverlayListener, NMapPOIdataOverlay.OnStateChangeListener {

    private static final String API_KEY = "cfcce79b42bb1802810a1ee83b7eff99";
    NMapController mMapController;
    NMapLocationManager mMapLocationManager;
    NMapMyLocationOverlay mMyLocationOverlay;
    NMapView mMapView;
    LinearLayout mapContainer;
    NMapCompassManager mMapCompassManager;
    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapOverlayManager mOverlayManager;

    private String cur_addr;
    private NGeoPoint startPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        Intent intent = getIntent();
        SaleItem item = intent.getExtras().getParcelable("item");

        final ImageView imageView = (ImageView) findViewById(R.id.image);
        initTextView(item.getConvenience(), R.id.convenience);
        initTextView(item.getType(), R.id.type);
        initTextView(item.getName(), R.id.name);
        initTextView(item.getPrice(), R.id.price);

        ImageDownloader.downloadImage_fromServer(item.getNo(), new ImageDownloader.IOnImageDownload() {
            @Override
            public void onImageDownload(Bitmap bmp) {
                imageView.setImageBitmap(bmp);
            }


        }, new ImageDownloader.IOnImageDownload() {
            @Override
            public void onImageDownload(Bitmap bmp) {
                imageView.setImageResource(R.drawable.load);
            }
        });

        mapContainer = (LinearLayout) findViewById(R.id.mainlayout);
        mMapView = new NMapView(this);
        mMapView.setApiKey(API_KEY);
        mMapView.setClickable(true);
        mMapView.setOnMapStateChangeListener(this);
        mMapView.setOnMapViewTouchEventListener(this);
        mMapView.setBuiltInZoomControls(false, null);
        super.setMapDataProviderListener(onDataProviderListener);
        mMapController = mMapView.getMapController();
        mapContainer.addView(mMapView);
    }

    public void initTextView(String text, int id) {
        TextView view = (TextView) findViewById(id);
        view.setText(text);
    }


    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        // success
        if (nMapError == null) {
            mMapView.setVisibility(View.INVISIBLE);
            startMyLocation();

        }else { // fail
            android.util.Log.e("NMAP", "onMapInitHandler: error=" + nMapError.toString());
            return;
        }
    }

    private void startMyLocation() {
        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);

        if (!isMyLocationEnabled) {
            Toast.makeText(
                    this,
                    "Please enable a My Location source in system settings",
                    Toast.LENGTH_LONG).show();

            Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(goToSettings);
            finish();

        } else {
        }
    }

    private void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);
                mMapCompassManager.disableCompass();
                mMapView.setAutoRotateEnabled(false, false);
                mapContainer.requestLayout();
            }
        }
    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

            if (errInfo != null) {
                Log.e("myLog", "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());
            }else{
                mMapView.setVisibility(View.VISIBLE);

                cur_addr = placeMark.toString();
                Toast.makeText(getApplicationContext(), cur_addr, Toast.LENGTH_SHORT).show();
                ArrayList<MapInfo> mapInfos = SearchArea.getMapInfoList(cur_addr, startPoint);
                if (mapInfos.size() > 0)
                    settingPOI(mapInfos);

                else {
                    mMapController.setMapCenter(startPoint);
                    Toast.makeText(getApplicationContext(),
                            "현재 위치에서 편의점을 찾을 수 없습니다.",
                            Toast.LENGTH_LONG).show();
                }

            }
        }
    };

    public void settingPOI(ArrayList<MapInfo> mapInfos) {
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        int markerId = NMapPOIflagType.PIN;     // 여러 오버레이 아이템을 하나의 오버레이 객체에서 관리

        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);
        for (MapInfo mInfo : mapInfos)
            poiData.addPOIitem(mInfo.latitude, mInfo.longitude, mInfo.title, markerId, 0);

        poiData.endPOIdata();

        NMapPOIdataOverlay poIdataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poIdataOverlay.showAllPOIdata(0);
        poIdataOverlay.setOnStateChangeListener(this);
        mOverlayManager.setOnCalloutOverlayListener(this);
    }

    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager,
                                         NGeoPoint myLocation) {

            startPoint = myLocation;
            //위도경도를 주소로 변환
            try {
                findPlacemarkAtLocation(myLocation.getLongitude(), myLocation.getLatitude());
            }catch (Exception e) {
                Log.e("map error", e.getMessage());
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
            Toast.makeText(getApplicationContext(),
                    "Your current location is temporarily unavailable.",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(
                NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(getApplicationContext(),
                    "Your current location is unavailable area.",
                    Toast.LENGTH_LONG).show();

            stopMyLocation();
        }

    };

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Override
    public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onLongPressCanceled(NMapView nMapView) {

    }

    @Override
    public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {

    }

    @Override
    public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {

    }

    @Override
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay nMapOverlay, NMapOverlayItem nMapOverlayItem, Rect rect) {
        return new NMapCalloutBasicOverlay(nMapOverlay, nMapOverlayItem, rect);
    }

    @Override
    public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

    }

    @Override
    public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
//        Toast.makeText(getApplicationContext(), nMapPOIitem.getTitle(), Toast.LENGTH_SHORT).show();
    }


}