package com.example.jong.test.Data;


import android.os.Parcel;
import android.os.Parcelable;

public class SaleItem implements Parcelable {
    private String no, name, price, convenience, type, img_url;
    private boolean highlighted = false;

    public SaleItem(String o, String n, String p, String c, String t, String img) {
        no = o;
        name = n;
        price = p;
        convenience = c;
        type = t;
        img_url = img;
    }
    public String getNo() { return  no;}
    public void setNo(String no) { this.no = no;}
    public String getName() { return  name;}
    public void setName(String name) { this.name = name;}
    public String getPrice() { return  price;}
    public void setPrice(String price) { this.price = price;}
    public String getConvenience() { return  convenience;}
    public void setConvenience(String convenience) { this.convenience = convenience;}
    public String getType() { return  type;}
    public void setType(String type) { this.type = type;}
    public String getImgurl() { return  img_url;}
    public void setImgurl(String img_url) { this.img_url = img_url;}
    public boolean getHighlighted() {return highlighted;}
    public void setHighlighted(boolean high) {highlighted = high;}

    public SaleItem(Parcel in){
        no = in.readString();
        name = in.readString();
        price = in.readString();
        convenience = in.readString();
        type = in.readString();
        img_url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(no);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(convenience);
        dest.writeString(type);
        dest.writeString(img_url);
    }

    public static final Parcelable.Creator<SaleItem> CREATOR = new Parcelable.Creator<SaleItem>(){
        public SaleItem createFromParcel(Parcel in){
            return new SaleItem(in);
        }

        public SaleItem[] newArray (int size){
            return new SaleItem[size];
        }
    };
}
