package com.cdzg.xzshop.domain;

import lombok.Data;

/**
 * 退款理由
 */
@Data
public class ReasonsForRefund {

    private Long id;
    /**
     * 理由
     */
    private String content;
    /**
     * 0退款 1退货
     */
    private Integer type;

}
