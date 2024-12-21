package com.zhikaixu.servicemap.service;

import com.zhikaixu.internalcommon.constant.AmapConfigConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DicDistrictService {

    @Value("${amap.key}")
    private String amapKey;

    public ResponseResult initDicDistrict(String keywords) {

        // https://restapi.amap.com/v3/config/district?keywords=%E4%B8%AD%E5%9B%BD&subdistrict=3&key=ff0a778ef9d305c924e21b7bd8677ef7
        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.DISTRICT_URL);
        url.append("?");
        url.append("keywords="+keywords);
        url.append("&");
        url.append("subdistrict=3");
        url.append("&");
        url.append("key="+amapKey);

        // 解析结果

        // 插入数据库

        return ResponseResult.success();
    }
}
