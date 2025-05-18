package com.example.backend.service;

import com.example.backend.entity.Menu;
import com.example.backend.entity.vo.MenuVo;

import java.util.List;

public interface IMenuService {
    List<MenuVo> list();

    void changeStatus(Integer id);

    void update(Menu menu);

    void add(Menu menu);

    void delete(Integer menuId);
}
