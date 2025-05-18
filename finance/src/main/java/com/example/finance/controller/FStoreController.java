package com.example.finance.controller;

import com.example.finance.common.R;
import com.example.finance.domain.entity.FStore;
import com.example.finance.service.FStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fStore")
public class FStoreController {
    @Autowired
    private FStoreService fStoreService;

    @GetMapping
    public R<List<FStore>> getAllStores() {
        return R.ok(fStoreService.listStores());
    }

    @PostMapping
    public R<Void> addStore(@RequestBody FStore store) {
        fStoreService.addStore(store);
        return R.ok();
    }

    @PutMapping
    public R<Void> updateStore(@RequestBody FStore store) {
        fStoreService.updateStore(store);
        return R.ok();
    }
    @DeleteMapping("/{id}")
    public R<Void> deleteStore(@PathVariable Integer id) {
        fStoreService.deleteStore(id);
        return R.ok();
    }
}
