package com.sasa.utils;

import com.oracle.tools.packager.Log;
import com.sasa.entity.FieldEntity;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiexiaodong on 16/3/18.
 */
public class SASAWriteXLS {
    /**
     * 根据名字,将data数据写入这个名字的xls文件中
     * @param fileName 文件名
     * @param dataList 数据源,可以是对象的List
     * @param rowName excel第一行的名字,必须和dataList中对象属性一致
     */
    public static void writeXLSByFileName(String fileName, List<Map<String, FieldEntity>> dataList, String... rowName) {
        OutputStream fileNameOutputStream = null;
        try {
            fileNameOutputStream = new FileOutputStream(new File(fileName));

            // 初始化一个workbook
            HSSFWorkbook workbook = new HSSFWorkbook();

            // 这里默认创建一个sheet,可以使用循环创建多个sheet,安排好数据存储方式就好
            String sheetName = "sheet";
            HSSFSheet sheet = workbook.createSheet(sheetName);

            // 创建行标题
            HSSFRow rowTitle = sheet.createRow(0);
            for (int cellnum = 0; cellnum < rowName.length; ++cellnum) {
                Cell cell = rowTitle.createCell(cellnum);
                cell.setCellValue(rowName[cellnum]);
            }

            // 创建行row,这里一行是一个数据对象
            for (int rowIndex = 0; rowIndex < dataList.size(); ++rowIndex) {
                // 创建索引号为rowIndex的row
                HSSFRow row = sheet.createRow(rowIndex + 1);
                // 得到一个对象
                Map<String, FieldEntity> map = dataList.get(rowIndex);

                // 创建列cell
                for (int cellnum = 0; cellnum < rowName.length; ++cellnum) {
                    Cell cell = row.createCell(cellnum);
                    cell.setCellValue(map.get(rowName[cellnum]).getValue().toString());
                    // System.out.println("cell name:" + cell.getStringCellValue());
                }
            }
            // 将数据表写入文件~
            workbook.write(fileNameOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fileNameOutputStream) {
                try {
                    fileNameOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.debug("写入文件成功~~~");
        }
    }

    /**
     * 把对象List转换成特殊格式Map<String, FieldEntity>,
     * Map保存着这个对象的属性名和属性值
     * @param objectList
     * @return
     */
    public static List<Map<String, FieldEntity>> getDataList(List objectList) {
        List<Map<String, FieldEntity>> dataList = new ArrayList<Map<String, FieldEntity>>();

        for (Object o : objectList) {
            try {
                dataList.add(FieldsCollector.getFields(o));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return dataList;
    }

    // 将list<Map<String, Object>>数据转换成list<Object>
}
