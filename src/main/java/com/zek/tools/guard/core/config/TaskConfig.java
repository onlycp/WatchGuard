package com.zek.tools.guard.core.config;

import lombok.Data;

import java.util.Map;

/**
 * @author chenp
 * @date 2024/1/19
 */
@Data
public class TaskConfig {

    private String name;
    private String cron;
    private Boolean enabled;
    private String template;
    private Map<String, String> properties;
}
