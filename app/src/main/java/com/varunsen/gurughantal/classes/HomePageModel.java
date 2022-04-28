package com.varunsen.gurughantal.classes;

import java.util.List;

public class HomePageModel {

    public static final int BANNER = 0;
    public static final int HORIZONTAL = 1;
    public static final int GRID = 2;

    public int type;
    public String layoutTitle;
    public List<HorizontalItemModel> horizontalList;
    public String bgColor;
    public List<ViewAllEeModel> viewAllEeModelList;
    public List<ViewAllEeModel> viewAllEeGridList;
    public List<String> postersList;
    public List<String> productList;

    public HomePageModel(int type, String layoutTitle, List<String> productList) {
        this.type = type;
        this.layoutTitle = layoutTitle;
        this.productList = productList;

    }

    public HomePageModel(int type, String layoutTitle, String bgColor, List<String> productList) {
        this.type = type;
        this.layoutTitle = layoutTitle;
        this.bgColor = bgColor;
        this.productList = productList;
    }

    public HomePageModel(int type, List<String> postersList) {
        this.type = type;
        this.postersList = postersList;
    }

    public List<String> getPostersList() {
        return postersList;
    }

    public void setPostersList(List<String> postersList) {
        this.postersList = postersList;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLayoutTitle() {
        return layoutTitle;
    }

    public void setLayoutTitle(String layoutTitle) {
        this.layoutTitle = layoutTitle;
    }

    public List<HorizontalItemModel> getHorizontalList() {
        return horizontalList;
    }

    public void setHorizontalList(List<HorizontalItemModel> horizontalList) {
        this.horizontalList = horizontalList;
    }

    public List<ViewAllEeModel> getViewAllEeModelList() {
        return viewAllEeModelList;
    }

    public void setViewAllEeModelList(List<ViewAllEeModel> viewAllEeModelList) {
        this.viewAllEeModelList = viewAllEeModelList;
    }

    public List<ViewAllEeModel> getViewAllEeGridList() {
        return viewAllEeGridList;
    }

    public void setViewAllEeGridList(List<ViewAllEeModel> viewAllEeGridList) {
        this.viewAllEeGridList = viewAllEeGridList;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }
}
