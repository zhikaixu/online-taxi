package com.zhikaixu.servicedriveruser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhikaixu.internalcommon.dto.DriverUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverUserMapper extends BaseMapper<DriverUser> {

    public Integer select1(@Param("cityCode") String cityCode);

    public Integer selectDriverUserCountByCityCode(@Param("cityCode") String cityCode);
}
