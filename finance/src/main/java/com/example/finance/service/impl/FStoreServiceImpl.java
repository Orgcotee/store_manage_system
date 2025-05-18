package com.example.finance.service.impl;

import com.example.finance.domain.entity.FStore;
import com.example.finance.mapper.FStoreMapper;
import com.example.finance.service.FStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FStoreServiceImpl implements FStoreService {
    @Autowired
    private FStoreMapper fStoreMapper;

    @Override
    public List<FStore> listStores() {
        return fStoreMapper.selectAll();
    }

    @Override
    public void addStore(FStore store) {
        if (store.getManager() == null || store.getManager().isEmpty()) {
            throw new IllegalArgumentException("管理员不能为空");
        }
        fStoreMapper.insert(store);
    }

    @Override
    public void updateStore(FStore store) {
        if (store.getId() == null) {
            throw new IllegalArgumentException("门店ID不能为空");
        }
        fStoreMapper.updateById(store);
    }

    @Override
    public void deleteStore(Integer id) {
        fStoreMapper.deleteById(id);
    }
}
