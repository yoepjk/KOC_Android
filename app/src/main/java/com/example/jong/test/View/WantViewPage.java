package com.example.jong.test.View;


import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jong.test.Util.ListAdapter;
import com.example.jong.test.R;
import com.example.jong.test.Data.Data;
import com.example.jong.test.Data.FavoriteItem;

public class WantViewPage extends ViewPage {
    ListView listView;
    ListAdapter adapter;
    EditText editText;
    Button addButton;

    public WantViewPage(MainActivity activity, int rscLayoutId) {
        super(activity, rscLayoutId);

        editText = (EditText)viewPage.findViewById(R.id.inputData);
        addButton = (Button)viewPage.findViewById(R.id.addButton);
        listView = (ListView)viewPage.findViewById(R.id.listView);
        adapter = new ListAdapter(myActivity);
        listView.setAdapter(adapter);

        final LinearLayout mainLayout = (LinearLayout)viewPage.findViewById(R.id.mainlayout);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(myActivity, v);
//                editText.clearFocus();
                return true;
            }
        });

        // 추가 버튼 눌렀을 때
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(myActivity, v);
                resetWantList();
                String new_word = editText.getText().toString();
                if (new_word.equals("") == false) {
                    if (isExisted(new_word))
                        Toast.makeText(myActivity, "리스트에 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                    else {
                        Data.addFavoriteItem(new_word);
                        adapter.notifyDataSetChanged();
                    }
                }
                SaleViewPage.newFavoriteItem = true;
                listView.setAdapter(adapter);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(myActivity, v);
                return false;
            }
        });

        // 리스트 뷰 아이템 클릭 했을 때
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteItem item = Data.getFavoriteItems().get(position);
                if (item.getClicked() == true) {
                    item.setClicked(false);
                    view.setBackgroundColor(Color.parseColor("#ffc8c8c8"));
                } else {
                    item.setClicked(true);
                    view.setBackgroundColor(Color.parseColor("#ffffffff"));
                }
                adapter.notifyDataSetChanged();
            }
        });

        Button removeChecked = (Button)viewPage.findViewById(R.id.removeChecked);
        Button removeAll = (Button)viewPage.findViewById(R.id.removeAll);

        // 선택 지우기 버튼 클릭 했을 때
        removeChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(myActivity, v);
                Data.removeSelectedItem();
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                SaleViewPage.newFavoriteItem = true;
            }
        });

        // 모두 지우기 버튼 클릭 했을 때
        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(myActivity, v);
                Data.removeAllItem();
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                SaleViewPage.newFavoriteItem = true;
            }
        });

        Button quickBtn = (Button)viewPage.findViewById(R.id.quickView);

        // 바로 보기 버튼 클릭 했을 때
        quickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(myActivity, v);
                resetWantList();
                // 원츄메뉴가 비어있으면 진행X
                if (Data.getFavoriteItems() == null || Data.getFavoriteItems().size() == 0)
                    return;
                SaleViewPage.newFavoriteItem = true;
                myActivity.startQuickVIew();
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });
    }

    public boolean isExisted(String new_word) {
        if (Data.getFavoriteItems() == null) {
            return false;
        }

        for (FavoriteItem item : Data.getFavoriteItems()) {
            if (item.getName().equals(new_word))
                return true;
        }
        return false;
    }

    private void resetWantList() {
        if (Data.getFavoriteItems() == null || Data.getFavoriteItems().size() == 0) {
            return;
        }

        for (int i=0; i<Data.getFavoriteItems().size(); i++) {
            FavoriteItem item = Data.getFavoriteItems().get(i);
            item.setClicked(false);
        }
    }


    @Override
    public View getViewPage() {
        return viewPage;
    }
}
















