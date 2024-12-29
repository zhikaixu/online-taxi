package com.zhikaixu.servicemap.remote;

import com.zhikaixu.internalcommon.constant.AmapConfigConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.TerminalResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TerminalClient {

    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.sid}")
    private String sid;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult<TerminalResponse> add(String name, String desc) {

        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_ADD);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + sid);
        url.append("&");
        url.append("name=" + name);
        url.append("&");
        url.append("desc=" + desc);
        System.out.println("创建终端请求：" + url);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        /**
        {
            "errcode": 10000,
            "errmsg": "OK",
            "data": {
                "name": "车辆1",
                "tid": 1125251016,
                "sid": 1044135
            }
        }
         */
        String body = stringResponseEntity.getBody();
        JSONObject result = new JSONObject(body);
        JSONObject data = result.getJSONObject("data");
        System.out.println("创建终端响应：" + data);
        String tid = String.valueOf(data.getInt("tid"));
        TerminalResponse terminalResponse = new TerminalResponse();
        terminalResponse.setTid(tid);
        return ResponseResult.success(terminalResponse);
    }

    public ResponseResult<List<TerminalResponse>> aroundSearch(String center, Integer radius) {
        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_AROUNDSEARCH);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + sid);
        url.append("&");
        url.append("center=" + center);
        url.append("&");
        url.append("radius=" + radius);
        System.out.println("创建终端请求：" + url);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = stringResponseEntity.getBody();
        JSONObject result = new JSONObject(body);
        JSONObject data = result.getJSONObject("data");
        System.out.println("创建终端响应：" + data);

        List<TerminalResponse> terminalResponseList = new ArrayList<>();

        JSONArray resultArray = data.getJSONArray("results");
        for (int i = 0; i < resultArray.length(); i++) {
            TerminalResponse terminalResponse = new TerminalResponse();

            JSONObject jsonObject = resultArray.getJSONObject(i);
            Long carId = jsonObject.getLong("desc");
            String tid = jsonObject.getInt("tid") + "";

            terminalResponse.setTid(tid);
            terminalResponse.setCarId(carId);

            terminalResponseList.add(terminalResponse);
        }

        return ResponseResult.success(terminalResponseList);
    }
}
