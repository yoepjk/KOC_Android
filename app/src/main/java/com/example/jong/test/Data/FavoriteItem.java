package com.example.jong.test.Data;

public class FavoriteItem {
    private String name;
    private boolean clicked;

    public FavoriteItem(String str) {
        name = str;
        clicked = false;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean getClicked() { return clicked; }
    public void setClicked(boolean clicked) { this.clicked = clicked; }
}