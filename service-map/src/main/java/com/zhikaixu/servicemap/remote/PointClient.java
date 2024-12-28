package com.zhikaixu.servicemap.remote;

import com.zhikaixu.internalcommon.constant.AmapConfigConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.request.PointDTO;
import com.zhikaixu.internalcommon.request.PointRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class PointClient {

    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.sid}")
    private String sid;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult upload(PointRequest pointRequest) {

        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.POINT_UPLOAD);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + sid);
        url.append("&");
        url.append("tid=" + pointRequest.getTid());
        url.append("&");
        url.append("trid=" + pointRequest.getTrid());
        url.append("&");
        url.append("points=");
        PointDTO[] points = pointRequest.getPoints();
        url.append("%5B");
        for (PointDTO p : points) {
            url.append("%7B");
            String location = p.getLocation();
            String locatetime = p.getLocatetime();
            url.append("%22location%22");
            url.append("%3A");
            url.append("%22" + location + "%22");
            url.append("%2C");
            url.append("%22locatetime%22");
            url.append("%3A");
            url.append("%22" + locatetime + "%22");
            url.append("%7D");
        }
        url.append("%5D");
        System.out.println("高德地图请求：" + url);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(URI.create(url.toString()), null, String.class);
        String body = stringResponseEntity.getBody();
        System.out.println("高德地图响应：" + body);

        return ResponseResult.success();
    }
}
