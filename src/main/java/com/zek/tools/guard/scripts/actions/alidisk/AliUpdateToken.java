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
public class AliUpdateToken implements TaskAction {
    /**
     * 执行动作
     *
     * @param context 任务上下文
     */
    @Override
    public void execute(TaskContext context) {
        String refreshToken = context.getString("refreshToken", null);
        if (StrUtil.isEmpty(refreshToken)) {
            context.appendLog("刷新令牌为空！");
            context.saveResult(false);
            return;
        }
        Map<String,Object> body = new HashMap<>();
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refreshToken);
        String responseBody = HttpUtil.createPost("https://auth.aliyundrive.com/v2/account/token").body(JSONObject.toJSONString(body)).execute().body();
        if (StringUtils.isEmpty(responseBody)) {
            context.appendLog("获取令牌失败！");
            context.saveResult(false);
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        if (jsonObject.containsKey("access_token")) {
            String accessToken = jsonObject.getString("access_token");
            context.appendLog("获取令牌成功！");
            context.setVariable("accessToken", accessToken);
            context.saveResult(true);
        }
        else {
            context.appendLog("获取令牌失败！响应内容:" + responseBody);
            context.saveResult(false);
        }


    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public String name() {
        return "AliUpdateToken";
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public String description() {
        return "获取阿里云盘令牌";
    }
}
