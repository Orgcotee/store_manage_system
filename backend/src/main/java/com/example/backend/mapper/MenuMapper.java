package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Menu;
import com.example.backend.entity.vo.MenuVo;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    List<MenuVo> getMenuVos();
}
