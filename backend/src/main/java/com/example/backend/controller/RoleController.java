package com.example.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.backend.anno.PreAuthorize;
import com.example.backend.common.R;
import com.example.backend.entity.dto.RoleDto;
import com.example.backend.entity.vo.RoleVo;
import com.example.backend.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色权限表 前端控制器
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;
    /**
     * 查询角色列表
     */
    @PreAuthorize("role:list")
    @GetMapping("/list")
    public R<List<RoleVo>> list() {
        List<RoleVo> vo = roleService.list();
        return R.ok(vo);
    }
    @PreAuthorize("role:list")
    @PostMapping("/pageList")
    public R<IPage<RoleVo>> pageList(@RequestBody RoleDto dto) {
        IPage<RoleVo> roleVoIPage = roleService.pageList(dto);
        return R.ok(roleVoIPage);
    }
    @PreAuthorize("role:add")
    @PostMapping("/add")
    public R<String> add(@RequestBody RoleDto role) {
        roleService.add(role);
        return R.ok("新增成功");
    }
    @PreAuthorize("role:update")
    @PostMapping("/update")
    public R<String> update(@RequestBody RoleDto role) {
        roleService.update(role);
        return R.ok("修改成功");
    }
    @PreAuthorize("role:delete")
    @DeleteMapping("/delete")
    public R<String> delete(@RequestParam("id") Integer id) {
        roleService.delete(id);
        return R.ok("删除成功");
    }
}
