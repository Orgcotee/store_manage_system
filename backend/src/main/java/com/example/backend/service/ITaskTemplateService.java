package com.example.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.TaskTemplate;
import com.example.backend.entity.dto.TaskTemplateDto;

import java.util.List;

public interface ITaskTemplateService{
    Page<TaskTemplate> list(TaskTemplateDto dto);

    void add(TaskTemplate taskTemplate);

    void update(TaskTemplate taskTemplate);

    void delete(Integer id);

    List<TaskTemplate> all();
}