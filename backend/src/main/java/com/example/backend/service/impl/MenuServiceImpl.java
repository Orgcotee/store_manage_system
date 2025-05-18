package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.entity.Menu;
import com.example.backend.entity.Permission;
import com.example.backend.entity.vo.MenuVo;
import com.example.backend.mapper.MenuMapper;
import com.example.backend.mapper.PermissionMapper;
import com.example.backend.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements IMenuService {
    private final MenuMapper menuMapper;
    private final PermissionMapper permissionMapper;
    @Override
    public List<MenuVo> list() {
        List<MenuVo> menuList = menuMapper.getMenuVos();
        // 先将权限信息分组到对应的菜单
        Map<Integer,List<Permission>> permissionMap = new HashMap<>();
        permissionMapper.selectList(null).forEach(permission -> {
            List<Permission> permissionList = permissionMap.get(permission.getMenuId());
            if (permissionList != null) {
                permissionList.add(permission);
            } else {
                List<Permission> permissions = new ArrayList<>();
                permissions.add(permission);
                permissionMap.put(permission.getMenuId(), permissions);
            }
        });

        menuList.forEach(menu -> {
            Integer menuId = menu.getMenuId();
            menu.setPermissions(permissionMap.get(menuId));
        });

        // 去除重复的菜单，合并权限
        Map<Integer, MenuVo> menuMap = new HashMap<>();
        for (MenuVo menu : menuList) {
            Integer menuId = menu.getMenuId();
            MenuVo currentMenu = menuMap.computeIfAbsent(menuId, k -> {
                MenuVo newMenu = new MenuVo();
                BeanUtils.copyProperties(menu, newMenu);
                return newMenu;
            });
            currentMenu.setPermissions(permissionMap.get(menuId));
        }

        // 构建菜单树
        List<MenuVo> rootMenus = new ArrayList<>();
        for (MenuVo menu : menuMap.values()) {
            Long pid = menu.getPid();
            if (pid == 0) {
                rootMenus.add(menu);
            } else {
                MenuVo parent = menuMap.get(pid.intValue());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }
        return rootMenus;
    }

    @Override
    public void changeStatus(Integer id) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null){
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "菜单不存在");
        }
        menu.setDisabled(!menu.getDisabled());
        menuMapper.updateById(menu);
        // 查找所有子菜单
        List<Integer> childMenuIds = new ArrayList<>();
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getPid, id);
        List<Menu> childMenus = menuMapper.selectList(queryWrapper);
        for (Menu childMenu : childMenus) {
            childMenuIds.add(childMenu.getMenuId());
        }
        // 更新子菜单的 disabled 状态
        if (!childMenuIds.isEmpty()) {
            LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(Menu::getMenuId, childMenuIds)
                    .set(Menu::getDisabled, menu.getDisabled());
            menuMapper.update(updateWrapper);
        }
    }

    @Override
    public void update(Menu menu) {
        Menu menu1 = menuMapper.selectById(menu.getMenuId());
        if (menu1 == null){
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "菜单不存在");
        }
        menuMapper.updateById(menu);
    }

    @Override
    public void add(Menu menu) {
        if (menu.getPid() != 0){
            Menu pmenu = menuMapper.selectById(menu.getPid());
            if (pmenu == null){
                throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "父菜单不存在");
            }
        }
        menuMapper.insert(menu);
    }

    @Override
    public void delete(Integer menuId) {
        menuMapper.selectById(menuId);
        if (menuId == null) {
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "菜单不存在");
        }
        //查询是否有子菜单 有子菜单不能删除
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getPid, menuId);
        if (menuMapper.selectList(queryWrapper).size() > 0) {
            throw new ServiceException(ServiceCode.ERR_CONFLICT, "该菜单有子菜单，不能删除");
        }
        menuMapper.deleteById(menuId);
    }
}
