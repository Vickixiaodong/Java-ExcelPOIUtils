package com.sasa.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by xiexiaodong on 16/3/18.
 * 读取xls格式的文件
 */
public class SASAReaderXLS {
    private static final int SHEET_INDEX = 0;

    private String fileName;
    private String packageName;

    private InputStream inputStream;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private List<String> excelTitle;
    private List<Map<String, String>> excelContent;
    private List excelRowObject;

    public SASAReaderXLS(InputStream inputStream) {

    }

    public SASAReaderXLS(String fileName, String packageName) {
        this.fileName = fileName;
        this.packageName = packageName;
        excelTitle = new ArrayList<String>();
        excelContent = new ArrayList<Map<String, String>>();
        excelRowObject = new ArrayList();
    }

    public void reader() {
        setFileNameInputStream();
        getWorkbook();
        getSheet();
        getExcelTitle();
        getExcelContent();
        getExcelExcelContentObject();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    /**
     * 通过文件名,得到流并返回流
     * @return
     */
    private void setFileNameInputStream() {
        if (null == inputStream) {
            try {
                inputStream = new FileInputStream(new File(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据inputStream得到workbook操作excel
     * @return
     */
    private void getWorkbook() {
        if (null == inputStream) {
            setFileNameInputStream();
        }
        try {
            if (null == workbook) {
                workbook = new HSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到sheet
     */
    private void getSheet() {
        if (null == workbook) {
            getWorkbook();
        }

        if (null == sheet) {
            sheet = workbook.getSheetAt(SHEET_INDEX);
        }
    }

    /**
     * 得到一个sheet的标题
     * @return
     */
    public List<String> getExcelTitle() {
        if (null == sheet) {
            getSheet();
        }

        HSSFRow row = sheet.getRow(0); // 得到标题行

        for (int cellnum = 0; cellnum < row.getLastCellNum(); ++cellnum) {
            HSSFCell cell = row.getCell(cellnum);

            excelTitle.add(getCellValue(cell));
        }

        return excelTitle;
    }

    /**
     * 得到非标题内容,[标题名, 对应值]...
     * @return
     */
    public List<Map<String, String>> getExcelContent() {
        if ((null == excelTitle) || (excelTitle.size() == 0) ) {
            getExcelTitle();
        }

        if (0 == sheet.getLastRowNum()) {
            return null;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); ++rowIndex) {
            HSSFRow row = sheet.getRow(rowIndex);
            for (int cellnum = 0; cellnum < row.getLastCellNum(); ++cellnum) {
                Map<String, String> cellKeyValue = getCellKeyValue(row.getCell(cellnum), cellnum);
                excelContent.add(cellKeyValue);
            }
        }

        return excelContent;
    }

    public List getExcelExcelContentObject() {
        if ((null == excelContent) || (excelContent.size() == 0)) {
            getExcelContent();
        }

        try {
            int flag = 0;
            Object object = Class.forName(packageName).newInstance();;
            int modFlag = excelTitle.size();
            for (Map<String, String> cellKeyValue : excelContent) {
                ++flag;
                Set set = cellKeyValue.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry) iterator.next();

                    // 通过反射执行setter方法
                    String methodName = FieldsCollector.toSetter(mapEntry.getKey().toString()); // setter方法名
                    String methodValue = mapEntry.getValue().toString(); // setter方法要设置的值
                    // 开始调用setter方法
                    Method method = Class.forName(packageName).getMethod(methodName, String.class);
                    method.invoke(object, methodValue);

                    if (0 == flag % modFlag) {
                        excelRowObject.add(object);
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

        return excelRowObject;
    }

    /**
     * 根据row得到这一行的所有值
     * @param row
     * @return
     */
    private List<String> getRowContent(HSSFRow row) {
        List<String> rowContent = null;

        if (null == excelTitle) {
            getExcelTitle();
        }

        if (null == row) {
            return null;
        }

        for (int cellnum = 0; cellnum < row.getLastCellNum(); ++cellnum) {
            Map<String, String> cellKeyValueMap = getCellKeyValue(row.getCell(cellnum), cellnum);
            rowContent.add(cellKeyValueMap.get(excelTitle.get(cellnum)));
        }

        return rowContent;
    }

    /**
     * 得到cell的键值对
     * @param cell
     * @param keyIndex
     * @return
     */
    private Map<String, String> getCellKeyValue(HSSFCell cell, int keyIndex) {
        Map<String, String> cellKeyValueMap = new HashMap<String, String>();

        if (null == cell) {
            return null;
        }

        String cellValue = getCellValue(cell);
        String cellKey = excelTitle.get(keyIndex);

        cellKeyValueMap.put(cellKey,cellValue);

        return cellKeyValueMap;
    }

    /**
     * 根据cell得到值value
     * @param cell
     * @return
     */
    private String getCellValue(HSSFCell cell) {
        String cellValue = null;

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:// 数字
                cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                break;
            default:
                cellValue = "null";
                break;
        }

        return cellValue;
    }
}
