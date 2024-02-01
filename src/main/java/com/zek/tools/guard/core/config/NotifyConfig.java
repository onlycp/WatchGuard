package com.zek.tools.guard.core.config;

import lombok.Data;

import java.util.Map;

/**
 * @author chenp
 * @date 2024/2/1
 */
@Data
public class NotifyConfig {
    private String name;
    private String type;
    private Map<String, String> properties;
}
