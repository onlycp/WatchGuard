package com.zek.tools.guard.scripts.push;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.zek.tools.guard.core.instance.TaskInstance;
import com.zek.tools.guard.core.push.PushTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author chenp
 * @date 2024/2/7
 */
@Slf4j
public class EmailPush implements PushTemplate {
    /**
     * 获取通知模板的名称
     */
    @Override
    public String name() {
        return "email";
    }

    /**
     * 发送通知
     *
     * @param title   通知标题
     * @param message 通知消息
     */
    @Override
    public void send(String title, String message,  TaskInstance instance, Map<String, String> notifyProperties) {
        MailAccount account = new MailAccount();
        account.setHost(notifyProperties.getOrDefault("host", ""));
        account.setPort(Integer.parseInt(notifyProperties.getOrDefault("port", "465")));
        account.setFrom(notifyProperties.getOrDefault("from", ""));
        account.setUser(notifyProperties.getOrDefault("from", ""));
        account.setPass(notifyProperties.getOrDefault("password", ""));
        account.setAuth(true);
        account.setSslEnable(true);
        String to = notifyProperties.getOrDefault("to", "");
        MailUtil.send(account, CollUtil.newArrayList(to), title,  message, false);
        log.info("通知标题: {}, 通知内容: {}", title, message);
    }
}
