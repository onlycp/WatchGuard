package com.zek.tools.guard.core.push;

import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.instance.TaskInstance;

import java.util.Map;

/**
 * 通知模板接口
 *
 * @author chenp
 * @date 2024/1/19
 */
public interface PushTemplate {

    /**
     * 获取通知模板的名称
     */
    String name();

    /**
     * 发送通知
     *
     * @param title 通知标题
     * @param message 通知消息
     */
    void send(String title, String message, TaskInstance instance, Map<String, String> notifyProperties);
}
