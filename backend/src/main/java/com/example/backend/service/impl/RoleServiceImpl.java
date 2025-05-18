package com.example.backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.entity.Role;
import com.example.backend.entity.RolePermission;
import com.example.backend.entity.UserRole;
import com.example.backend.entity.dto.RoleDto;
import com.example.backend.entity.vo.RoleVo;
import com.example.backend.mapper.RoleMapper;
import com.example.backend.mapper.RolePermissionMapper;
import com.example.backend.mapper.UserRoleMapper;
import com.example.backend.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    @Override
    public List<RoleVo> list() {
        List<Role> list = roleMapper.selectList(null);
        List<RoleVo> collect = new ArrayList<>();
        if (list != null) {
            collect = list.stream().map(role -> {
                RoleVo roleVo = new RoleVo();
                BeanUtils.copyProperties(role, roleVo);
                return roleVo;
            }).collect(Collectors.toList());
        }
        return collect;
    }

    @Override
    public void add(RoleDto role) {
        Role r = new Role();
        BeanUtils.copyProperties(role, r);
        roleMapper.insert(r);
        // 添加角色权限关联表
        List<RolePermission> rolePermissions = role.getPermissions().stream().map(permission -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(r.getId());
            rolePermission.setPermissionId(permission);
            return rolePermission;
        }).collect(Collectors.toList());
        rolePermissionMapper.insert(rolePermissions);
    }

    @Override
    public void update(RoleDto role) {
        Role role1 = roleMapper.selectById(role.getId());
        if (role1 == null){
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,"角色不存在");
        }
        Role r = new Role();
        BeanUtils.copyProperties(role, r);
        roleMapper.updateById(r);
        //删除原来的权限
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,role.getId());
        rolePermissionMapper.delete(wrapper);
        //添加权限
        List<RolePermission> rolePermissions = role.getPermissions().stream().map(permission -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permission);
            return rolePermission;
        }).collect(Collectors.toList());
        rolePermissionMapper.insert(rolePermissions);
    }

    @Override
    public void delete(Integer id) {
        Role role = roleMapper.selectById(id);
        if (role == null){
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,"角色不存在");
        }
        //查询有没有使用当前角色的用户 存在的话不能删
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getRoleId,id);
        if (userRoleMapper.selectCount(queryWrapper) > 0){
            throw new ServiceException(ServiceCode.ERR_CONFLICT,"该角色正在被使用，不能删除");
        }
        roleMapper.deleteById(id);
        //删除角色的权限
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,id);
        rolePermissionMapper.delete(wrapper);
    }

    @Override
    public IPage<RoleVo> pageList(RoleDto dto) {
        // 获取分页参数
        int pageNum = dto.getPageNum() == null ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null ? 5 : dto.getPageSize();

        // 创建分页对象
        Page<Role> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(dto.getKeyWord() != null, "name", dto.getKeyWord());
        IPage<Role> rolePage = roleMapper.selectPage(page, queryWrapper);

        // 获取当前页所有角色的 ID
        List<Integer> roleIds = rolePage.getRecords().stream()
                .map(Role::getId)
                .collect(Collectors.toList());

//        if (roleIds.isEmpty())
//            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "没有角色信息");
        // 批量查询所有角色对应的权限 ID
        List<Map<String, Object>> rolePermissions = rolePermissionMapper.getPermissionsByRoleIds(roleIds);

        // 将查询结果转换为以角色 ID 为键，权限 ID 列表为值的 Map
        Map<Integer, List<Integer>> rolePermissionMap = new HashMap<>();
        for (Map<String, Object> entry : rolePermissions) {
            Integer roleId = (Integer) entry.get("role_id");
            Integer permissionId = (Integer) entry.get("permission_id");
            rolePermissionMap.computeIfAbsent(roleId, k -> new ArrayList<>()).add(permissionId);
        }

        // 将 Role 转换为 RoleVo
        List<RoleVo> roleVoList = rolePage.getRecords().stream()
                .map(role -> {
                    RoleVo roleVo = new RoleVo();
                    BeanUtils.copyProperties(role, roleVo);
                    // 从 Map 中获取当前角色的权限 ID 列表
                    List<Integer> permissions = rolePermissionMap.getOrDefault(role.getId(), new ArrayList<>());
                    roleVo.setPermissions(permissions);
                    return roleVo;
                })
                .collect(Collectors.toList());

        // 创建新的 IPage 对象，将 RoleVo 列表放入其中
        IPage<RoleVo> roleVoPage = new Page<>(rolePage.getCurrent(), rolePage.getSize(), rolePage.getTotal());
        roleVoPage.setRecords(roleVoList);

        // 返回封装好的结果
        return roleVoPage;
    }
}
