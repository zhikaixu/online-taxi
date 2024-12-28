package com.zhikaixu.servicemap.remote;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.zhikaixu.internalcommon.constant.AmapConfigConstants;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.TerminalResponse;
import com.zhikaixu.internalcommon.response.TrackResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrackClient {

    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.sid}")
    private String sid;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult<TrackResponse> add(String tid) {

        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TRACK_ADD);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + sid);
        url.append("&");
        url.append("tid=" + tid);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);

        String body = stringResponseEntity.getBody();
        JSONObject result = new JSONObject(body);
        JSONObject data = result.getJSONObject("data");
        String trid = String.valueOf(data.getInt("trid"));
        String trname = !data.has("trname") ? "": data.getString("trname");

        TrackResponse trackResponse = new TrackResponse();
        trackResponse.setTid(trid);
        trackResponse.setTrname(trname);
        return ResponseResult.success(trackResponse);
    }
}
