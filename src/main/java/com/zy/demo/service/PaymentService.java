package com.zy.demo.service;

/**
 * 支付接口
 *
 * @author zy
 */
public interface PaymentService {

    /**
     * 支付
     *
     * @param orderId   订单ID
     * @param payMethod 支付方式
     * @param payAmount 支付金额
     * @throws Exception Exception
     */
    void pay(long orderId, String payMethod, int payAmount) throws Exception;
}
