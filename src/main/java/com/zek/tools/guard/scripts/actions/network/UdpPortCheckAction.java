package com.zek.tools.guard.scripts.actions.network;

import com.zek.tools.guard.core.action.TaskAction;
import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.ex.ActionException;
import org.springframework.util.StringUtils;

import java.net.*;

/**
 * @author chenp
 * @date 2024/2/7
 */
public class UdpPortCheckAction implements TaskAction {
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
        if (!isUdpPortOpen(ip, port)) {
            throw  new ActionException("网络不通!");
        }
    }

    public static boolean isUdpPortOpen(String host, int port) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(1000);
            byte[] sendData = new byte[1];
            sendData[0] = 0; // 发送任意数据

            InetAddress address = InetAddress.getByName(host);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            socket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // 如果接收到响应，即说明端口是开放的
            return true;
        } catch (SocketTimeoutException e) {
            // 如果超时，则认为端口是关闭的
            return false;
        } catch (Exception e) {
            // 其他异常情况，也认为端口是关闭的
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
        return "udp-port-check";
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public String description() {
        return "UDP端口检测";
    }
}
