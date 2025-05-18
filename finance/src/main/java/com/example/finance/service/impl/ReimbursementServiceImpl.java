package com.example.finance.service.impl;

import com.example.finance.domain.entity.FReimbursement;
import com.example.finance.mapper.ReimbursementMapper;
import com.example.finance.service.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {
    @Autowired
    private ReimbursementMapper reimbursementMapper;
    @Override
    @Transactional
    public Integer createReimbursement(FReimbursement reimbursement) {
        reimbursement.setApplicationTime(new Date());
        reimbursement.setApprovalStatus(0); // 初始状态：待审批 0
        reimbursementMapper.insertReimbursement(reimbursement);
        return reimbursement.getId();
    }

    // 根据审批人姓名查询处理过的记录
    @Override
    public List<FReimbursement> getApproverHistory(String approver) {
        return reimbursementMapper.selectByApprover(approver);
    }

    @Override
    @Transactional
    public Boolean updateReimbursement(FReimbursement reimbursement) {
        return reimbursementMapper.updateReimbursement(reimbursement) > 0;
    }

    @Override
    @Transactional
    public Boolean deleteReimbursement(Integer id) {
        return reimbursementMapper.deleteReimbursementById(id) > 0;
    }

    @Override
    public FReimbursement getById(Integer id) {
        return reimbursementMapper.selectReimbursementById(id);
    }

    @Override
    public List<FReimbursement> getAllReimbursements() {
        return reimbursementMapper.selectAllReimbursements();
    }

    @Override
    public List<FReimbursement> getPendingApprovals() {
        return reimbursementMapper.selectByStatus(0);
    }

    @Override
    public List<FReimbursement> getByApplicant(String applicant) {
        return reimbursementMapper.selectByApplicant(applicant);
    }

    @Override
    public List<FReimbursement> getByApplicantId(Integer applicantId) {
        return reimbursementMapper.selectByApplicantId(applicantId);
    }

    @Override
    @Transactional
    public Boolean approveReimbursement(Integer id, String approver, String opinion) {
        FReimbursement entity = reimbursementMapper.selectReimbursementById(id);
        if(entity == null || entity.getApprovalStatus() != 0) {
            throw new RuntimeException("无效的审批状态");
        }

        entity.setApprovalStatus(1);
        entity.setApprover(approver);
        entity.setApprovalOpinion(opinion);
        entity.setDealTime(new Date());

        return reimbursementMapper.updateReimbursement(entity) > 0;
    }

    @Override
    @Transactional
    public Boolean rejectReimbursement(Integer id, String approver, String reason) {
        FReimbursement entity = reimbursementMapper.selectReimbursementById(id);
        if(entity == null) return false;

        entity.setApprovalStatus(2);
        entity.setApprover(approver);
        entity.setApprovalOpinion(reason);
        entity.setDealTime(new Date());

        return reimbursementMapper.updateReimbursement(entity) > 0;
    }
}
