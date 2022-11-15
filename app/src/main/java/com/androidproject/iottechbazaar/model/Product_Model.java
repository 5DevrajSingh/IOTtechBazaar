package com.androidproject.iottechbazaar.model;

public class Product_Model {
    public String id,sku_code,category,product_name,actual_price,cell_price,discount,offer,key_feauture,specification,overview,description,img,img1,img2;

    String wish;

    public Product_Model(String id, String sku_code, String category, String product_name, String actual_price, String cell_price, String discount, String offer, String key_feauture, String specification, String overview, String description, String img, String img1, String img2, String wish) {
        this.id = id;
        this.sku_code = sku_code;
        this.category = category;
        this.product_name = product_name;
        this.actual_price = actual_price;
        this.cell_price = cell_price;
        this.discount = discount;
        this.offer = offer;
        this.key_feauture = key_feauture;
        this.specification = specification;
        this.overview = overview;
        this.description = description;
        this.img = img;
        this.img1 = img1;
        this.img2 = img2;
        this.wish = wish;
    }

    public String getId() {
        return id;
    }

    public String getSku_code() {
        return sku_code;
    }

    public String getCategory() {
        return category;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getActual_price() {
        return actual_price;
    }

    public String getCell_price() {
        return cell_price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getOffer() {
        return offer;
    }

    public String getKey_feauture() {
        return key_feauture;
    }

    public String getSpecification() {
        return specification;
    }

    public String getOverview() {
        return overview;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public String getImg1() {
        return img1;
    }

    public String getImg2() {
        return img2;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }
}
