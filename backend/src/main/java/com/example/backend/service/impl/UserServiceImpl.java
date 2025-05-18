package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.JwtTokenUtil;
import com.example.backend.common.PasswordUtil;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.entity.Menu;
import com.example.backend.entity.Permission;
import com.example.backend.entity.User;
import com.example.backend.entity.UserRole;
import com.example.backend.entity.dto.UserDto;
import com.example.backend.entity.vo.MenuVo;
import com.example.backend.entity.vo.UserRoleVo;
import com.example.backend.entity.vo.UserVo;
import com.example.backend.mapper.MenuMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.mapper.UserRoleMapper;
import com.example.backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserMapper userMapper;
    private final MenuMapper menuMapper;

    private final UserRoleMapper userRoleMapper;
    @Override
    public UserVo login(UserDto dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,dto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,"用户不存在");
        }

        UserVo vo = new UserVo();
        BeanUtils.copyProperties(user,vo);

        if (!PasswordUtil.matchesPassword(dto.getPassword(),user.getSalt(),user.getPassword())) {
            throw new ServiceException(ServiceCode.ERR_BAD_REQUEST,"密码错误");
        }
        //创建token
        vo.setToken(JwtTokenUtil.generateToken(user.getId(),user.getUsername()));
        //获取权限
        vo.setAuthorize(userMapper.selectPermissionCodeById(user.getId()));
        //获取路由
        List<Integer> idList = userMapper.selectComponentById(user.getId()); //用户所有的全部菜单id
        List<Menu> allMenu = menuMapper.selectList(null);//全部菜单
        List<Permission> permissionList = userMapper.selectPermissionById(user.getId());//用户的全部权限对象
        // 构建菜单树
        Map<Integer, MenuVo> menuVoMap = new HashMap<>();
        for (Menu menu : allMenu) {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            menuVoMap.put(menu.getMenuId(), menuVo);
        }

        for (MenuVo menuVo : menuVoMap.values()) {
            Long pid = menuVo.getPid();
            if (pid != null && menuVoMap.containsKey(pid.intValue())) {
                MenuVo parent = menuVoMap.get(pid.intValue());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(menuVo);
            }
        }

        // 找出所有需要展示的菜单ID，包括父菜单
        Set<Integer> allNeededMenuIds = new HashSet<>(idList);
        for (Integer menuId : idList) {
            MenuVo menuVo = menuVoMap.get(menuId);
            if (menuVo != null) {
                Long pid = menuVo.getPid();
                while (pid != null && pid != 0 && menuVoMap.containsKey(pid.intValue())) {
                    allNeededMenuIds.add(pid.intValue());
                    menuVo = menuVoMap.get(pid.intValue());
                    pid = menuVo.getPid();
                }
            }
        }

        // 筛选出需要展示的菜单
        List<MenuVo> userMenuVos = new ArrayList<>();
        for (Integer menuId : allNeededMenuIds) {
            MenuVo menuVo = menuVoMap.get(menuId);
            if (menuVo != null) {
                // 设置权限
                List<Permission> menuPermissions = new ArrayList<>();
                for (Permission permission : permissionList) {
                    if (Objects.equals(permission.getMenuId(), menuId)) {
                        menuPermissions.add(permission);
                    }
                }
                menuVo.setPermissions(menuPermissions);
                userMenuVos.add(menuVo);
            }
        }

        // 只保留根菜单
        List<MenuVo> rootMenuVos = new ArrayList<>();
        for (MenuVo menuVo : userMenuVos) {
            if (menuVo.getPid() == null ||!allNeededMenuIds.contains(menuVo.getPid().intValue())) {
                rootMenuVos.add(menuVo);
            }
        }
        vo.setRoutes(rootMenuVos);

        return vo;
    }

    @Override
    public void create(UserDto dto) {
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new ServiceException(ServiceCode.ERR_BAD_REQUEST,"用户名不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new ServiceException(ServiceCode.ERR_BAD_REQUEST,"密码不能为空");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,dto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            throw new ServiceException(ServiceCode.ERR_EXISTS,"用户已经存在");
        }
        User newUser = new User();
        BeanUtils.copyProperties(dto,newUser);
        try {
            newUser.setSalt(PasswordUtil.generateSalt(10));
            newUser.setPassword(PasswordUtil.encodePassword(dto.getPassword(),newUser.getSalt()));
            newUser.setCreateTime(Date.from(Instant.now()));
            userMapper.insert(newUser);
        } catch (Exception e) {
            throw new ServiceException(ServiceCode.ERR_BAD_REQUEST,"创建失败");
        }
    }

    @Override
    public void update(UserDto dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,dto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            throw new ServiceException(ServiceCode.ERR_EXISTS,"用户名已存在");
        }
        User newUser = new User();
        BeanUtils.copyProperties(dto,newUser);
        try {
            //需要修改密码
            if (!(dto.getPassword() == null || dto.getPassword().isEmpty())){
                //重新生成盐值 并且
                newUser.setSalt(PasswordUtil.generateSalt(10));
                newUser.setPassword(PasswordUtil.encodePassword(dto.getPassword(),newUser.getSalt()));
            }
            userMapper.insert(newUser);
        } catch (Exception e) {
            throw new ServiceException(ServiceCode.ERR_BAD_REQUEST,"修改失败");
        }
    }

    @Override
    public IPage<UserVo> list(UserDto dto) {
        //根据用户名模糊查询
        List<UserRoleVo> userRoleVoPage = userMapper.selectUserWithRolesPage(dto.getUsername(),dto.getRoleId());

        Map<Integer, List<UserRoleVo>> userRoleMap = userRoleVoPage.stream()
                .collect(Collectors.groupingBy(UserRoleVo::getId));

        List<UserVo> userVoList = new ArrayList<>();
        for (List<UserRoleVo> userRoleVos : userRoleMap.values()) {
            UserVo userVo = new UserVo();
            UserRoleVo firstVo = userRoleVos.get(0);
            userVo.setId(firstVo.getId());
            userVo.setUsername(firstVo.getUsername());
            userVo.setAvatar(firstVo.getAvatar());
            userVo.setPhone(firstVo.getPhone());
            userVo.setEmail(firstVo.getEmail());
            List<String> roleNames = userRoleVos.stream()
                    .map(UserRoleVo::getName)
                    .collect(Collectors.toList());
            userVo.setRoles(roleNames);
            userVoList.add(userVo);
        }
        // 对结果进行分页
        int pageNum = dto.getPageNum();
        int pageSize = dto.getPageSize();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, userVoList.size());
        List<UserVo> paginatedUserVoList = new ArrayList<>();
        if (startIndex < userVoList.size()) {
            paginatedUserVoList = userVoList.subList(startIndex, endIndex);
        }
        // 创建 IPage 对象
        IPage<UserVo> page = new Page<>(pageNum, pageSize);
        page.setRecords(paginatedUserVoList);
        page.setTotal(userVoList.size());
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        return page;
    }

    @Override
    public void updateRole(UserDto dto) {
        User user = userMapper.selectById(dto.getId());
        if (user == null) {
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,"用户不存在");
        }
        //删除原本的角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,dto.getId());
        userRoleMapper.delete(wrapper);

        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            List<UserRole> userRoleList = dto.getRoleIds().stream()
                    .map(roleId -> {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(dto.getId());
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList());
            userRoleMapper.insert(userRoleList);
        }
    }

    @Override
    public void delete(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,"用户不存在");
        }
        //删除当前用户所有的角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        userRoleMapper.delete(wrapper);

        userMapper.deleteById(id);
    }

    private MenuVo findMenuVoInChildren(MenuVo menuVo, Integer menuId) {
        if (Objects.equals(menuVo.getMenuId(), menuId)) {
            return menuVo;
        }
        if (menuVo.getChildren() != null) {
            for (MenuVo child : menuVo.getChildren()) {
                MenuVo found = findMenuVoInChildren(child, menuId);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
