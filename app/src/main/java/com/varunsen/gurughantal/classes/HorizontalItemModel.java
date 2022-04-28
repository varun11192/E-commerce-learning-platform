package com.varunsen.gurughantal.classes;

public class HorizontalItemModel {

    public String imageResource;
    public String title, subtitle, price, productId;

    public HorizontalItemModel(String productId,String imageResource, String title, String subtitle, String price) {
        this.imageResource = imageResource;
        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
