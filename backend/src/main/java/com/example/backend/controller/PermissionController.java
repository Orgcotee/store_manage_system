package com.example.backend.controller;

import com.example.backend.anno.PreAuthorize;
import com.example.backend.common.R;
import com.example.backend.entity.Permission;
import com.example.backend.entity.vo.PermissionVo;
import com.example.backend.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统权限表 前端控制器
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {
    private final IPermissionService permissionService;
    @PreAuthorize("menu:list")
    @GetMapping("/list")
    public R<List<PermissionVo>> list() {
        List<PermissionVo> list = permissionService.list();
        return R.ok(list) ;
    }
    @PreAuthorize("menu:add")
    @PostMapping("/add")
    public R<String> add(@RequestBody Permission permission) {
        permissionService.add(permission);
        return R.ok("新增成功") ;
    }
    @PreAuthorize("menu:update")
    @PostMapping("/update")
    public R<String> update(@RequestBody Permission permission) {
        permissionService.update(permission);
        return R.ok("修改成功") ;
    }
    @PreAuthorize("menu:delete")
    @DeleteMapping("/delete")
    public R<String> delete(@RequestParam("id") Integer id) {
        permissionService.delete(id);
        return R.ok("删除成功") ;
    }
}
