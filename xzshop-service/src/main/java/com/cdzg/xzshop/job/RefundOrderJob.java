package com.cdzg.xzshop.job;

import com.cdzg.xzshop.service.RefundOrderService;
import com.cdzg.xzshop.utils.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefundOrderJob {

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private RedisLock redisLock;

    private static final String AUTO_REFUND_LOCK = "auto:refund:lock";
    private static final String SYSTEM_AUTO_DEAL_LOCK = "system:auto:deal:lock";
    private static final String SYSTEM_AUTO_FAIL_LOCK = "system:auto:fail:lock";
    private static final String SYSTEM_AUTO_REFUND_LOCK = "system:auto:refund:lock";

    /**
     * 买家提交退款，卖家未处理，自动退款
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void autoRefund() {
        try {
            if (redisLock.lock(AUTO_REFUND_LOCK, 10000L)) {
                refundOrderService.autoRefund();
            }
        } finally {
            redisLock.releaseLock(AUTO_REFUND_LOCK);
        }
    }

    /**
     * 买家提交退货退款/换货申请，卖家未处理，系统自动处理
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void systemAutoDeal(){
        try {
            if (redisLock.lock(SYSTEM_AUTO_DEAL_LOCK, 10000L)) {
                refundOrderService.autoRefund();
            }
        } finally {
            redisLock.releaseLock(SYSTEM_AUTO_DEAL_LOCK);
        }
    }

    /**
     * 卖家同意退货/换货，买家未处理，系统自动失败
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void systemAutoFail(){
        try {
            if (redisLock.lock(SYSTEM_AUTO_FAIL_LOCK, 10000L)) {
                refundOrderService.autoRefund();
            }
        } finally {
            redisLock.releaseLock(SYSTEM_AUTO_FAIL_LOCK);
        }
    }

    /**
     * 卖家确认收货，未处理退款，系统自动退款
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void systemAutoRefund(){
        try {
            if (redisLock.lock(SYSTEM_AUTO_REFUND_LOCK, 10000L)) {
                refundOrderService.autoRefund();
            }
        } finally {
            redisLock.releaseLock(SYSTEM_AUTO_REFUND_LOCK);
        }
    }

}
