package com.varunsen.gurughantal.classes;

public class NotificationModelList {

    String fruitName;
    int qty;

    public NotificationModelList(String fruitName, int qty) {
        this.fruitName = fruitName;
        this.qty = qty;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
