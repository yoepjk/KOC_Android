package com.example.jong.test.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ImageDownloader {
    static private HashMap<String, Bitmap> image_dict_ = new HashMap<String, Bitmap>();

    static public String HOST_NAME = "http://192.168.0.14:8080/KOC/get_img";

    ImageDownloader() {
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public interface IOnImageDownload {
        public void onImageDownload(Bitmap bmp);
    }

    static public void downloadImage_fromServer(String no, final IOnImageDownload onDownload, final IOnImageDownload doing) {
        String url = HOST_NAME + "?no=" + no;
        // already exists
        if (image_dict_.containsKey(url)) {
            if (onDownload != null)
                onDownload.onImageDownload(image_dict_.get(url));
            return;
        }

        downloadImage(url, onDownload, doing);
    }

    static public void downloadImage(String url, final IOnImageDownload onDownload, final IOnImageDownload doing) {

        AsyncTask<String, Void, Bitmap> task = new AsyncTask<String, Void, Bitmap>() {
            String link;

            @Override
            protected void onPreExecute() {
                doing.onImageDownload(null);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    link = params[0];
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    connection.setReadTimeout(30000);
                    connection.setInstanceFollowRedirects(true);

                    InputStream input = connection.getInputStream();
                    return BitmapFactory.decodeStream(input);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (isCancelled()) {
                    bitmap = null;
                }

                if (onDownload != null) {
                    onDownload.onImageDownload(bitmap);
                    image_dict_.put(link, bitmap);
                }
            }
        };
        task.execute(url);
    }
}
