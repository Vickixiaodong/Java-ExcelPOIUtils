package com.sasa.entity;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class FieldEntity {
    private String fieldname;

    private Object value;

    private Class clazz;

    public FieldEntity() {
        super();
    }

    public FieldEntity(String fieldname, Object value, Class clazz) {
        super();
        this.fieldname = fieldname;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "FieldEntity[fieldname=" + this.fieldname + ", value=" + this.value + ", clazz=" + "]";
    }

    public String getFieldname() {
        return this.fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
