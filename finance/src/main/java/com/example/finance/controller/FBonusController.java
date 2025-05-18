package com.example.finance.controller;

import com.example.finance.common.R;
import com.example.finance.domain.entity.FBonus;
import com.example.finance.domain.entity.FReimbursement;
import com.example.finance.service.FBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fBonus")
public class FBonusController {
    @Autowired
    private FBonusService fBonusService;

    // 提交奖金申请
    @PostMapping("/create")
    public R<Integer> create(@RequestBody FBonus bonus) {
        return R.ok(fBonusService.create(bonus));
    }

    @GetMapping("/historyById/{id}")
    public R<List<FBonus>> getByApplicantId(@PathVariable Integer id) {
        return R.ok(fBonusService.getByApplicantId(id));
    }

    // 获取当前审批人处理过的记录（审批历史）
    @GetMapping("/approver/{name}")
    public R<List<FBonus>> getApproverHistory(@PathVariable String name) {
        return R.ok(fBonusService.getApproverHistory(name));
    }

    // 获取待审批列表
    @GetMapping("/pending")
    public R<List<FBonus>> getPending() {
        return R.ok(fBonusService.getPending());
    }

    // 审批操作
    @PostMapping("/approve/{id}")
    public R<Integer> approve(
            @PathVariable Integer id,
            @RequestParam Integer status,
            @RequestParam String opinion,
            @RequestParam String approver
    ) {
        return R.ok(fBonusService.processApproval(id, status, opinion, approver));
    }

    // 获取申请历史
    @GetMapping("/history")
    public R<List<FBonus>> getHistory(
            @RequestParam(required = false) String applicant,
            @RequestParam(required = false) Integer status
    ) {
        return R.ok(fBonusService.getHistory(applicant, status));
    }

    // 根据申请人获取历史记录
    @GetMapping("/applicant/{applicant}")
    public R<List<FBonus>> getByApplicant(
            @PathVariable String applicant,
            @RequestParam(required = false) Integer status
    ) {
        return R.ok(fBonusService.getHistory(applicant, status));
    }
}
