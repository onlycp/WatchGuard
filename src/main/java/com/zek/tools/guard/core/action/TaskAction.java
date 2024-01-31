package com.zek.tools.guard.core.action;

import com.zek.tools.guard.core.context.TaskContext;

/**
 * @author chenp
 * @date 2024/1/19
 */
public interface TaskAction {

    /**
     * 执行动作
     * @param context 任务上下文
     * @return 是否执行成功
     */
    boolean execute(TaskContext context);

    /**
     * 获取名称
     *
     * @return 名称
     */
    String name();

    /**
     * 获取描述
     *
     * @return 描述
     */
    String description();

}
