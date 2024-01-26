package com.zek.tools.guard.instance;

import com.zek.tools.guard.task.TaskConfig;

/**
 * @author chenp
 * @date 2024/1/19
 */
public class TaskManager {

    // 单实例
    private static TaskManager instance = null;

    /**
     * 获取TaskManager的实例
     * 如果实例不存在，则创建一个新的TaskManager实例并返回
     * @return TaskManager的实例
     */
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    /**
     * 私有构造方法，禁止外部实例化
     */
    private TaskManager() {
    }

    /**
     * 添加任务
     *
     * @param task 任务配置对象
     */
    public void addTask(TaskConfig task) {
        System.out.println("newTask");
    }

}
