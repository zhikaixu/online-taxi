package com.zhikaixu.serviceprice.service;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.ForecastPriceDTO;
import com.zhikaixu.internalcommon.response.DirectionResponse;
import com.zhikaixu.internalcommon.response.ForecastPriceResponse;
import com.zhikaixu.serviceprice.remote.ServiceMapClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ForecastPriceService {
    private final ServiceMapClient serviceMapClient;

    public ForecastPriceService(ServiceMapClient serviceMapClient) {
        this.serviceMapClient = serviceMapClient;
    }

    public ResponseResult forecastPrice(String depLongitude, String depLatitude, String destLongitude, String destLatitude) {
        log.info("出发地经度：" + depLongitude);
        log.info("出发地纬度：" + depLatitude);
        log.info("目的地经度：" + destLongitude);
        log.info("目的地纬度：" + destLatitude);

        log.info("调用地图服务，查询距离时长");
        // 接耦分装参数
        ForecastPriceDTO forecastPriceDTO = new ForecastPriceDTO();
        forecastPriceDTO.setDepLongitude(depLongitude);
        forecastPriceDTO.setDepLatitude(depLatitude);
        forecastPriceDTO.setDestLongitude(destLongitude);
        forecastPriceDTO.setDestLatitude(destLatitude);
        ResponseResult<DirectionResponse> direction = serviceMapClient.direction(forecastPriceDTO);
        Integer distance = direction.getData().getDistance();
        Integer duration = direction.getData().getDuration();
        log.info("距离: " + distance + ", 时长: " + duration);

        log.info("读取计价规则");

        log.info("根据距离、时长和计价规则，计算价格");

        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(12.34);
        return ResponseResult.success(forecastPriceResponse);
    }
}
