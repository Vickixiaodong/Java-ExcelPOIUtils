package com.sasa.test;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class Dog {
    private String name;
    private String color;

    public Dog() {

    }

    public Dog(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return "User[name=" + this.name + ", color=" + this.color + "]";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
