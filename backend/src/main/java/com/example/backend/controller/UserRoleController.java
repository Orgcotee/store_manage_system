package com.example.backend.controller;

import com.example.backend.service.IUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户与角色多对多关联表 前端控制器
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@Controller
@RequestMapping("userRole")
@RequiredArgsConstructor
public class UserRoleController {
    private final IUserRoleService userRoleService;


}
