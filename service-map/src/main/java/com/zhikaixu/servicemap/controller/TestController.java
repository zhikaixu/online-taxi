package com.zhikaixu.servicemap.controller;

import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.zhikaixu.internalcommon.dto.DicDistrict;
import com.zhikaixu.servicemap.mapper.DicDistrictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private DicDistrictMapper dicDistrictMapper;


    @GetMapping("/test")
    public String test() {
        return "Service-map Test";
    }

    @GetMapping("/test-map")
    public String testMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("address_code", "110000");
        List<DicDistrict> dicDistricts = dicDistrictMapper.selectByMap(map);
        System.out.println(dicDistricts);

        return "test-map";
    }
}
