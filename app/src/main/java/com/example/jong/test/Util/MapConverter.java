package com.example.jong.test.Util;

import android.util.Log;

import com.example.jong.test.Data.GeoTrans;
import com.example.jong.test.Data.GeoTransPoint;
import com.example.jong.test.Data.MapInfo;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.util.ArrayList;

public class MapConverter {

    public static int getDistanceMeter(NGeoPoint startP, NGeoPoint endP)
    {
        GeoTransPoint oMinGEO = new GeoTransPoint(startP.getLongitudeE6()/1E6, startP.getLatitudeE6()/1E6);
        GeoTransPoint oMaxGEO = new GeoTransPoint(endP.getLongitudeE6()/1E6, endP.getLatitudeE6()/1E6);
        double fDistance = GeoTrans.getDistancebyGeo(oMinGEO, oMaxGEO);
        int nDistance = (int)(fDistance * 1000);
        return nDistance;
    }

    public static MapInfo convertNGeoPoint(MapInfo mInfo) {
        GeoTransPoint oKA = new GeoTransPoint(mInfo.x, mInfo.y);
        GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
        Double lat = oGeo.getY() * 1E6;
        Double lng = oGeo.getX() * 1E6;
        NGeoPoint oLatLng = new NGeoPoint(lat.intValue(), lng.intValue());
        mInfo.latitude = oLatLng.getLatitude();
        mInfo.longitude = oLatLng.getLongitude();

        return mInfo;
    }



}
