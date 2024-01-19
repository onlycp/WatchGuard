package com.zek.tools.guard.action;

import com.zek.tools.guard.context.TaskContext;

/**
 * @author chenp
 * @date 2024/1/19
 */
public interface Action {

    /**
     * 执行动作
     * @param context 任务上下文
     * @return 是否执行成功
     */
    boolean execute(TaskContext context);
}
