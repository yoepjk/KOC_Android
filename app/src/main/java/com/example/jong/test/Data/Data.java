package com.example.jong.test.Data;

import java.util.ArrayList;

public class Data {
    private static ArrayList<SaleItem> saleItems;
    private static ArrayList<FavoriteItem> favoriteItems;

    public static ArrayList<SaleItem> getSaleItems() {
        return saleItems;
    }

    public static void setSaleItems(ArrayList<SaleItem> items) {
        if (saleItems == null)
            saleItems = new ArrayList<SaleItem>();
        saleItems.clear();
        saleItems.addAll(items);
    }

    public static ArrayList<FavoriteItem> getFavoriteItems() { return favoriteItems;}

    public static void addFavoriteItem(String new_word) {
        if (favoriteItems == null)
            favoriteItems = new ArrayList<FavoriteItem>();

        FavoriteItem item = new FavoriteItem(new_word);
        favoriteItems.add(item);
    }

    public static void removeSelectedItem() {
        for (int i=favoriteItems.size()-1; i > -1; i--) {
            if (favoriteItems.get(i).getClicked() == true)
                favoriteItems.remove(i);
        }
    }

    public static void removeAllItem() { favoriteItems.clear();}


}
