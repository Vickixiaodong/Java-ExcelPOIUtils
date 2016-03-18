package com.sasa.test;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class User {
    private String name;
    private String age;
    private String desc;

    public User() {

    }

    public User(String name, String age, String desc) {
        this.name = name;
        this.age = age;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "User[name=" + this.name + ", age=" + this.age + ", desc=" + this.desc + "]";
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

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
