package com.zek.tools.guard.core.instance;

import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.config.TaskConfig;
import lombok.Data;

/**
 * @author chenp
 * @date 2024/1/19
 */
@Data
public class TaskInstance {

    /**
     * 任务配置对象
     */
    private TaskConfig taskConfig;

    /**
     * 任务上下文对象
     */
    private TaskContext taskContext;

    /**
     * 任务开始时间
     */
    private Long startTime;
}
