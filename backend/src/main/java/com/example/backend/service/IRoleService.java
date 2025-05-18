package com.example.backend.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.backend.entity.dto.RoleDto;
import com.example.backend.entity.vo.RoleVo;

import java.util.List;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
public interface IRoleService {

    List<RoleVo> list();

    void add(RoleDto role);

    void update(RoleDto role);

    void delete(Integer id);

    IPage<RoleVo> pageList(RoleDto dto);
}
