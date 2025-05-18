package com.example.backend.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.anno.PreAuthorize;
import com.example.backend.common.R;
import com.example.backend.entity.TaskTemplate;
import com.example.backend.entity.dto.TaskTemplateDto;
import com.example.backend.service.ITaskTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskTemplate")
@RequiredArgsConstructor
public class TaskTemplateController {
    @Autowired
    private ITaskTemplateService taskTemplateService;

    @PreAuthorize("task:template:list")
    @PostMapping("/list")
    public R<Page<TaskTemplate>> list(@RequestBody TaskTemplateDto dto) {
        Page<TaskTemplate> page = taskTemplateService.list(dto);
        return R.ok(page);
    }
    @PreAuthorize("task:template:list")
    @PostMapping("/all")
    public R<List<TaskTemplate>> all() {
        List<TaskTemplate> list = taskTemplateService.all();
        return R.ok(list);
    }

    @PreAuthorize("task:template:add")
    @PostMapping("/add")
    public R<String> add(@RequestBody TaskTemplate taskTemplate) {
        taskTemplateService.add(taskTemplate);
        return R.ok("新增成功");
    }

    @PreAuthorize("task:template:update")
    @PostMapping("/update")
    public R<String> update(@RequestBody TaskTemplate taskTemplate) {
        taskTemplateService.update(taskTemplate);
        return R.ok("修改成功");
    }

    @PreAuthorize("task:template:delete")
    @DeleteMapping("/delete")
    public R<String> delete(@RequestParam("id") Integer id) {
        taskTemplateService.delete(id);
        return R.ok("删除成功");
    }
}    