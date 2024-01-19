package com.zek.tools.guard.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenp
 * @date 2024/1/19
 * @description 任务上下文
 */
public class TaskContext {

    /**
     * 变量存储的Map对象
     */
    private final Map<String, Object> variables = new HashMap<>();

    /**
     * 获取指定名称的变量值
     * @param name 变量名称
     * @param defaultValue 变量值的默认值
     * @return 变量值
     */
    public Object getVariable(String name, Object defaultValue) {
        String[] paths = name.split("\\.");
        Map<String, Object> tempVariables = new HashMap<>(variables);
        for (int i = 0; i < paths.length; i++) {
            if (i == paths.length - 1) {
                return (Integer) tempVariables.getOrDefault(name, defaultValue);
            }
            Object value = tempVariables.getOrDefault(paths[i], null);
            if (value == null) {
                return defaultValue;
            }
            tempVariables = (Map<String, Object>) value;
        }
        return null;
    }

    /**
     * 获取指定名称的整型变量值，如果不存在则返回默认值
     * @param name 变量名称
     * @param defaultValue 默认值
     * @return 变量值或默认值
     */
    public Integer getInteger(String name, Integer defaultValue) {
        Object value = getVariable(name, null);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value.toString());
    }

    /**
     * 获取指定名称的字符串变量值，如果不存在则返回默认值
     * @param name 变量名称
     * @param defaultValue 默认值
     * @return 变量值或默认值
     */
    public String getString(String name, String defaultValue) {
        Object value = getVariable(name, null);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    /**
     * 设置指定名称的变量值
     * @param name 变量名称
     * @param value 变量值
     */
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }
}
