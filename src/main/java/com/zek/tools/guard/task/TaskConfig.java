package com.zek.tools.guard.task;

import java.util.Map;

/**
 * @author chenp
 * @date 2024/1/19
 */
public class TaskConfig {

    private String name;
    private String cron;
    private Boolean enabled;
    private String taskTemplateName;
    private Map<String, String> properties;
}
