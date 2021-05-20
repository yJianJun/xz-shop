package com.cdzg.xzshop.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefundProcess {

    /**
     * id
     */
    private Long id;
    /**
     * 退款订单id
     */
    private Long refundOrderId;
    /**
     * 操作内容
     */
    private String content;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 操作时间
     */
    private LocalDateTime createTime;
    /**
     * 操作人
     */
    private Long createBy;

}
