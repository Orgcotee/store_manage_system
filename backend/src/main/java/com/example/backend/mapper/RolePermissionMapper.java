package com.example.backend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.RolePermission;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色与权限多对多关联表 Mapper 接口
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    @MapKey("roleId")
    List<Map<String, Object>> getPermissionsByRoleIds(@Param("roleIds") List<Integer> roleIds);

}

