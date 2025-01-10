package com.zhikaixu.servicessepush.controller;

import com.zhikaixu.internalcommon.util.SsePrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class SseController {

    public static Map<String, SseEmitter> sseEmitterMap = new HashMap<>();

    /**
     * 建立连接
     * @param userId 用户id
     * @param identity 身份信息
     * @return
     */
    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam Long userId, @RequestParam String identity) {
        log.info("connect: 用户ID: " + userId + " ,身份类型：" + identity);
        SseEmitter sseEmitter = new SseEmitter(0l);

        String sseMapKey = SsePrefixUtils.generatorSseKey(userId, identity);
        sseEmitterMap.put(sseMapKey, sseEmitter);

        return sseEmitter;
    }

    /**
     * 发送消息
     * @param userId 用户id
     * @param identity 身份信息
     * @param content 消息内容
     * @return
     */
    @GetMapping("/push")
    public String push(@RequestParam Long userId, @RequestParam String identity, @RequestParam String content) {
        log.info("push: 用户ID：" + userId + " ,身份：" + identity);
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId, identity);
        try {
            if (sseEmitterMap.containsKey(sseMapKey)) {
                sseEmitterMap.get(sseMapKey).send(content);
            } else {
                log.info("push: 用户ID：" + userId + " ,身份：" + identity + " 推送失败");
                return "推送失败";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "给用户: " + sseMapKey + " ,发送了消息: " + content;
    }

    /**
     * 关闭连接
     * @param userId 用户id
     * @param identity 身份信息
     * @return
     */
    @GetMapping("/close")
    public String close(@RequestParam Long userId, @RequestParam String identity) {
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId, identity);
        log.info("关闭连接：" + sseMapKey);
        if (sseEmitterMap.containsKey(sseMapKey)) {
            sseEmitterMap.remove(sseMapKey);
        }

        return "close 成功";
    }
}
