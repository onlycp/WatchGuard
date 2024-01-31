package com.zek.tools.guard.template.demo.action;

import com.zek.tools.guard.core.action.TaskAction;
import com.zek.tools.guard.core.context.TaskContext;

/**
 * @author chenp
 * @date 2024/1/31
 */
public class DemoLoginAction implements TaskAction {
    /**
     * 执行动作
     *
     * @param context 任务上下文
     * @return 是否执行成功
     */
    @Override
    public boolean execute(TaskContext context) {
        return true;
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public String name() {
        return "jd-login";
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public String description() {
        return "Demo登录";
    }
}
