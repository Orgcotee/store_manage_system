package com.example.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.anno.PreAuthorize;
import com.example.backend.common.R;
import com.example.backend.entity.Task;
import com.example.backend.entity.dto.TaskDto;
import com.example.backend.entity.vo.TaskVo;
import com.example.backend.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskService;

//    @PreAuthorize("task:list")
//    @GetMapping("/list")
//    public List<Task> list() {
//        return taskService.list();
//    }

    @PreAuthorize("task:list")
    @PostMapping("/list")
    public R<Page<TaskVo>> list(@RequestBody TaskDto dto) {
        Page<TaskVo> page = taskService.list(dto);
        return R.ok(page);
    }

    @PreAuthorize("task:add")
    @PostMapping("/add")
    public R<String> save(@RequestBody Task task) {
        taskService.add(task);
        return R.ok("新增成功");
    }

    @PreAuthorize("task:update")
    @PostMapping("/update")
    public R<String> update(@RequestBody Task task) {
        taskService.update(task);
        return R.ok("修改成功");
    }

    @PreAuthorize("task:delete")
    @DeleteMapping("/delete")
    public boolean delete(@RequestParam("id") Integer id) {
        return taskService.delete(id);
    }
}    