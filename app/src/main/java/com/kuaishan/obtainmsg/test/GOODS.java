package com.kuaishan.obtainmsg.test;

/**
 * Created by hong on 2016/11/14.
 */
public class GOODS {
    private String date;
    private String goods;
    private String price;
    private int imageId;
    public GOODS(String date,String goods,String price,int imageId) {
        this.date = date;
        this.goods = goods;
        this.price = price;
        this.imageId = imageId;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getGoods() {
        return goods;
    }
    public void setGoods(String goods) {
        this.goods = goods;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }



}
