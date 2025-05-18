package com.example.backend.controller;

import com.example.backend.anno.PreAuthorize;
import com.example.backend.common.R;
import com.example.backend.entity.Menu;
import com.example.backend.entity.vo.MenuVo;
import com.example.backend.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final IMenuService menuService;

    /**
     * 查询全部菜单和权限
     * @return
     */
    @PreAuthorize("menu:list")
    @GetMapping("/list")
    public R<List<MenuVo>> list() {
        List<MenuVo> vo = menuService.list();
        return R.ok(vo);
    }
    @PreAuthorize("menu:update")
    @PostMapping("/changeStatus")
    public R<String> changeStatus(@RequestBody Menu menu) {
        menuService.changeStatus(menu.getMenuId());
        return R.ok("修改成功");
    }
    @PreAuthorize("menu:update")
    @PostMapping("/update")
    public R<String> update(@RequestBody Menu menu) {
        menuService.update(menu);
        return R.ok("修改成功");
    }
    @PreAuthorize("menu:add")
    @PostMapping("/add")
    public R<String> add(@RequestBody Menu menu) {
        menuService.add(menu);
        return R.ok("新增成功");
    }
    @PreAuthorize("menu:delete")
    @DeleteMapping("/delete")
    public R<String> delete(@RequestParam("menuId") Integer menuId) {
        menuService.delete(menuId);
        return R.ok("删除成功");
    }
}
