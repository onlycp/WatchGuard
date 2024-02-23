package com.zek.tools.guard.scripts.actions.alidisk;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.zek.tools.guard.core.action.TaskAction;
import com.zek.tools.guard.core.context.TaskContext;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenp
 * @date 2024/2/22
 */
public class AliSigninCountAction implements TaskAction {
    /**
     * 执行动作
     *
     * @param context 任务上下文
     */
    @Override
    public void execute(TaskContext context) {
        String accessToken = context.getString("accessToken", "");
        if (StrUtil.isEmpty(accessToken)) {
            context.appendLog("令牌为空！");
            context.saveResult(false);
            return;
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization",  accessToken);
        headers.put("Content-Type", "application/json");
        String url = "https://member.aliyundrive.com/v1/activity/sign_in_list";
        String responseBody = HttpUtil.createPost(url).headerMap(headers,true).body("{}").execute().body();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int signInCount = jsonObject.getJSONObject("result").getInteger("signInCount");
        context.setVariable("signInCount", signInCount);
        context.appendLog("签到成功");


    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public String name() {
        return "AliSignCount";
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public String description() {
        return "获取签到天数";
    }
}
