package com.zek.tools.guard.scripts.push;

import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.instance.TaskInstance;
import com.zek.tools.guard.core.push.PushTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author chenp
 * @date 2024/2/7
 */
@Slf4j
public class LogPush  implements PushTemplate {
    /**
     * 获取通知模板的名称
     */
    @Override
    public String name() {
        return "log";
    }

    /**
     * 发送通知
     *
     * @param title   通知标题
     * @param message 通知消息
     * @param context
     */
    @Override
    public void send(String title, String message,  TaskInstance instance, Map<String, String> notifyProperties) {
        log.info("通知标题: {}, 通知内容: {}", title, message);
    }
}
