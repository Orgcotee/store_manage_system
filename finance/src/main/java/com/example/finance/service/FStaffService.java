package com.example.finance.service;

import com.example.finance.domain.entity.FStaff;

import java.util.List;

public interface FStaffService {
    List<FStaff> getAllStaff();
    int addStaff(FStaff staff);
    int updateStaff(FStaff staff);
    int deleteStaff(Integer id);
}
