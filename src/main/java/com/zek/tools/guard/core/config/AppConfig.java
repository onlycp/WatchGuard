package com.zek.tools.guard.core.config;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author chenp
 * @date 2024/1/31
 */
@Data
public class AppConfig {


    private List<TaskConfig> tasks;
    private List<NotifyConfig> notifies;
}
