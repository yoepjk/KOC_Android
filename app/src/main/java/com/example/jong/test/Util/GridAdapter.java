package com.example.jong.test.Util;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jong.test.R;
import com.example.jong.test.Data.Data;
import com.example.jong.test.Data.FavoriteItem;
import com.example.jong.test.Data.SaleItem;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private Activity mActivity;
    public ArrayList<SaleItem> itemList;
//    private ViewHolder mHolder = null;

    public GridAdapter(Activity act) {
        mActivity = act;
        itemList = new ArrayList<SaleItem>();
        itemList.addAll(Data.getSaleItems());
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity.getApplicationContext());
            convertView = inflater.inflate(R.layout.sale_view, parent, false);

            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            holder.nameView = (TextView)convertView.findViewById(R.id.name);
            holder.priceView = (TextView)convertView.findViewById(R.id.price);
            holder.conView = (TextView)convertView.findViewById(R.id.convenience);
            holder.typeView = (TextView)convertView.findViewById(R.id.type);
//            holder.progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
            convertView.setTag(holder);
        }

        highlightView(convertView, position);
        holder = (ViewHolder)convertView.getTag();

        final ViewHolder mHolder = (ViewHolder)convertView.getTag();
        ImageDownloader.downloadImage_fromServer(itemList.get(position).getNo(), new ImageDownloader.IOnImageDownload() {
            @Override
            public void onImageDownload(Bitmap bmp) {
//                mHolder.progressBar.setVisibility(View.GONE);
//                mHolder.imageView.setVisibility(View.VISIBLE);
                mHolder.imageView.setImageBitmap(bmp);

            }


        }, new ImageDownloader.IOnImageDownload() {
            @Override
            public void onImageDownload(Bitmap bmp) {
//                mHolder.imageView.setVisibility(View.GONE);
//                mHolder.progressBar.setVisibility(View.VISIBLE);
                mHolder.imageView.setImageResource(R.drawable.load);
            }
        });

        holder.nameView.setText(itemList.get(position).getName());
        holder.priceView.setText(itemList.get(position).getPrice());
        holder.conView.setText(itemList.get(position).getConvenience());
        holder.typeView.setText(itemList.get(position).getType());

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView nameView, priceView, conView, typeView;
        ProgressBar progressBar;
    }

    public void updateView(ArrayList<SaleItem> list) {
        itemList.clear();
        itemList.addAll(list);

        searchFavoriteItem();

        this.notifyDataSetChanged();
    }


    public void searchFavoriteItem() {
        clearHighlighted();
        for (SaleItem item : itemList) {
            if (isFavoriteItem(item))
                item.setHighlighted(true);
        }
    }

    public void clearHighlighted() {
        for (SaleItem item : itemList)
            item.setHighlighted(false);
    }

    public boolean isFavoriteItem(SaleItem item) {
        if (Data.getFavoriteItems() == null)
            return false;

        for (FavoriteItem favor_item : Data.getFavoriteItems()) {
            if (item.getName().contains(favor_item.getName()))
                return true;
        }
        return false;
    }

    public void highlightView(View view, int position) {
        if (itemList.get(position).getHighlighted())
            view.setBackgroundColor(Color.parseColor("#FFFFFF00"));
        else
            view.setBackgroundColor(Color.parseColor("#00FF0000"));
    }

    public void refresh() {
        this.notifyDataSetChanged();
    }
}