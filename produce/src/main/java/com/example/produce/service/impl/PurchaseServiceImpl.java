package com.example.produce.service.impl;

import com.example.produce.domain.dto.PurchaseProductDTO;
import com.example.produce.domain.vo.PurchaseProductVO;
import com.example.produce.mapper.PurchaseMapper;
import com.example.produce.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-26 16:43
 **/
@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public List<PurchaseProductVO> getMaterialListByProductIdAndQuantity(List<PurchaseProductDTO> purchaseProductDTOs) {
        if (CollectionUtils.isEmpty(purchaseProductDTOs)) {
            return Collections.emptyList();
        }
        List<PurchaseProductVO> allPurchaseProductVOs = new ArrayList<>();

        purchaseProductDTOs.forEach(purchaseProductDTO -> {
            List<PurchaseProductVO> purchaseProductVOs = purchaseMapper.selectMaterialListByProductIdAndQuantity(purchaseProductDTO);
            allPurchaseProductVOs.addAll(purchaseProductVOs);
    });
        // 并行处理版本（适用于大数据量）
        Map<Integer, PurchaseProductVO> mergedMap = allPurchaseProductVOs.parallelStream()
                .collect(Collectors.toMap(
                        PurchaseProductVO::getMaterialId,
                        this::copyPurchaseProductVO,
                        (existing, newItem) -> {
                            existing.setTotalQuantity(existing.getTotalQuantity().add(newItem.getTotalQuantity()));
                            return existing;
                        }
                ));
        return new ArrayList<>(mergedMap.values());
}

    // 辅助方法：复制PurchaseProductVO对象
    private PurchaseProductVO copyPurchaseProductVO(PurchaseProductVO source) {
        PurchaseProductVO copy = new PurchaseProductVO();
        copy.setMaterialId(source.getMaterialId());
        copy.setMaterialName(source.getMaterialName());
        copy.setUnit(source.getUnit());
        copy.setTotalQuantity(source.getTotalQuantity());
        // 复制其他必要属性
        return copy;
    }
}