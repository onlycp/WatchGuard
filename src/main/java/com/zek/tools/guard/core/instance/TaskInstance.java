package com.zek.tools.guard.core.instance;

import com.zek.tools.guard.core.AppCommandLineRunner;
import com.zek.tools.guard.core.action.TaskAction;
import com.zek.tools.guard.core.action.TaskActionManager;
import com.zek.tools.guard.core.config.NotifyConfig;
import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.config.TaskConfig;
import com.zek.tools.guard.core.push.PushManager;
import com.zek.tools.guard.core.push.PushTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * @author chenp
 * @date 2024/1/19
 */
@Data
@Slf4j
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

    public void execute() {
        try {
            // 查找任务模板
            TaskConfig taskConfig = this.getTaskConfig();
            // 依次运行Action
            for (String actionName: taskConfig.getActions()) {
                TaskAction taskAction = TaskActionManager.getInstance().getTemplate(actionName);
                taskAction.execute(this.getTaskContext());
            }
            // 发送成功通知
            if (this.getTaskConfig().getSuccessNotifyChannels() != null && this.getTaskConfig().getSuccessNotifyChannels().size() > 0) {
                for (String notifyName: this.getTaskConfig().getSuccessNotifyChannels()) {
                    // 寻找通知实例
                    NotifyConfig notifyConfig = AppCommandLineRunner.appConfig.getNotifies().stream().filter(it->it.getName().equals(notifyName)).findFirst().orElse(null);
                    if (notifyConfig != null) {
                        PushTemplate pushTemplate = PushManager.getInstance().get(notifyConfig.getType());
                        String title = "【任务通知】【成功】" +this.getTaskConfig().getName();
                        String message = this.getTaskContext().getLogs().stream().collect(Collectors.joining("\n"));
                        pushTemplate.send(title, message, this, notifyConfig.getProperties());
                    }
                }
            }
        }
        catch (Exception e) {
            if (this.getTaskConfig().getFailNotifyChannels() != null && this.getTaskConfig().getFailNotifyChannels().size() > 0) {
                for (String notifyName: this.getTaskConfig().getFailNotifyChannels()) {
                    NotifyConfig notifyConfig = AppCommandLineRunner.appConfig.getNotifies().stream().filter(it->it.getName().equals(notifyName)).findFirst().orElse(null);
                    if (notifyConfig != null) {
                        PushTemplate pushTemplate = PushManager.getInstance().get(notifyConfig.getType());
                        String title = "【任务通知】【失败】" +this.getTaskConfig().getName();
                        String message = this.getTaskContext().getLogs().stream().collect(Collectors.joining("\n"));
                        pushTemplate.send(title, message, this, notifyConfig.getProperties());
                    }
                }
            }
            log.error("任务执行失败, 任务名称:{}, 异常信息", this.getTaskConfig().getName(), e);
        }
    }
}
