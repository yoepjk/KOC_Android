package com.example.jong.test.Data;


public class CheckboxArray {
    private static CategoryCheckbox[][] checkboxes;

    public static CategoryCheckbox[][] getCheckboxes() { return checkboxes; }

    public static void setCheckboxes(CategoryCheckbox[][] cboxes) {
        checkboxes = cboxes.clone();
    }
}
