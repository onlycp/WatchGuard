package com.zek.tools.guard.scripts.actions.imaotai;

import lombok.Data;

/**
 * @author chenp
 * @date 2024/2/18
 */
@Data
public class MoutaiConfig {

    /**
     * 城市
     */
    private String city;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 经度
     */
    private String lng;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 省份
     */
    private String province;

    /**
     * Token
     */
    private String token;

    /**
     * 用户ID
     */
    private String userid;
    /**
     * 设备id
     */
    private String deviceid;
}

