package com.example.finance.service.impl;

import com.example.finance.domain.entity.FStaff;
import com.example.finance.mapper.FStaffMapper;
import com.example.finance.service.FStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FStaffServiceImpl implements FStaffService {
    @Autowired
    private FStaffMapper fStaffMapper;

    @Override
    public List<FStaff> getAllStaff() {
        return fStaffMapper.selectAll();
    }

    @Override
    public int addStaff(FStaff staff) {
        return fStaffMapper.insert(staff);
    }

    @Override
    public int updateStaff(FStaff staff) {
        return fStaffMapper.update(staff);
    }

    @Override
    public int deleteStaff(Integer id) {
        return fStaffMapper.delete(id);
    }
}

