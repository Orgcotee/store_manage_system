package com.example.produce.service.impl;

import com.example.produce.commmon.ServiceCode;
import com.example.produce.commmon.ServiceException;
import com.example.produce.domain.dto.ProductBomDTO;
import com.example.produce.domain.vo.ProductBomVO;
import com.example.produce.mapper.ProductBomMapper;
import com.example.produce.service.ProductBomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: wm
 * @description:
 * @author: Qoo
 * @create: 2025-04-18 12:06
 **/

@Service
public class ProductBomServiceImpl implements ProductBomService {

    @Autowired
    private ProductBomMapper productBomMapper;

    public List<ProductBomVO> getProductBomByPId(Integer pid)
    {
        List<ProductBomVO> productBomVOS = productBomMapper.selectProductBomByPId(pid);
        System.out.println(productBomVOS);
        if(productBomMapper.selectProductBomByPId(pid) != null) {
            return productBomMapper.selectProductBomByPId(pid);
        }
        return null;
    }

    @Override
    public void deleteProductBomByPBId(Integer pbid) {
        Integer count = productBomMapper.deleteProductBomByPBId(pbid);
        if(count != 1) {
            throw new ServiceException(ServiceCode.ERR_DELETE_FAILED,"删除失败");
        }
    }

    @Override
    public void addProductBom(ProductBomDTO productBomDTO) {
        productBomDTO.setCreateTime(new Date());
        System.out.println("productBomDTO:{}"+productBomDTO);
        Integer count = productBomMapper.addProductBom(productBomDTO);
        if(count != 1) {
            throw new ServiceException(ServiceCode.ERR_SAVE_FAILED,"保存失败");
        }
    }

    @Override
    public void updateProductBom(ProductBomDTO productBomDTO) {
        if(productBomDTO.getId()==null) {
            throw new ServiceException(ServiceCode.ERR_UPDATE_FAILED,"更新失败");
        }
        Integer count = productBomMapper.updateProductBom(productBomDTO);
        if(count != 1) {
            throw new ServiceException(ServiceCode.ERR_UPDATE_FAILED,"更新失败");
        }
    }
}