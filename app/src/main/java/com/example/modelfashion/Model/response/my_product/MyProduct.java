package com.example.modelfashion.Model.response.my_product;

import java.util.ArrayList;

public class MyProduct {
    private String id;
    private String product_name;
    private String description;
    private String price;
    private String cost;
    private String type;
    private String subtype;
    private ArrayList<Sizes> sizes;
    private ArrayList<String> photos;

    public MyProduct(String id, String product_name, String description, String price, String cost, String type, String subtype, ArrayList<Sizes> sizes, ArrayList<String> photos) {
        this.id = id;
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.cost = cost;
        this.type = type;
        this.subtype = subtype;
        this.sizes = sizes;
        this.photos = photos;
    }

    public MyProduct() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public ArrayList<Sizes> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Sizes> sizes) {
        this.sizes = sizes;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "MyProduct{" +
                "id='" + id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", cost='" + cost + '\'' +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                ", sizes=" + sizes +
                ", photos=" + photos +
                '}';
    }
}

