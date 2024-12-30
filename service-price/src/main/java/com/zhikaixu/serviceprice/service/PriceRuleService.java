package com.zhikaixu.serviceprice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikaixu.internalcommon.constant.CommonStatusEnum;
import com.zhikaixu.internalcommon.dto.PriceRule;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikaixu.serviceprice.mapper.PriceRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-30
 */
@Service
public class PriceRuleService {

    @Autowired
    private PriceRuleMapper priceRuleMapper;

    public ResponseResult add(PriceRule priceRule) {

        // 拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode + "$" + vehicleType;
        priceRule.setFareType(fareType);

        // 添加版本号
        Map<String, Object> map = new HashMap<>();
        map.put("city_code", cityCode);
        map.put("vehicle_type", vehicleType);
        // 问题1: 增加了版本号，前面的两个字段无法唯一确定一条记录
        // 问题2: 找最大的版本号，需要排序

        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code", cityCode);
        queryWrapper.eq("vehicle_type", vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);

        Integer fareVersion = 0;
        if (!priceRules.isEmpty()) {
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EXISTS.getCode(), CommonStatusEnum.PRICE_RULE_EXISTS.getValue());
        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success("");
    }

    public ResponseResult edit(PriceRule priceRule) {
        // 拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode + "$" + vehicleType;
        priceRule.setFareType(fareType);

        // 添加版本号
        Map<String, Object> map = new HashMap<>();
        map.put("city_code", cityCode);
        map.put("vehicle_type", vehicleType);

        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code", cityCode);
        queryWrapper.eq("vehicle_type", vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);

        Integer fareVersion = 0;
        if (!priceRules.isEmpty()) {
            PriceRule latestPriceRule = priceRules.get(0);
            Double unitPricePerMile = latestPriceRule.getUnitPricePerMile();
            Double unitPricePerMinute = latestPriceRule.getUnitPricePerMinute();
            Double startFare = latestPriceRule.getStartFare();
            Integer startMile = latestPriceRule.getStartMile();

            if (unitPricePerMile == priceRule.getUnitPricePerMile().doubleValue()
            && unitPricePerMinute == priceRule.getUnitPricePerMinute().doubleValue()
            && startMile == priceRule.getStartMile().intValue()
            && startFare == priceRule.getStartFare().doubleValue()) {
                return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_NOT_EDIT.getCode(), CommonStatusEnum.PRICE_RULE_NOT_EDIT.getValue());
            }
            fareVersion = latestPriceRule.getFareVersion();
        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success("");
    }

    public ResponseResult<PriceRule> getLatestVersion(@RequestParam String fareType) {
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fare_type", fareType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);

        if (!priceRules.isEmpty()) {
            return ResponseResult.success(priceRules.get(0));
        } else {
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(), CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }
    }

    public ResponseResult<Boolean> isNew(String fareType, int fareVersion) {
        ResponseResult<PriceRule> latestVersionPriceRule = getLatestVersion(fareType);
        if (latestVersionPriceRule.getCode() == CommonStatusEnum.PRICE_RULE_EMPTY.getCode()) {
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(), CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }

        PriceRule priceRule = latestVersionPriceRule.getData();
        Integer fareVersionDB = priceRule.getFareVersion();
        if (fareVersionDB > fareVersion) {
            return ResponseResult.success(false);
        }
        return ResponseResult.success(true);
    }
}
