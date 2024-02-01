package com.zek.tools.guard.core.task;

/**
 * 任务模板接口
 *
 * @author chenp
 * @date 2024/1/19
 */
public interface TaskTemplate {

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    String name();

    /**
     * 获取任务动作数组
     *
     * @return 任务动作数组
     */
    String[] actions();

    /**
     * 获取任务描述
     *
     * @return 任务描述
     */
    String description();

    /**
     * 获取通知渠道数组
     *
     * @return 通知渠道数组
     */
    String[] successNotifyChannels();

    /**
     * 获取通知渠道数组
     *
     * @return 通知渠道数组
     */
    String[] failNotifyChannels();
}
