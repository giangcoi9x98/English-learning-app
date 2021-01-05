package com.example.project2.Models;

public  class CategoryModel {
    String name;
    String  color;
    String category;

    public CategoryModel(String name, String color,String category) {
        this.name = name;
        this.color = color;
        this.category=category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
