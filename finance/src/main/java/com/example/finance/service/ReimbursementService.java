package com.example.finance.service;

import com.example.finance.domain.entity.FReimbursement;

import java.util.List;

public interface ReimbursementService {
    // 基础操作
    Integer createReimbursement(FReimbursement reimbursement);
    Boolean updateReimbursement(FReimbursement reimbursement);
    Boolean deleteReimbursement(Integer id);
    FReimbursement getById(Integer id);
    List<FReimbursement> getAllReimbursements();

    // 业务方法
    List<FReimbursement> getPendingApprovals();
    List<FReimbursement> getByApplicant(String applicant);
    List<FReimbursement> getApproverHistory(String approver);
    List<FReimbursement> getByApplicantId(Integer id);
    Boolean approveReimbursement(Integer id, String approver, String opinion);
    Boolean rejectReimbursement(Integer id, String approver, String reason);
}
