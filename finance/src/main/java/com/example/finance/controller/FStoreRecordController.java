package com.example.finance.controller;

import com.example.finance.common.R;
import com.example.finance.domain.entity.FStoreRecord;
import com.example.finance.service.FStoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fStoreRecord")
public class FStoreRecordController {
    @Autowired
    private FStoreRecordService fStoreRecordService;

    @PostMapping("/daily")
    public R<Void> createDailyRecord(@RequestBody FStoreRecord record) {
        fStoreRecordService.createDailyRecord(record);
        return R.ok();
    }
}
