package com.example.backend.service.impl;

import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl {

    /**
     * 从 Excel 文件中导入数据
     * @param file 上传的 Excel 文件
     * @param type 数据类型的 Class 对象
     * @param <T> 泛型类型
     * @return 包含导入数据的列表
     * @throws IOException 读取文件时可能抛出的异常
     * @throws NoSuchMethodException 反射调用构造函数时可能抛出的异常
     * @throws IllegalAccessException 反射访问字段时可能抛出的异常
     * @throws InvocationTargetException 反射调用方法时可能抛出的异常
     * @throws InstantiationException 实例化对象时可能抛出的异常
     */
    public <T> List<T> importExcel(MultipartFile file, Class<T> type) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<T> dataList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Field[] fields = type.getDeclaredFields();
        Constructor<T> constructor = type.getConstructor();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            T instance = constructor.newInstance();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                field.setAccessible(true);
                Cell cell = row.getCell(j);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            field.set(instance, cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                field.set(instance, cell.getDateCellValue());
                            } else {
                                double numericValue = cell.getNumericCellValue();
                                if (field.getType() == int.class || field.getType() == Integer.class) {
                                    field.set(instance, (int) numericValue);
                                } else if (field.getType() == double.class || field.getType() == Double.class) {
                                    field.set(instance, numericValue);
                                }
                            }
                            break;
                        case BOOLEAN:
                            field.set(instance, cell.getBooleanCellValue());
                            break;
                        default:
                            field.set(instance, "");
                    }
                }

            }
            dataList.add(instance);
        }
        workbook.close();
        inputStream.close();
        return dataList;
    }

    /**
     * 将数据列表写入 Excel 文件
     * @param dataList 包含要写入数据的列表
     * @param type 数据类型的 Class 对象
     * @param <T> 泛型类型
     * @return 包含写入数据的 Workbook 对象
     * @throws IllegalAccessException 反射访问字段时可能抛出的异常
     */
    public <T> Workbook writeToExcel(List<?> dataList, Class<?> type) throws IllegalAccessException {
        // 校验数据列表类型
        if (dataList.size() == 0) {
            throw new ServiceException(ServiceCode.ERR_UNKNOWN, "列表为空");
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        Row headerRow = sheet.createRow(0);
        Field[] fields = type.getDeclaredFields();

        // 写入表头
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(field.getName());
        }

        // 修改后的写入逻辑
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Object item = dataList.get(i);

            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                Cell cell = row.createCell(j);
                Object value;

                if (item instanceof Map) {  // 处理Map类型数据
                    value = ((Map<?,?>) item).get(field.getName()); // 根据字段名获取值
                } else {  // 处理普通JavaBean
                    field.setAccessible(true);
                    value = field.get(item);
                }

                if (value != null) {
                    cell.setCellValue(value.toString());
                }
            }
        }


        /**
         * todo 测试文件是否正常保存
         */
        String filePath = "D:\\file\\test.xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return workbook;
    }
}