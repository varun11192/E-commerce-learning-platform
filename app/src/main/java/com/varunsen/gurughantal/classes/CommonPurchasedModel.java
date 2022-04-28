package com.varunsen.gurughantal.classes;

public class CommonPurchasedModel {

    private String productTitle;
    private String productSubtitle;
    private String productImage;
    public int productType;
    public String productId;

    public CommonPurchasedModel(String productTitle, String productSubtitle, String productImage, int productType, String productId) {
        this.productTitle = productTitle;
        this.productSubtitle = productSubtitle;
        this.productImage = productImage;
        this.productId = productId;
        this.productType = productType;
    }

    public CommonPurchasedModel(int productType, String productId) {
        this.productType = productType;
        this.productId = productId;
    }

    public CommonPurchasedModel(String title, String subtitle, String image, String productId, int intValue) {
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public void setProductSubtitle(String productSubtitle) {
        this.productSubtitle = productSubtitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
