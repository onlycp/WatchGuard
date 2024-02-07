package com.zek.tools.guard.core.config;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author chenp
 * @date 2024/1/19
 */
@Data
/**
 * 任务配置类
 */
public class TaskConfig {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务cron表达式
     */
    private String cron;

    /**
     * 任务是否启用
     */
    private Boolean enabled;


    /**
     * 任务动作列表
     */
    private List<String> actions;

    /**
     * 任务属性
     */
    private Map<String, Object> properties;

    /**
     * 任务成功通知渠道列表
     */
    private List<String> successNotifyChannels;

    /**
     * 任务失败通知渠道列表
     */
    private List<String> failNotifyChannels;
}
