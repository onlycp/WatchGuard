package com.zek.tools.guard.scripts.push;

import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.instance.TaskInstance;
import com.zek.tools.guard.core.push.PushTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author chenp
 * @date 2024/2/1
 */
public class ServerChanPush implements PushTemplate {
    /**
     * 获取通知模板的名称
     */
    @Override
    public String name() {
        return "serverChan";
    }

    /**
     * 发送通知
     *
     * @param title   通知标题
     * @param message 通知消息
     */
    @Override
    public void send(String title, String message, TaskInstance instance) {
        try {
            String url = "https://sctapi.ftqq.com/" + instance.getTaskContext().getString("key", "") + ".send";
            String postData = "text=" + URLEncoder.encode(title, "UTF-8") + "&desp=" + URLEncoder.encode(message, "UTF-8");


            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postData);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
    }
}
