package com.example.jong.test.Util;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jong.test.R;
import com.example.jong.test.Data.Data;

public class ListAdapter extends BaseAdapter {
    private Activity mActivity;

    public ListAdapter(Activity act) {
        mActivity = act;
    }

    @Override
    public int getCount() {
        if (Data.getFavoriteItems() == null)
            return 0;
        return Data.getFavoriteItems().size();
    }

    @Override
    public Object getItem(int position) {
        return Data.getFavoriteItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity.getApplicationContext());
            convertView = inflater.inflate(R.layout.want_list_view, parent, false);

            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.wantItem);
            convertView.setTag(holder);
        }

        holder = (ViewHolder)convertView.getTag();
        holder.textView.setText(Data.getFavoriteItems().get(position).getName());

        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}


