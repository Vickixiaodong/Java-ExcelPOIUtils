package com.sasa.test;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class User {
    private String name;
    private String age;

    public User() {

    }

    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User[name=" + this.name + ", age=" + this.age + "]";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
