package com.zek.tools.guard.notify;

/**
 * 通知模板接口
 *
 * @author chenp
 * @date 2024/1/19
 */
public interface NotifyTemplate {

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
    void send(String title, String message);
}
