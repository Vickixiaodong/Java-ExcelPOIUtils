package com.sasa.utils;

import com.oracle.tools.packager.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiexiaodong on 16/3/18.
 * 读取xls格式的文件
 */
public class SASAReadXLS {

    private static final int ROW_TYPE_TITLE = 0;
    private static final int ROW_TYPE_CONTENT = 1;

    /**
     * 通过文件名,进行xls格式文件的读取操作
     * @param fileName
     */
    public static List<List<List<Map<String, String>>>> readXLSByFileName(String fileName) {
        List<List<List<Map<String, String>>>> sheetList = new ArrayList<List<List<Map<String, String>>>>(); // 存放总数据的,包含多个sheet的数据
        String XLSContent = ""; // 存放所有读取到的数据,这里做测试用,您可以根据自己的需求进行改进
        InputStream fileNameInputStream = null;
        try {
            fileNameInputStream = new FileInputStream(new File(fileName));

            // 初始化整个Excel
            HSSFWorkbook workbook = new HSSFWorkbook(fileNameInputStream);
            // 循环获取workbook中的所有sheet
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); ++sheetIndex) { //-- start sheet --//
                // 根据sheetIndex获取sheet
                HSSFSheet sheet = workbook.getSheetAt(sheetIndex);

                // 输出sheet相关信息:序号(0开始),名称
                // System.out.println("----sheet序号:" + sheetIndex + ",sheet名称:" + workbook.getSheetName(sheetIndex) + "----");

                int rowType = ROW_TYPE_TITLE;
                boolean isAddRowList = false;
                List<String> titleList = new ArrayList<String>();
                // 存放单独row所有值
                List<List<Map<String, String>>> rowList = new ArrayList<List<Map<String, String>>>();
                // 循环该sheet中Row(行),对Row进行操作
                for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); ++rowIndex) { //-- start row --//
                    // 根据rowIndex得到row
                    HSSFRow row = sheet.getRow(rowIndex);

                    // 如果row为空,就继续循环,不进行row操作
                    if (null == row) {
                        continue;
                    }
                    List<Map<String, String>> cellList = new ArrayList<Map<String, String>>();

                    // 循环该行的每一个单元格cell
                    for (int cellnum = 0; cellnum <= row.getLastCellNum(); ++cellnum) { //-- start cell --//
                        // 根据cellnum得到cell
                        HSSFCell cell = row.getCell(cellnum);

                        String strCellContent = null;

                        if (null == cell) {
                            continue;
                        }

                        // 得到cell内容,根据类型进行字符串的转换
                        switch (cell.getCellType()) {
                            /**************************************
                             // 一些其他的类型,根据自己的需要进行功能完善~
                             case Cell.CELL_TYPE_BOOLEAN:
                             break;
                             case Cell.CELL_TYPE_ERROR:
                             break;
                             case Cell.CELL_TYPE_FORMULA:
                             break;
                             **************************************/
                            case Cell.CELL_TYPE_BLANK:  // 空
                                // strCellContent = "null";
                                // 这里是如果cell内容为空的时候,就什么都不做了,你也可以在这里添加一些你想做的事情
                                continue;
                            case Cell.CELL_TYPE_STRING: // 字符串
                                strCellContent = cell.getRichStringCellValue().getString();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:// 数字
                                strCellContent = NumberToTextConverter.toText(cell.getNumericCellValue());
                                break;
                            default:
                                strCellContent = "null";
                                break;
                        }

                        // 如果cell内容不为空,就继续循环,不进行cell操作
                        if (!strCellContent.equals("") && strCellContent.length() > 0) {
                            // 可以将读取的数据进行相应的操作,比如插入数据库,POJO存取等

                            // 如果是第一次,则读取的是title,加在这里是为了防止前几行没有数据
                            if (ROW_TYPE_TITLE == rowType) {
                                isAddRowList = false;
                                titleList.add(strCellContent); // 将title存到list中
                                XLSContent += "第" + rowIndex + "行--第" + cellnum + "列--标题为:" + strCellContent + "\n";
                            } else {
                                isAddRowList = true;
                                // System.out.println("第" + rowIndex + "行--第" + cellnum + "列--内容为:" + strCellContent);
                                XLSContent += "第" + rowIndex + "行--第" + cellnum + "列--内容为:" + strCellContent + "\n";

                                /* 可以通过titleList得到标题进行下一步操作了 */
                                // 将非标题title的值存到Map中
                                // 取出titleList中值

                                // 对应关系:Map<标题名, 值>
                                Map<String, String> map = new HashMap<String, String>();
                                map.put(titleList.get(cellnum),strCellContent);
                                // System.out.println("---name:" + titleList.get(cellnum) + ", value:" + strCellContent);
                                // 将这一行的数据存到cellList中
                                cellList.add(map);
                            }
                        }
                    } //-- end cell --//
                    rowType = ROW_TYPE_CONTENT;

                    if (isAddRowList) {
                        rowList.add(cellList);
                    }

                } // -- end row --//

                // sheet结束标记
                // System.out.println("----sheet结束----");

                sheetList.add(rowList);
            } //-- end sheet --//

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.debug("文件未找到!");
        } catch (IOException e) {
            e.printStackTrace();
            Log.debug("IO异常!");
        } finally {
            try {
                if (fileNameInputStream != null) {
                    fileNameInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.debug("IO异常!");
            }
        }

        // System.out.println(XLSContent); // 输出一下内容,测试用~

        return sheetList;
    }
}
