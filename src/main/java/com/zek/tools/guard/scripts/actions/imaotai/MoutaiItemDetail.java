package com.zek.tools.guard.scripts.actions.imaotai;

import lombok.Data;

/**
 * @author chenp
 * @date 2024/2/19
 */
@Data
public class MoutaiItemDetail {

    private int count;
    private int maxReserveCount;
    private int defaultReserveCount;
    private String itemId;
    private int inventory;
    private String ownerName;
    private String shopId;
}
