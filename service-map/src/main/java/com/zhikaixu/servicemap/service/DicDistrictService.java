package com.zhikaixu.servicemap.service;

import com.zhikaixu.internalcommon.constant.AmapConfigConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicemap.remote.MapDicDistrictClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DicDistrictService {

    @Autowired
    private MapDicDistrictClient mapDicDistrictClient;

    public ResponseResult initDicDistrict(String keywords) {

        // 请求地图
        String dicDistrict = mapDicDistrictClient.dicDistrict(keywords);
        System.out.println(dicDistrict);
        // 解析结果

        // 插入数据库

        return ResponseResult.success();
    }
}
