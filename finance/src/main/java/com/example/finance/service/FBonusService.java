package com.example.finance.service;

import com.example.finance.domain.entity.FBonus;
import com.example.finance.domain.entity.FReimbursement;

import java.util.List;

public interface FBonusService {
    int create(FBonus bonus);
    List<FBonus> getPending();
    int processApproval(Integer id, Integer status, String opinion, String approver);
    List<FBonus> getHistory(String applicant, Integer status);
    List<FBonus> getByApplicant(String applicant, Integer status);
    List<FBonus> getByApplicantId(Integer id);
    List<FBonus> getApproverHistory(String approver);
}
