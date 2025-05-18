package com.example.finance.controller;

import com.example.finance.common.R;
import com.example.finance.domain.entity.FStaff;
import com.example.finance.service.FStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fStaff")
public class FStaffController {
    @Autowired
    private FStaffService fStaffService;

    @GetMapping
    public R<List<FStaff>> getAllStaff() {
        return R.ok(fStaffService.getAllStaff());
    }

    @PostMapping
    public R<Integer> addStaff(@RequestBody FStaff staff) {
        return R.ok(fStaffService.addStaff(staff));
    }

    @PutMapping
    public R<Integer> updateStaff(@RequestBody FStaff staff) {
        return R.ok(fStaffService.updateStaff(staff));
    }

    @DeleteMapping("/{id}")
    public R<Integer> deleteStaff(@PathVariable Integer id) {
        return R.ok(fStaffService.deleteStaff(id));
    }

    @GetMapping("/config")
    public R<Map<String, Object>> getSalaryConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("taxRate", 0.1);      // 税率
        config.put("attendanceBase", 100); // 全勤基数
        config.put("workDays", 22);      // 每月应出勤天数
        return R.ok(config);
    }
}
