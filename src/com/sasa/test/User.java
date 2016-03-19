package com.sasa.test;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class User {
    private String name;
    private String age;
    private String des;

    public User() {

    }

    public User(String name, String age, String des) {
        this.name = name;
        this.age = age;
        this.des = des;
    }

    @Override
    public String toString() {
        return "User[name=" + this.name + ", age=" + this.age + ", desc=" + this.des + "]";
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

    public String getDes() {
        return this.des;
    }

    public void setDes(String desc) {
        this.des = desc;
    }
}
