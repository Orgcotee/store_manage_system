package com.example.finance.service.impl;

import com.example.finance.domain.entity.FBonus;
import com.example.finance.domain.entity.FReimbursement;
import com.example.finance.mapper.FBonusMapper;
import com.example.finance.service.FBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FBonusServiceImpl implements FBonusService {
    @Autowired
    private FBonusMapper fBonusMapper;

    @Override
    public int create(FBonus bonus) {
        return fBonusMapper.insert(bonus);
    }

    @Override
    public List<FBonus> getByApplicantId(Integer applicantId) {
        return fBonusMapper.selectByApplicantId(applicantId);
    }

    // 根据审批人姓名查询处理过的记录
    @Override
    public List<FBonus> getApproverHistory(String approver) {
        return fBonusMapper.selectByApprover(approver);
    }

    @Override
    public List<FBonus> getByApplicant(String applicant, Integer status) {
        return fBonusMapper.selectByApplicant(applicant, status);
    }

    @Override
    public List<FBonus> getPending() {
        return fBonusMapper.selectPending();
    }

    @Override
    public int processApproval(Integer id, Integer status, String opinion, String approver) {
        FBonus bonus = new FBonus();
        bonus.setId(id);
        bonus.setApprovalStatus(status);
        bonus.setApprovalOpinion(opinion);
        bonus.setApprover(approver);
        return fBonusMapper.updateById(bonus);
    }

    @Override
    public List<FBonus> getHistory(String applicant, Integer status) {
        return fBonusMapper.selectHistory(applicant, status);
    }
}
