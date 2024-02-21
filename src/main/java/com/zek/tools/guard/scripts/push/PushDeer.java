package com.zek.tools.guard.scripts.push;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.instance.TaskInstance;
import com.zek.tools.guard.core.push.PushTemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenp
 * @date 2024/2/7
 */
@Slf4j
public class PushDeer implements PushTemplate {
    /**
     * 获取通知模板的名称
     */
    @Override
    public String name() {
        return "pushdeer";
    }

    /**
     * 发送通知
     *
     * @param title   通知标题
     * @param message 通知消息
     * @param instance
     */
    @Override
    public void send(String title, String message, TaskInstance instance, Map<String, String> notifyProperties) {
        try {
            // 构建请求URL
            String url = "https://api2.pushdeer.com/message/push";
            // 构建请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("pushkey", instance.getTaskContext().getString("pushKey", ""));
            params.put("text", URLEncodeUtil.encode(title));
            params.put("desp", URLEncodeUtil.encode(message));
            // 发送HTTP请求并获取响应体
            String responseBody = HttpUtil.get(url, params);
            // 解析响应体为JSON对象
            JSONObject response = JSONUtil.parseObj(responseBody);
            // 判断响应码是否为0，若不为0则表示发送失败
            if (!response.get("code").toString().equals("0")) {
                log.warn("短信发送失败:{}", responseBody);
            }
            // 返回响应对象
            // return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            // return null;
        }
    }
}
