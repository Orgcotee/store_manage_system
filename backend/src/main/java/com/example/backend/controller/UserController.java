package com.example.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.backend.anno.PreAuthorize;
import com.example.backend.common.R;
import com.example.backend.entity.dto.UserDto;
import com.example.backend.entity.vo.UserVo;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final UserMapper mapper;

    /**
     * 测试查询
     * @return
     */
//    @PreAuthorize("test:list")
//    @GetMapping("/list")
//    public R<List<User>> getAddressList(){
//        List<String> list = new ArrayList<>();
//        list.add("1");
//        return R.ok(mapper.selectByIds(list));
//    }

    @PostMapping("/login")
    public R<UserVo> login(@RequestBody UserDto dto){
        UserVo userVo = userService.login(dto);
        return R.ok(userVo);
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody UserDto dto){
        userService.create(dto);
        return R.ok();
    }
    @PostMapping("/update")
    public R<Void> update(@RequestBody UserDto dto){
        userService.update(dto);
        return R.ok();
    }

    /**
     * 查询用户list
     *
     * @param dto
     * @return
     */
    @PreAuthorize("user:list")
    @PostMapping("/list")
    public R<IPage<UserVo>> list(@RequestBody UserDto dto){
        IPage<UserVo> list = userService.list(dto);
        return R.ok(list);
    }
    @PreAuthorize("user:update")
    @PostMapping("/updateRole")
    public R<Void> updateRole(@RequestBody UserDto dto){
        userService.updateRole(dto);
        return R.ok();
    }
    @PreAuthorize("user:delete")
    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam ("id") Integer id){
        userService.delete(id);
        return R.ok();
    }

}
