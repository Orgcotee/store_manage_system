package com.example.backend.mapper;

import com.example.backend.entity.Permission;
import com.example.backend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.vo.UserRoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 */
public interface UserMapper extends BaseMapper<User> {
    List<String> selectPermissionCodeById (Integer userId);
    List<Integer> selectComponentById (Integer userId);
    List<UserRoleVo> selectUserWithRolesPage(@Param("username") String username, @Param("roleId") String roleId);
    List<Permission> selectPermissionById (Integer userId);
}
