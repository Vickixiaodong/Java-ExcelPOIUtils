package com.sasa.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * Created by xiexiaodong on 16/3/19.
 */
public class SASADatabase {

    private static final String DPropertierFileName = "resource/database.properties";
    private int columnCount = 0;

    private Properties databaseProperties = null; // 数据库配置信息文件

    /* 数据库配置相关信息 */
    private String driver = null;
    private String url = null;
    private String username = null;
    private String password = null;

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public SASADatabase() {
        initProperties();
        initDatabaseSetting();
        try {
            Class.forName(driver); // 加载对应驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化配置文件
     */
    private void initProperties() {
        databaseProperties = new Properties();
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(DPropertierFileName));
            databaseProperties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据库配置信息
     */
    private void initDatabaseSetting() {
        driver = databaseProperties.getProperty("driver");
        url = databaseProperties.getProperty("url");
        username = databaseProperties.getProperty("username");
        password = databaseProperties.getProperty("password");
    }

    /**
     * 建立数据库连接
     * @return 数据库连接
     */
    private Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * SQL 查询将查询结果直接放入ResultSet中
     * @param sql           SQL语句
     * @param params        参数数组,若没有参数则为null
     * @return              结果集
     */
    private ResultSet executeQueryRS(String sql, Object[] params) {
        // 得到连接
        connection = this.getConnection();

        try {
            // 调用sql
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数,没有参数则为null
            if (null != params) {
                for (int i = 0; i < params.length; ++i) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }

            // 执行
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * 将查询结果保存到List中
     * @param sql
     * @param params
     * @return
     */
    public List<Object> executeQuery(String sql, Object[] params) {
        // 执行SQL获得结果集
        ResultSet rs = executeQueryRS(sql, params);

        // 创建ResultSetMetaData对象
        ResultSetMetaData rsmd = null;

        // 结果集列数
        columnCount = 0;
        try {
            rsmd = rs.getMetaData();
            // 获得结果集列数
            columnCount = rsmd.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Object> list = new ArrayList<Object>();
        try {
            // 将ResultSet的结果保存到List中
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; ++i) {
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }

        return list;
    }

    /**
     * 将List转换成ReaderXLS可用的List数据源
     * @param sql
     * @param params
     * @param packageName
     * @return
     */
    public List formatList(String sql, Object[] params, String packageName) {
        List<Object> list = executeQuery(sql, params);
        List formatList = new ArrayList<>();

        Object object = null;
        int flag = 0;
        int modFlag = columnCount;
        try {
            object = Class.forName(packageName).newInstance();
            for (int i = 0; i < list.size(); ++i) {
                Map<String, Object> map = (Map<String, Object>) list.get(i);
                Set set = map.entrySet();

                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    ++flag;
                    Map.Entry mapEntry = (Map.Entry) iterator.next();

                    // 通过反射执行setter方法
                    String methodName = FieldsCollector.toSetter(mapEntry.getKey().toString()); // setter方法名
                    String methodValue = mapEntry.getValue().toString(); // setter方法要设置的值

                    // 开始调用setter方法
                    Method method = Class.forName(packageName).getMethod(methodName, String.class);
                    method.invoke(object, methodValue);

                    if (0 == flag % modFlag) {
                        formatList.add(object);
                        object = Class.forName(packageName).newInstance();
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return formatList;
    }

    /**
     * 根据表名和对象,得到拼装后的sql,然后进行插入数据库操作
     * @param tableName
     * @param object
     * @return
     */
    private void insertOneObject(String tableName, Object object) {
        Class clazz = object.getClass(); // 得到这个类型
        Field[] fields = clazz.getDeclaredFields(); // 获取这个object的所有属性:name,age,...
        List<String> fieldName = new ArrayList<String>(); // 存放属性名字
        List<String> fieldValue = new ArrayList<String>(); // 属性值
        for (int i = 0; i < fields.length; ++i) {
            String name = fields[i].getName();
            fieldName.add(name);
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            try {
                Method method = clazz.getMethod("get" + name);
                String value = (String) method.invoke(object);
                fieldValue.add(value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // 得到属性名数组和属性值数组了
        // 装配sql
        String sql = "insert into " + tableName + " (";
        for (int i = 0; i < fieldName.size(); ++i) {
            sql += fieldName.get(i);
            if (i < fieldName.size() - 1) {
                sql += ", ";
            } else {
                sql += ") ";
            }
        }
        sql += "values (";
        for (int i = 0; i < fieldName.size(); ++i) {
            sql += "?";
            if (i < fieldName.size() - 1) {
                sql += ", ";
            } else {
                sql += ") ";
            }
        }

        Connection conn = getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            for (int i = 0; i < fieldValue.size(); ++i) {
                pstmt.setString(i + 1, fieldValue.get(i));
            }
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据源插入到数据库中
     * @param tableName 数据库表名
     * @param objectList 数据源
     */
    public void insertObjects(String tableName, List objectList) {
        for (Object object: objectList) {
            insertOneObject(tableName, object);
        }
    }

    /**
     * 关闭资源
     */
    private void closeAll() {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != preparedStatement) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
