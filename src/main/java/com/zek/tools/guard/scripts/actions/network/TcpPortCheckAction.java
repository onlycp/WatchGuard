package com.zek.tools.guard.scripts.actions.network;

import com.zek.tools.guard.core.action.TaskAction;
import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.ex.ActionException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author chenp
 * @date 2024/2/7
 */
public class TcpPortCheckAction  implements TaskAction {
    /**
     * 执行动作
     *
     * @param context 任务上下文
     * @return 是否执行成功
     */
    @Override
    public void execute(TaskContext context) {
        Integer port = context.getInteger("port", -1);
        if (port != -1) {
            throw  new ActionException("port不能为空!");
        }
        String ip = context.getString("ip", "");
        if (StringUtils.isEmpty(ip)) {
            throw  new ActionException("ip不能为空!");
        }
        if (!isTcpPortOpen(ip, port)) {
            throw  new ActionException("网络不通!");
        }
    }

    public static boolean isTcpPortOpen(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 5000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public String name() {
        return "tcp-port-check";
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public String description() {
        return "TCP端口检测";
    }
}
