package com.example.jong.test.View;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.jong.test.Util.ExpandCollapseAnimation;
import com.example.jong.test.Util.GridAdapter;
import com.example.jong.test.R;
import com.example.jong.test.Data.CategoryCheckbox;
import com.example.jong.test.Data.CheckboxArray;
import com.example.jong.test.Data.Data;
import com.example.jong.test.Data.FavoriteItem;
import com.example.jong.test.Data.SaleItem;
import com.example.jong.test.Util.SearchFilterFunctions;

import java.util.ArrayList;

public class SaleViewPage extends ViewPage implements CompoundButton.OnCheckedChangeListener {

    GridView gridView;
    GridAdapter adapter;
    EditText searchText;
    private ArrayList<SaleItem> itemLIst = Data.getSaleItems();
    private boolean active = true;
    private SearchFilterFunctions searchFilterFunctions = new SearchFilterFunctions();

    public static boolean newFavoriteItem = false;

    public SaleViewPage(MainActivity activity, int rscLayoutId) {
        super(activity, rscLayoutId);
        myActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // 체크박스 배열 초기화
        initCategoryCheckbox();

        // 검색 입력 창
        searchText = (EditText)viewPage.findViewById(R.id.inputData);
        final LinearLayout mainLayout = (LinearLayout)viewPage.findViewById(R.id.mainlayout);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(myActivity, v);
                searchText.clearFocus();
                return true;
            }
        });

        // 검색 클릭
        final Button searchBtn = (Button)viewPage.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(myActivity, v);

                // 키워드로 검색
                String txt = searchText.getText().toString();
                ArrayList<String> sList = searchFilterFunctions.initKeywordFilterList(txt);
                itemLIst = searchFilterFunctions.searchByKeword(Data.getSaleItems(), sList);
                // 체크박스 체크된 거로 검색
                for (int i=0; i<2; i++) {
                    sList = searchFilterFunctions.initCheckedFilterList(i);
                    itemLIst = searchFilterFunctions.search(itemLIst, sList);
                }
                adapter.updateView(itemLIst);
                gridView.setAdapter(adapter);
            }
        });

        final LinearLayout search_layout = (LinearLayout) viewPage.findViewById(R.id.searchLayout);

        Button button = (Button) viewPage.findViewById(R.id.up_down_btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftKeyboard(myActivity, v);
                ExpandCollapseAnimation animation = null;
                if(active) {
                    animation = new ExpandCollapseAnimation(search_layout, 200, 1);
                    active = false;
                } else {
                    animation = new ExpandCollapseAnimation(search_layout, 200, 0);
                    active = true;
                }
                search_layout.startAnimation(animation);
            }
        });

        // 그리드 뷰 추가
        gridView = (GridView) viewPage.findViewById(R.id.gridView);
        adapter = new GridAdapter(myActivity);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftKeyboard(myActivity, view);
                Intent intent = new Intent(myActivity.getApplicationContext(), DetailActivity.class);
                intent.putExtra("item", (SaleItem)itemLIst.get(position));
                myActivity.startActivity(intent);
            }
        });
    }

    public void initCategoryCheckbox() {
        CategoryCheckbox allEvent = createCategoryCheckbox("allEvent", R.id.allEvent);
        CategoryCheckbox onePoneEvent = createCategoryCheckbox("1+1", R.id.onePoneEvent);
        CategoryCheckbox twoPoneEvent = createCategoryCheckbox("2+1", R.id.twoPoneEvent);
        CategoryCheckbox bonusEvent = createCategoryCheckbox("bonusEvent", R.id.bonusEvent);
        CategoryCheckbox allConvenience = createCategoryCheckbox("allConvenience", R.id.allConvenience);
        CategoryCheckbox gs25 = createCategoryCheckbox("GS25", R.id.gs25);
        CategoryCheckbox seveneleven = createCategoryCheckbox("7Eleven", R.id.seveneleven);
        CategoryCheckbox cu = createCategoryCheckbox("CU", R.id.cu);

        CategoryCheckbox[][] checkboxes = new CategoryCheckbox[2][];
        checkboxes[0] = new CategoryCheckbox[] {allEvent, onePoneEvent, twoPoneEvent, bonusEvent};
        checkboxes[1] = new CategoryCheckbox[] {allConvenience, gs25, seveneleven, cu};

        CheckboxArray.setCheckboxes(checkboxes);
    }

    public CategoryCheckbox createCategoryCheckbox(String name, int id) {
        CategoryCheckbox cbox = new CategoryCheckbox(name, id);
        cbox.setCheckBox(createCheckbox(id));
        return cbox;
    }

    public CheckBox createCheckbox(int id) {
        CheckBox checkBox = (CheckBox)viewPage.findViewById(id);
        checkBox.setOnCheckedChangeListener(this);
        return checkBox;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        hideSoftKeyboard(myActivity, buttonView);
        switch(buttonView.getId()){
            case R.id.allEvent:
                allClickCheckbox(0, isChecked);
                break;
            case R.id.allConvenience:
                allClickCheckbox(1, isChecked);
                break;
        }
    }

    public void allClickCheckbox(int idx, boolean isChecked) {
        CategoryCheckbox[][] checkboxes = CheckboxArray.getCheckboxes();
        checkboxes[idx][1].getCheckBox().setChecked(isChecked);
        checkboxes[idx][2].getCheckBox().setChecked(isChecked);
        checkboxes[idx][3].getCheckBox().setChecked(isChecked);
    }


    public void updatePage(ArrayList<SaleItem> list) {
        // 체크박스 해제
        allClickCheckbox(0, false); allClickCheckbox(1, false);

        // filter 리스트 생성
        ArrayList<String> filter_list = initFilterList();

        // 검색 입력창에 filter 문자열 출력
        String edit_text = filterListToString(filter_list);
        searchText.setText(edit_text);

        // 키워드로 검색
        ArrayList<SaleItem> items = searchFilterFunctions.searchByKeword(list, filter_list);
        adapter.updateView(items);
        gridView.setAdapter(adapter);
    }

    public ArrayList<String> initFilterList() {
        ArrayList<String> filter_list = new ArrayList<String>();
        for (FavoriteItem item : Data.getFavoriteItems())
            filter_list.add(item.getName());

        return filter_list;
    }

    public String filterListToString(ArrayList<String> filter_list) {
        String edit_text = new String();
        for (String text : filter_list)
            edit_text += text + " ";

        return edit_text;
    }


    @Override
    public void updatePage() {
        // 리스트 갱신 및 bmp 다운로드 생략
        if (Data.getFavoriteItems() == null || Data.getFavoriteItems().size() == 0) {
            searchText.setText("");
            adapter.updateView(Data.getSaleItems());
            gridView.setAdapter(adapter);
        }
        adapter.searchFavoriteItem();
        adapter.notifyDataSetChanged();
        newFavoriteItem = false;
    }

    @Override
    public View getViewPage() {
        return viewPage;
    }

}