package com.cdzg.xzshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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

    public RefundProcess(Long refundOrderId, String content, Integer status, Long createBy) {
        this.refundOrderId = refundOrderId;
        this.content = content;
        this.status = status;
        this.createTime = LocalDateTime.now();
        this.createBy = createBy;
    }
}
