package com.example.finance.controller;

import com.example.finance.common.R;
import com.example.finance.domain.entity.FReimbursement;
import com.example.finance.service.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reimbursement")
public class ReimbursementController {
    @Autowired
    private ReimbursementService reimbursementService;

    // 获取当前审批人处理过的记录（审批历史）
    @GetMapping("/approver/{name}")
    public R<List<FReimbursement>> getApproverHistory(@PathVariable String name) {
        return R.ok(reimbursementService.getApproverHistory(name));
    }

    @PostMapping("/create")
    public R<Integer> create(@RequestBody FReimbursement reimbursement) {
//        try {
            Integer id = reimbursementService.createReimbursement(reimbursement);
            return R.ok(id);
//        }
    }

    @PutMapping("/{id}")
    public R<Boolean> update(@PathVariable Integer id, @RequestBody FReimbursement reimbursement) {
        reimbursement.setId(id);
        return R.ok(reimbursementService.updateReimbursement(reimbursement));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Integer id) {
        return R.ok(reimbursementService.deleteReimbursement(id));
    }

    @GetMapping("/pending")
    public R<List<FReimbursement>> getPending() {
        return R.ok(reimbursementService.getPendingApprovals());
    }

    @GetMapping("/applicant/{name}")
    public R<List<FReimbursement>> getByApplicant(@PathVariable String name) {
        return R.ok(reimbursementService.getByApplicant(name));
    }

    @GetMapping("/applicantId/{id}")
    public R<List<FReimbursement>> getByApplicantId(@PathVariable Integer id) {
        return R.ok(reimbursementService.getByApplicantId(id));
    }

    @PostMapping("/approve/{id}")
    public R<Boolean> approve(@PathVariable Integer id,
                              @RequestParam String approver,
                              @RequestParam(required = false) String remark) {
        return R.ok(reimbursementService.approveReimbursement(id, approver, remark));
    }

    @PostMapping("/reject/{id}")
    public R<Boolean> reject(@PathVariable Integer id,
                             @RequestParam String approver,
                             @RequestParam String reason) {
        return R.ok(reimbursementService.rejectReimbursement(id, approver, reason));
    }
}
