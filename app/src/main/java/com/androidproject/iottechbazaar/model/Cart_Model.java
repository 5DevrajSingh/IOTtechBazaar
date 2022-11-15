package com.androidproject.iottechbazaar.model;

public class Cart_Model {
 public String id,sku_code,category,product_name,actual_price,cell_price,discount,offer,key_feauture,specification,overview,description,img,img1,img2,wish,stock,quantity,total_price,showPrice,showActualPrice,total_actal_price;

 public boolean isSelected;

 public Cart_Model(String id, String sku_code, String category, String product_name, String actual_price, String cell_price, String discount, String offer, String key_feauture, String specification, String overview, String description, String img, String img1, String img2, String wish, String stock, String quantity, String total_price, String showPrice, String showActualPrice, String total_actal_price, boolean isSelected) {
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
  this.stock = stock;
  this.quantity = quantity;
  this.total_price = total_price;
  this.showPrice = showPrice;
  this.showActualPrice = showActualPrice;
  this.total_actal_price = total_actal_price;
  this.isSelected = isSelected;
 }

 public String getTotal_actal_price() {
  return total_actal_price;
 }

 public void setTotal_actal_price(String total_actal_price) {
  this.total_actal_price = total_actal_price;
 }

 public String getShowActualPrice() {
  return showActualPrice;
 }

 public void setShowActualPrice(String showActualPrice) {
  this.showActualPrice = showActualPrice;
 }

 public String getShowPrice() {
  return showPrice;
 }

 public void setQuantity(String quantity) {
  this.quantity = quantity;
 }

 public void setShowPrice(String showPrice) {
  this.showPrice = showPrice;
 }

 public void setSelected(boolean selected) {
  isSelected = selected;
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

 public String getStock() {
  return stock;
 }

 public String getQuantity() {
  return quantity;
 }

 public String getTotal_price() {
  return total_price;
 }

 public boolean isSelected() {
  return isSelected;
 }
}
