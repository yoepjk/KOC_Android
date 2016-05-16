package com.example.jong.test.Util;

import android.os.StrictMode;
import android.util.Log;

import com.example.jong.test.Data.MapInfo;
import com.nhn.android.maps.maplib.NGeoPoint;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class SearchArea {

    public static ArrayList<MapInfo> getMapInfoList(String cur_addr, NGeoPoint startPoint) {
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            ArrayList<MapInfo> mapInfos = xmlParsing(responseString(cur_addr));

            return mapInfoListFilter(mapInfos, startPoint);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<MapInfo> mapInfoListFilter(ArrayList<MapInfo> maps, NGeoPoint startPoint) {
        ArrayList<MapInfo> filteredList = new ArrayList<MapInfo>();

        maps = convert(maps);
        NGeoPoint startP = startPoint;
        for (MapInfo mInfo : maps) {
            NGeoPoint endP = new NGeoPoint(mInfo.latitude, mInfo.longitude);
            if (MapConverter.getDistanceMeter(startP, endP) <= 450) {
                filteredList.add(mInfo);
            }
        }

        return filteredList;
    }

    public static ArrayList<MapInfo> convert(ArrayList<MapInfo> maps) {
        for (MapInfo mInfo : maps) {
            mInfo = MapConverter.convertNGeoPoint(mInfo);
            Log.d("My", "lat : " + mInfo.latitude + " lng : " + mInfo.longitude);
        }
        return maps;
    }

    // Web Server에서 상품 목록 데이터 받아오기
    private static String responseString(String cur_addr) {
        String result = null;
        try {
            URL url = new URL("http://openapi.naver.com/search?"
                    + "key=62224d50b61cbdcc0a78865dca215667"
//                    + "&query=" + URLEncoder.encode("월계동 편의점", "UTF-8")
                    + "&query=" + URLEncoder.encode(cur_addr+"편의점", "UTF-8")
                    + "&target=local&start=1&display=10");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            result = readStream(con.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    // 상품목록 데이터 추출
    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        String urlString = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = "";
            String result= "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }

            urlString = new String(result);


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
            return urlString;
        }
    }


    public static ArrayList<MapInfo> xmlParsing(String xml) throws ParserConfigurationException, IOException, SAXException {
        InputSource source = new InputSource(new StringReader(xml));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);
        XPath xPath = XPathFactory.newInstance().newXPath();

        ArrayList<MapInfo> maps = new ArrayList<MapInfo>();
        try {
            NodeList list = (NodeList) xPath.evaluate("//item/title", document, XPathConstants.NODESET);
            int len = list.getLength();
            for (int i=0; i<len; i++) {
                MapInfo map = new MapInfo();
                map.title = list.item(i).getFirstChild().getTextContent();
                maps.add(map);
            }

            list = (NodeList) xPath.evaluate("//item/mapx", document, XPathConstants.NODESET);
            for (int i=0; i<len; i++)
                maps.get(i).x = Integer.parseInt(list.item(i).getFirstChild().getTextContent());

            list = (NodeList) xPath.evaluate("//item/mapy", document, XPathConstants.NODESET);
            for (int i=0; i<len; i++)
                maps.get(i).y = Integer.parseInt(list.item(i).getFirstChild().getTextContent());

            return maps;

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return null;
    }





}
