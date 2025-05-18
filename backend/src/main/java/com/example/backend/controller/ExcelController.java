package com.example.backend.controller;

import com.example.backend.common.R;
import com.example.backend.service.impl.ExcelServiceImpl;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelServiceImpl excelService;

    /**
     * 处理 Excel 文件导入请求
     *
     * @param file      上传的 Excel 文件
     * @param className 数据类型的全限定名
     * @return 包含导入数据的列表
     * @throws IOException               读取文件时可能抛出的异常
     * @throws ClassNotFoundException    找不到指定类时可能抛出的异常
     * @throws NoSuchMethodException     反射调用构造函数时可能抛出的异常
     * @throws IllegalAccessException    反射访问字段时可能抛出的异常
     * @throws InvocationTargetException 反射调用方法时可能抛出的异常
     * @throws InstantiationException    实例化对象时可能抛出的异常
     */
    @PostMapping("/import")
    public R<List<?>> importExcel(@RequestParam("file") MultipartFile file, @RequestParam("className") String className) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> type = Class.forName(className);
        List<?> dataList = excelService.importExcel(file, type);
        return R.ok(dataList);
    }

    /**
     * 处理将数据写入 Excel 文件的请求
     * @param dataList 包含要写入数据的列表
     * @param className 数据类型的全限定名
     * @return 包含生成 Excel 文件的字节数组
     * @throws IOException 写入文件时可能抛出的异常
     * @throws ClassNotFoundException 找不到指定类时可能抛出的异常
     * @throws IllegalAccessException 反射访问字段时可能抛出的异常
     */
    @PostMapping("/write")
    public R<byte[]> writeToExcel(@RequestBody List<Object> dataList, @RequestParam("className") String className) throws Exception  {
        Class<?> type = Class.forName(className);
        try (Workbook workbook = excelService.writeToExcel(dataList, type);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            byte[] bytes = outputStream.toByteArray();
            return R.ok(bytes);
        }
    }
}