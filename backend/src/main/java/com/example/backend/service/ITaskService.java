package com.example.backend.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.Task;
import com.example.backend.entity.dto.TaskDto;
import com.example.backend.entity.vo.TaskVo;

public interface ITaskService{
    Page<TaskVo> list(TaskDto task);

    void add(Task task);

    void update(Task task);

    boolean delete(Integer id);
}    