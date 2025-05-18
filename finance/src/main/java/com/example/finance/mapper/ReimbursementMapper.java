package com.example.finance.mapper;

import com.example.finance.domain.entity.FReimbursement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReimbursementMapper {
    // 基本CRUD操作
    int insertReimbursement(FReimbursement entity);
    int updateReimbursement(FReimbursement entity);
    int deleteReimbursementById(Integer id);
    FReimbursement selectReimbursementById(Integer id);
    List<FReimbursement> selectAllReimbursements();

    // 条件查询
    List<FReimbursement> selectByApplicant(String applicant);
    List<FReimbursement> selectByStatus(Integer status);

    // 新增条件查询方法
    List<FReimbursement> selectByApprover(String approver);

    List<FReimbursement> selectByApplicantId(Integer applicantId);
}
