package com.sasa.utils;

import com.oracle.tools.packager.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.io.*;

/**
 * Created by xiexiaodong on 16/3/18.
 * 读取xls格式的文件
 */
public class SASAReadXLS {
    /**
     * 通过文件名,进行xls格式文件的读取操作
     * @param fileName
     */
    public static String readXLSByFileName(String fileName) {
        String XLSContent = null; // 存放所有读取到的数据,这里做测试用,您可以根据自己的需求进行改进
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

                // 循环该sheet中Row(行),对Row进行操作
                for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); ++rowIndex) { //-- start row --//
                    // 根据rowIndex得到row
                    HSSFRow row = sheet.getRow(rowIndex);

                    // 如果row为空,就继续循环,不进行row操作
                    if (null == row) {
                        continue;
                    }

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
                            // System.out.println("第" + rowIndex + "行--第" + cellnum + "列--内容为:" + strCellContent);
                            XLSContent += "第" + rowIndex + "行--第" + cellnum + "列--内容为:" + strCellContent + "\n";
                        }

                    } //-- end cell --//

                }// -- end row --//

                // sheet结束标记
                // System.out.println("----sheet结束----");

            }//-- end sheet --//

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

        return XLSContent;
    }
}
