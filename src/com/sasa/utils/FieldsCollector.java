package com.sasa.utils;

import com.sasa.entity.FieldEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class FieldsCollector {
    public static Map<String, FieldEntity> getFields(Object object) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, FieldEntity> map = new HashMap<String, FieldEntity>();

        Class clazz = object.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; ++i) {
            Object resultObject = invokeMethod(object, fields[i].getName(), null);
            map.put(fields[i].getName(), new FieldEntity(fields[i].getName(), resultObject, fields[i].getType()));
        }
        return map;
    }

    public static Object invokeMethod(Object owner, String fieldname, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class ownerClass = owner.getClass();

        Method method = null;
        method = ownerClass.getMethod(toGetter(fieldname));
        Object object = null;
        object = method.invoke(owner);

        return object;
    }

    public static String toGetter(String fieldname) {
        if (fieldname == null || fieldname.length() == 0) {
            return null;
        }
        /* If the second char is upper, make 'get' + field name as getter name. For example, eBlog -> geteBlog */
        if (fieldname.length() > 2) {
            String second = fieldname.substring(1, 2);
            if (second.equals(second.toUpperCase())) {
                return new StringBuffer("get").append(fieldname).toString();
            }
        }
        /* Common situation */
        fieldname = new StringBuffer("get").append(fieldname.substring(0, 1).toUpperCase()).append(fieldname.substring(1)).toString();
        return  fieldname;
    }

    public static String toSetter(String fieldname) {
        if (fieldname == null || fieldname.length() == 0) {
            return null;
        }
        /* If the second char is upper, make 'get' + field name as getter name. For example, eBlog -> geteBlog */
        if (fieldname.length() > 2) {
            String second = fieldname.substring(1, 2);
            if (second.equals(second.toUpperCase())) {
                return new StringBuffer("set").append(fieldname).toString();
            }
        }
        /* Common situation */
        fieldname = new StringBuffer("set").append(fieldname.substring(0, 1).toUpperCase()).append(fieldname.substring(1)).toString();
        return  fieldname;
    }
}
