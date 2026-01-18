package com.zy.demo.service.impl;

import com.zy.demo.constant.RedisConstant;
import com.zy.demo.exception.BusinessException;
import com.zy.demo.service.PaymentService;
import com.zy.demo.util.RedisOpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 支付接口
 *
 * @author zy
 */
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final RedisOpUtil redisOpUtil;

    public PaymentServiceImpl(RedisOpUtil redisOpUtil) {
        this.redisOpUtil = redisOpUtil;
    }

    @Override
    public void pay(long orderId, String payMethod, int payAmount) throws Exception {
        String lockKey = RedisConstant.BIZ_PAY_LOCK_KEY + orderId;
        try {
            //非阻塞获取锁，自动续期。
            boolean hasLock = this.redisOpUtil.tryLock(lockKey, 5L, -1, TimeUnit.SECONDS);
            if (hasLock) {
                log.info("支付业务执行，订单ID={}，支付方式={}，支付金额={}", orderId, payMethod, payAmount);
            }
        } catch (InterruptedException e) {
            throw new BusinessException("支付失败", e);
        } finally {
            this.redisOpUtil.unlock(lockKey);
        }
    }
}
