package com.zek.tools.guard.scripts.actions.imaotai;

import lombok.Data;

import java.util.List;

/**
 * @author chenp
 * @date 2024/2/18
 */
@Data
public class MoutaiShop {

    private String address;
    private Integer city;
    private String cityName;
    private Integer district;
    private String districtName;
    private String fullAddress;
    private Float lat;
    private String layaway;
    private Float lng;
    private String name;
    private String openEndTime;
    private String openStartTime;
    private Integer province;
    private String provinceName;
    private String shopId;
    private List<String> tags;
    private String tenantName;
}
