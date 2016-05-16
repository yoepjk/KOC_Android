package com.example.jong.test.Util;

import com.example.jong.test.Data.CategoryCheckbox;
import com.example.jong.test.Data.CheckboxArray;
import com.example.jong.test.Data.SaleItem;

import java.util.ArrayList;

public class SearchFilterFunctions {
    /// Event Search Interface
    interface ISearchFilter {
        boolean filter(SaleItem saleItem);
    }

    private ISearchFilter func_onePlusOne = new ISearchFilter() {
        @Override
        public boolean filter(SaleItem saleItem) {
            return saleItem.getType().equals("1+1");
        }
    };

    private ISearchFilter func_twoPlusOne = new ISearchFilter() {
        @Override
        public boolean filter(SaleItem saleItem) {
            return saleItem.getType().equals("2+1");
        }
    };

    private ISearchFilter func_CU = new ISearchFilter() {
        @Override
        public boolean filter(SaleItem saleItem) {
            return saleItem.getConvenience().equals("CU");
        }
    };

    private ISearchFilter func_GS = new ISearchFilter() {
        @Override
        public boolean filter(SaleItem saleItem) {
            return saleItem.getConvenience().equals("GS25");
        }
    };

    private ISearchFilter func_Seven = new ISearchFilter() {
        @Override
        public boolean filter(SaleItem saleItem) {
            return saleItem.getConvenience().equals("7Eleven");
        }
    };

    /// Keyword Search Interface
    interface IKeywordFilter {
        boolean filter(SaleItem saleItem);
    }

    // 체크박스에 체크된 항목만 찾아 filter에 사용할 수 있도록 string 배열로 만든다.
    public ArrayList<String> initCheckedFilterList(int idx) {
        // 카테고리 동기화
        categoryCheckboxUpdate();

        CategoryCheckbox[][] checkboxes = CheckboxArray.getCheckboxes();
        // 행사에 관련된 항목 추출
        ArrayList<String> filter_list = new ArrayList<String>();
        for (int j=0; j<4; j++) {
            if(checkboxes[idx][j].getisChecked())
                filter_list.add(checkboxes[idx][j].getName());
        }


        return filter_list;
    }

    // 체크박스가 체크되어 있는지를 확인하여 동기화한다.
    public void categoryCheckboxUpdate() {
        CategoryCheckbox[][] checkboxes = CheckboxArray.getCheckboxes();
        for(int i=0; i<2; i++) {
            for (int j=0; j<4; j++) {
                CategoryCheckbox cbox = checkboxes[i][j];
                cbox.setisChecked(cbox.getCheckBox().isChecked()) ;
            }
        }
    }

    private ArrayList<ISearchFilter> searchFilters = new ArrayList<ISearchFilter>();

    public ArrayList<SaleItem> search(ArrayList<SaleItem> sale_items, ArrayList<String> filter_list) {
        searchFilters.clear();

        if (filter_list.size() == 0)
            return sale_items;

        initSearchFilters(filter_list);

        ArrayList<SaleItem> items = new ArrayList<SaleItem>();
        for (SaleItem sItem : sale_items) {
            for (int i=0; i< searchFilters.size(); i++) {
                if(searchFilters.get(i).filter(sItem) == true) {
                    items.add(sItem);
                    break;
                }
            }
        }

        return items;
    }

    public void initSearchFilters(ArrayList<String> filter_list) {
        for (int i=0; i<filter_list.size(); i++) {
            switch (filter_list.get(i)) {
                case "1+1"      :  searchFilters.add(func_onePlusOne);  break;
                case "2+1"      :  searchFilters.add(func_twoPlusOne);  break;
                case "CU"       :  searchFilters.add(func_CU);            break;
                case "GS25"     :  searchFilters.add(func_GS);            break;
                case "7Eleven" :  searchFilters.add(func_Seven);         break;
            }
        }
    }

    public ArrayList<String> initKeywordFilterList(String string) {
        String[] arr = string.split(" ");
        ArrayList<String> filter_list = new ArrayList<>();
        for (int i=0; i<arr.length; i++) {
            filter_list.add(arr[i]);
        }
        return filter_list;
    }

    private ArrayList<IKeywordFilter> keywordFilters = new ArrayList<IKeywordFilter>();

    public ArrayList<SaleItem> searchByKeword(ArrayList<SaleItem> saleItems, ArrayList<String> filter_list) {
        keywordFilters.clear();

        for (final String filter : filter_list) {
            IKeywordFilter func_keyword = initKeywordFilter(filter);
            keywordFilters.add(func_keyword);
        }

        ArrayList<SaleItem> items = new ArrayList<SaleItem>();
        for (SaleItem sItem : saleItems) {
            for (IKeywordFilter func_keyword : keywordFilters) {
                if (func_keyword.filter(sItem) == true) {
                    items.add(sItem);
                    break;
                }
            }
        }
        return items;
    }

    public IKeywordFilter initKeywordFilter(final String word) {
        IKeywordFilter func_keyword = new IKeywordFilter() {
            String keyword = new String(word);
            @Override
            public boolean filter(SaleItem saleItem) {
                if (saleItem.getName().contains(keyword))
                    return true;
                else
                    return false;
            }
        };
        return func_keyword;
    }


}








