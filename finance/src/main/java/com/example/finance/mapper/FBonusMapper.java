package com.example.finance.mapper;

import com.example.finance.domain.entity.FBonus;
import com.example.finance.domain.entity.FReimbursement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FBonusMapper {
    int insert(FBonus bonus);
    int updateById(FBonus bonus);
    List<FBonus> selectPending();
    List<FBonus> selectHistory(String applicant, Integer status);
    List<FBonus> selectByApplicant(
            @Param("applicant") String applicant,
            @Param("status") Integer status
    );
    // 新增条件查询方法
    List<FBonus> selectByApprover(String approver);
    List<FBonus> selectByApplicantId(Integer applicantId);
}
