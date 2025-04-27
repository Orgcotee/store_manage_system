package com.example.produce.service;

import com.example.produce.domain.dto.PurchaseProductDTO;
import com.example.produce.domain.vo.PurchaseProductVO;

import java.util.List;

public interface PurchaseService {
    List<PurchaseProductVO> getMaterialListByProductIdAndQuantity(List<PurchaseProductDTO> purchaseProductDTO);
}
