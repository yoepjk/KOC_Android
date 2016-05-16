package com.example.jong.test.Data;

import android.widget.CheckBox;

public class CategoryCheckbox {
    private String name;
    private int id;
    private boolean isChecked;
    private CheckBox checkBox;

    public CategoryCheckbox(String n, int i) {
        name = n;
        id = i;
        isChecked=false;
    }

    public String getName() { return  name;}
    public void setName(String name) { this.name = name;}
    public int getId() { return id;}
    public void setId(int id) { this.id = id;}
    public boolean getisChecked() { return isChecked;}
    public void setisChecked(boolean isChecked) { this.isChecked = isChecked;}
    public CheckBox getCheckBox() {return checkBox;}
    public void setCheckBox(CheckBox checkBox) { this.checkBox = checkBox;}

}
