package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.entity.Menu;
import com.example.backend.entity.Permission;
import com.example.backend.entity.RolePermission;
import com.example.backend.entity.vo.PermissionVo;
import com.example.backend.mapper.MenuMapper;
import com.example.backend.mapper.PermissionMapper;
import com.example.backend.mapper.RolePermissionMapper;
import com.example.backend.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统权限表 服务实现类
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements IPermissionService {
    private final PermissionMapper mapper;
    private final MenuMapper menuMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public List<PermissionVo> list() {
        List<PermissionVo> collect = mapper.selectList(null).stream().map(permission -> {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            return permissionVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void add(Permission permission) {
        if(permission.getMenuId() == null || permission.getMenuId() == 0){
            throw new ServiceException(ServiceCode.ERR_BAD_REQUEST, "菜单不能为空,或为根目录");
        }
        Menu menu = menuMapper.selectById(permission.getMenuId());
        if (menu == null){
            throw new RuntimeException("菜单不存在");
        }
        mapper.insert(permission);
    }

    @Override
    public void update(Permission permission) {
        if(permission.getMenuId() == null || permission.getMenuId() == 0){
            throw new ServiceException(ServiceCode.ERR_BAD_REQUEST, "菜单不能为空,或为根目录");
        }
        Menu menu = menuMapper.selectById(permission.getMenuId());
        if (menu == null){
            throw new RuntimeException("菜单不存在");
        }
        mapper.updateById(permission);
    }

    @Override
    public void delete(Integer id) {
        if (mapper.selectById(id) == null){
            throw new RuntimeException("权限不存在");
        }
        //删除权限和角色关联表中的数据
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getPermissionId, id);
        rolePermissionMapper.delete(queryWrapper);
        mapper.deleteById(id);
    }
}
