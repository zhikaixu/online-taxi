package com.zhikaixu.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.constant.OrderConstants;
import com.zhikaixu.internalcommon.dto.OrderInfo;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.OrderRequest;
import com.zhikaixu.internalcommon.util.RedisPrefixUtils;
import com.zhikaixu.serviceorder.mapper.OrderInfoMapper;
import com.zhikaixu.serviceorder.remote.ServicePriceClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-30
 */
@Service
public class OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ServicePriceClient servicePriceClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult add(OrderRequest orderRequest) {
        // 需要判断计价规则的版本是否为最新
        ResponseResult<Boolean> isNew = servicePriceClient.isNew(orderRequest.getFareType(), orderRequest.getFareVersion());
        if (!(isNew.getData())) {
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGED.getCode(), CommonStatusEnum.PRICE_RULE_CHANGED.getValue());
        }

        // 判断有正在进行的订单，不允许下单
        if (isOrderGoingon(orderRequest.getPassengerId()) > 0) {
            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(), CommonStatusEnum.ORDER_GOING_ON.getValue());
        }

        // 判断下单的设备是否是黑名单设备
        String deviceCode = orderRequest.getDeviceCode();
        // 生成key
        String deviceCodeKey = RedisPrefixUtils.blackDeviceCodePredix + deviceCode;
        // 设置key，校验原来有没有key
        if (isBlackDevice(deviceCodeKey)) {
            return ResponseResult.fail(CommonStatusEnum.DEVICE_IS_BLACK.getCode(), CommonStatusEnum.DEVICE_IS_BLACK.getValue());
        }

        stringRedisTemplate.opsForValue().setIfAbsent(deviceCodeKey, "1");

        // 创建订单
//        OrderInfo orderInfo = new OrderInfo();
//
//        BeanUtils.copyProperties(orderRequest, orderInfo);
//
//        orderInfo.setOrderStatus(OrderConstants.ORDER_START);
//
//        LocalDateTime now = LocalDateTime.now();
//        orderInfo.setGmtCreate(now);
//        orderInfo.setGmtModified(now);
//
//        orderInfoMapper.insert(orderInfo);
        return ResponseResult.success("");
    }

    private int isOrderGoingon(Long passengerId) {
        // 判断有正在进行的订单，不允许下单
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("passenger_id", passengerId);
        queryWrapper.and(wrapper->wrapper.eq("order_status", OrderConstants.ORDER_START)
                .or().eq("order_status", OrderConstants.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status", OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status", OrderConstants.PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.PASSENGER_GETOFF)
                .or().eq("order_status", OrderConstants.TO_START_PAY)
        );

        Integer validOrderNumber = orderInfoMapper.selectCount(queryWrapper);
        return validOrderNumber;

    }

    private boolean isBlackDevice(String deviceCodeKey) {
        Boolean b = stringRedisTemplate.hasKey(deviceCodeKey);
        if (b) {
            String s = stringRedisTemplate.opsForValue().get(deviceCodeKey);
            int i = Integer.parseInt(s);
            if (i >= 2) {
                // 当前设备超过下单次数
                return true;
            } else {
                stringRedisTemplate.opsForValue().increment(deviceCodeKey);
            }
        } else {
            stringRedisTemplate.opsForValue().setIfAbsent(deviceCodeKey, "1", 1L, TimeUnit.HOURS);
        }
        return false;
    }
}
