package com.varunsen.gurughantal.classes;

public class ViewAllEeModel {

    public String imageResource;
    public String lecNo, watch, price, oldPrice, productId;

    public ViewAllEeModel(String productId, String imageResource, String lecNo, String watch, String price, String oldPrice) {
        this.imageResource = imageResource;
        this.lecNo = lecNo;
        this.watch = watch;
        this.price = price;
        this.oldPrice = oldPrice;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getLecNo() {
        return lecNo;
    }

    public void setLecNo(String lecNo) {
        this.lecNo = lecNo;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }
}