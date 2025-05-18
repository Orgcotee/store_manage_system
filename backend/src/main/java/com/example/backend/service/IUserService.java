package com.example.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.backend.entity.dto.UserDto;
import com.example.backend.entity.vo.UserVo;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
public interface IUserService{

    UserVo login(UserDto dto);

    void create(UserDto dto);

    void update(UserDto dto);

    IPage<UserVo> list(UserDto dto);

    void updateRole(UserDto dto);

    void delete(Integer id);
}
