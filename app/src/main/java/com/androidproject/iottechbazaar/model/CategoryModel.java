package com.androidproject.iottechbazaar.model;

public class CategoryModel {
   public String id,name,cat_image;

    public CategoryModel(String id, String name, String cat_image) {
        this.id = id;
        this.name = name;
        this.cat_image = cat_image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCat_image() {
        return cat_image;
    }
}
