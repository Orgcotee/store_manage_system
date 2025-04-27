package com.example.produce.mapper;

import com.example.produce.domain.dto.PurchaseProductDTO;
import com.example.produce.domain.vo.PurchaseProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseMapper {
    List<PurchaseProductVO> selectMaterialListByProductIdAndQuantity(PurchaseProductDTO purchaseProductDTO);
}
