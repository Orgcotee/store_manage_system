package com.example.backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.entity.Task;
import com.example.backend.entity.TaskTemplate;
import com.example.backend.entity.User;
import com.example.backend.entity.dto.TaskDto;
import com.example.backend.entity.vo.TaskVo;
import com.example.backend.mapper.TaskMapper;
import com.example.backend.mapper.TaskTemplateMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements ITaskService {
    private final TaskMapper taskMapper;
    private final TaskTemplateMapper taskTemplateMapper;

    private final UserMapper userMapper;
    @Override
    public Page<TaskVo> list(TaskDto task) {
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(task.getUserId() != null, Task::getUserId, task.getUserId())
                .eq(task.getTemplateId() != null, Task::getTemplateId, task.getTemplateId())
                .eq(task.getFinished() != null, Task::getFinished, task.getFinished())
                .like(task.getTaskName() != null, Task::getTaskName, task.getTaskName())
                .between(task.getStartTime() != null && task.getEndTime() != null, Task::getCreateTime, task.getStartTime(), task.getEndTime());
        Integer pageNum = task.getPageNum() == null ? Integer.valueOf(1) : task.getPageNum();
        Integer pageSize = task.getPageSize() == null ? Integer.valueOf(5) : task.getPageSize();
        Page<Task> page = new Page<>(pageNum, pageSize);
        Page<Task> taskPage = taskMapper.selectPage(page, queryWrapper);
        Page<TaskVo> vos = new Page<>();
        vos.setRecords(taskPage.getRecords().stream().map(temp -> {
            TaskVo taskVo = new TaskVo();
            BeanUtils.copyProperties(temp, taskVo);
            User user = userMapper.selectById(temp.getUserId());
            if (user == null) taskVo.setUsername("用户不存在");
            else
                taskVo.setUsername(user.getUsername());
            return taskVo;
        }).collect(Collectors.toList()));
        vos.setTotal(taskPage.getTotal());
        vos.setSize(taskPage.getSize());
        vos.setCurrent(taskPage.getCurrent());

        return vos;
    }

    @Override
    public void add(Task task) {
        TaskTemplate taskTemplate = taskTemplateMapper.selectById(task.getTemplateId());
        if (taskTemplate == null){
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "模板不存在");
        }
        task.setCreateTime(new Date());
        taskMapper.insert(task);
    }

    @Override
    public void update(Task task) {
        TaskTemplate taskTemplate = taskTemplateMapper.selectById(task.getTemplateId());
        if (taskTemplate == null){
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "模板不存在");
        }
        taskMapper.updateById(task);
    }

    @Override
    public boolean delete(Integer id) {
        if (taskMapper.selectById(id) == null)
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, "任务不存在");
        return taskMapper.deleteById(id) > 0;
    }
}