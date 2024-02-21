package com.zek.tools.guard.core.context;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenp
 * @date 2024/1/19
 * @description 任务上下文
 */
@Slf4j
public class TaskContext {

    /**
     * 变量存储的Map对象
     */
    private final Map<String, Object> variables = new HashMap<>();
    /**
     * 日志存储的List对象
     */
    private List<String> logs = new ArrayList<>();

    /**
     * 获取指定名称的变量值
     * @param name 变量名称
     * @param defaultValue 变量值的默认值
     * @return 变量值
     */
    public Object getVariable(String name, Object defaultValue) {
       return variables.get(name);
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

    /*
     * 设置多个变量值
     */
    public void setVariables(Map<String, Object> variables) {
        this.variables.putAll(variables);
    }


    /**
     * 添加日志
     * @param message 日志内容
     */
    public void appendLog(String message) {
        log.info(message);
        logs.add(message);
    }

    /**
     * 获取日志列表
     * @return 日志列表
     */
    public List<String> getLogs() {
        return logs;
    }
    /**
     * 清空日志
     */
    public void clearLogs() {
        logs.clear();
    }
}
