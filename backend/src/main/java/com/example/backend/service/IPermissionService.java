package com.example.backend.service;

import com.example.backend.entity.Permission;
import com.example.backend.entity.vo.PermissionVo;

import java.util.List;

/**
 * <p>
 * 系统权限表 服务类
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
public interface IPermissionService {

    List<PermissionVo> list();

    void add(Permission permission);

    void update(Permission permission);

    void delete(Integer id);
}
