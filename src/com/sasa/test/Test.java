package com.sasa.test;

import com.oracle.tools.packager.Log;
import com.sasa.utils.SASAReadXLS;
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
    public static void main(String[] args) {
        System.out.println("Running~~~");
        // testNewXLS();
        testWriteXLS();
        /*
            将user.xls文件读取到程序中,这里保存在字符串中
            可以将其保存为VO对象进行其他操作,比如写入数据库中
         */
        System.out.println(SASAReadXLS.readXLSByFileName("user.xls"));
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
        User user1 = new User("张三", "22");
        User user2 = new User("李四", "23");
        User user3 = new User("王五", "22");

        List<User> dataList = new ArrayList<User>();
        dataList.add(user1);
        dataList.add(user2);
        dataList.add(user3);

        SASAWriteXLS.writeXLSByFileName("user.xls", SASAWriteXLS.getDataList(dataList), "name", "age");
    }
}
