package com.example.finance.service;

import com.example.finance.domain.entity.FStore;

import java.util.List;

public interface FStoreService {
    List<FStore> listStores();
    void addStore(FStore store);
    void updateStore(FStore store);
    void deleteStore(Integer id);
}
