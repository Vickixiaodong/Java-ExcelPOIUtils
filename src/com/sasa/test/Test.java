package com.sasa.test;

import com.oracle.tools.packager.Log;
import com.sasa.utils.SASADatabase;
import com.sasa.utils.SASAReaderXLS;
import com.sasa.utils.SASAWriteXLS;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("Running~~~");
        // testNewXLS();
        // testWriteXLS();
        /*
            将user.xls文件读取到程序中,这里保存在字符串中
            可以将其保存为VO对象进行其他操作,比如写入数据库中
         */

//        SASAReaderXLS readerXLS = new SASAReaderXLS("user.xls", "com.sasa.test.User");
//        List<User> usersList = readerXLS.getExcelExcelContentObject();
//        for (User user : usersList) {
//            System.out.println(user);
//        }
//
//        SASAReaderXLS readerXLS2 = new SASAReaderXLS("dog.xls", "com.sasa.test.Dog");
//        List<Dog> dogsList = readerXLS2.getExcelExcelContentObject();
//        for (Dog dog : dogsList) {
//            System.out.println(dog);
//        }

        // testWriteDB();

        // testInsertDB();

        System.out.println("Success~~~");
    }

    /**
     * testNewXLS
     * 测试创建一个workbook.xls文件在src目录下
     */
    public static void testNewXLS() {
        Workbook wb = new HSSFWorkbook();
        try {
            FileOutputStream fileOut = new FileOutputStream("workbook.xls");
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.debug("文件操作错误!");
        }
    }

    /**
     * testWriteXLS
     * 测试将POJO数据写入到xls文件
     * 可以将数据库中数据读取存到VO对象list中,进行写入xls操作
     */
    public static void testWriteXLS() {
        User user1 = new User("张三", "22", "我是猪");
        User user2 = new User("李四", "23", "我是狗");
        User user3 = new User("王五", "22", "我是鸡");
        User user4 = new User("孙六", "22", "我是猴");
        User user5 = new User("李八", "22", "我是羊");

        List<User> dataList = new ArrayList<User>();
        dataList.add(user1);
        dataList.add(user2);
        dataList.add(user3);
        dataList.add(user4);
        dataList.add(user5);

        SASAWriteXLS.writeXLSByFileName("user.xls", SASAWriteXLS.getDataList(dataList), "name", "age", "des");

        Dog dog1 = new Dog("大毛", "黄色");
        Dog dog2 = new Dog("二毛", "白色");
        Dog dog3 = new Dog("三狗", "黑白相间");
        Dog dog4 = new Dog("死狗", "黑色");
        Dog dog5 = new Dog("五狗", "花色");

        List<Dog> dogsList = new ArrayList<Dog>();
        dogsList.add(dog1);
        dogsList.add(dog2);
        dogsList.add(dog3);
        dogsList.add(dog4);
        dogsList.add(dog5);

        SASAWriteXLS.writeXLSByFileName("dog.xls", SASAWriteXLS.getDataList(dogsList), "name", "color");
    }

    /**
     * 测试从数据库中读取数据,导出到excel中,您只需要写sql语句,得到数据源后,调用方法即可~
     */
    public static void testWriteDB() {
        // 测试数据库
        SASADatabase database = new SASADatabase();
        // 测试读取
        String sql = "select * from dog";
        List list = database.formatList(sql, null, "com.sasa.test.Dog");
        System.out.println(list);
        SASAWriteXLS.writeXLSByFileName("dogfordb.xls", SASAWriteXLS.getDataList(list), "name", "color");

        String sql2 = "select * from user";
        List list2 = database.formatList(sql2, null, "com.sasa.test.User");
        System.out.println(list2);
        SASAWriteXLS.writeXLSByFileName("userfordb.xls", SASAWriteXLS.getDataList(list2), "name", "age", "des");

        System.out.println("请查看生成文件~");
    }

    public static void testInsertDB() {
        // 测试数据库
        SASADatabase database = new SASADatabase();

        Dog dog1 = new Dog("大毛", "黄色");
        Dog dog2 = new Dog("二毛", "白色");
        Dog dog3 = new Dog("三狗", "黑白相间");
        Dog dog4 = new Dog("死狗", "黑色");
        Dog dog5 = new Dog("五狗", "花色");

        List<Dog> dogsList = new ArrayList<Dog>();
        dogsList.add(dog1);
        dogsList.add(dog2);
        dogsList.add(dog3);
        dogsList.add(dog4);
        dogsList.add(dog5);

        database.insertObjects("dog", dogsList);

        User user1 = new User("张三", "22", "我是猪");
        User user2 = new User("李四", "23", "我是狗");
        User user3 = new User("王五", "22", "我是鸡");
        User user4 = new User("孙六", "22", "我是猴");
        User user5 = new User("李八", "22", "我是羊");

        List<User> dataList = new ArrayList<User>();
        dataList.add(user1);
        dataList.add(user2);
        dataList.add(user3);
        dataList.add(user4);
        dataList.add(user5);

        database.insertObjects("user", dataList);

        System.out.println("数据库已更新~");
    }
}
