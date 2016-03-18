package com.sasa.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class CollectionToObjectsList {

    /**
     * 将Map嵌套集合,转换成对象List
     * @param objectMapListListList 源数据
     * @param objectName        要转换成的对象名称
     * @return
     */
    public static List convert(List<List<List<Map<String, String>>>> objectMapListListList, String objectName) {
        List objectList = new ArrayList();

        for (List<List<Map<String, String>>> objectMapListList : objectMapListListList) {
            for (List<Map<String, String>> objectMapList : objectMapListList) {
                // 根据objectName通过反射机制创建实例
                try {
                    Object object = Class.forName(objectName).newInstance();
                    // Map<属性名, 属性值>
                    for (Map<String, String> objectMap : objectMapList) {
                        Set set = objectMap.entrySet();
                        Iterator iterator = set.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry mapEntry = (Map.Entry) iterator.next();

                            // 通过反射执行setter方法
                            String methodName = FieldsCollector.toSetter(mapEntry.getKey().toString()); // setter方法名
                            String methodValue = mapEntry.getValue().toString(); // setter方法要设置的值
                            // 开始调用setter方法
                            Method method = Class.forName(objectName).getMethod(methodName, String.class);
                            method.invoke(object, methodValue);
                        }
                    }
                    // 将这个对象添加到list中
                    objectList.add(object);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }

        return objectList;
    }
}
