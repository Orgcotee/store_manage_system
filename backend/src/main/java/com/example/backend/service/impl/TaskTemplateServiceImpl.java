package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.entity.TaskTemplate;
import com.example.backend.entity.dto.TaskTemplateDto;
import com.example.backend.mapper.TaskMapper;
import com.example.backend.mapper.TaskTemplateMapper;
import com.example.backend.service.ITaskTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskTemplateServiceImpl implements ITaskTemplateService {
    private final TaskTemplateMapper taskTemplateMapper;
    private final TaskMapper taskMapper;
    @Override
    public Page<TaskTemplate> list(TaskTemplateDto dto) {
        LambdaQueryWrapper<TaskTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(TaskTemplate::getTemplateName, dto.getTemplateName());

        return taskTemplateMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), queryWrapper);
    }

    @Override
    public void add(TaskTemplate taskTemplate) {
        LambdaQueryWrapper<TaskTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskTemplate::getTemplateName, taskTemplate.getTemplateName());
        if (taskTemplateMapper.selectOne(queryWrapper) != null) {
            throw new ServiceException(ServiceCode.ERR_EXISTS, "模板已存在");
        }
        taskTemplateMapper.insert(taskTemplate);
    }

    @Override
    public void update(TaskTemplate taskTemplate) {
        TaskTemplate template = taskTemplateMapper.selectById(taskTemplate.getId());
        if (template == null) {
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "模板不存在");
        }
        taskTemplateMapper.updateById(taskTemplate);
    }

    @Override
    public void delete(Integer id) {
        taskTemplateMapper.deleteById(id);
    }

    @Override
    public List<TaskTemplate> all() {
        return taskTemplateMapper.selectList(null);
    }
}