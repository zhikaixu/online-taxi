package com.zhikaixu.servicemap.controller;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.TerminalResponse;
import com.zhikaixu.internalcommon.response.TrsearchResponse;
import com.zhikaixu.servicemap.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    /**
     * 添加终端
     * @param name
     * @param desc
     * @return
     */
    @PostMapping("/add")
    public ResponseResult<TerminalResponse> add(String name, String desc) {
        return terminalService.add(name, desc);
    }

    /**
     * 终端搜索
     * @param center
     * @param radius
     * @return
     */
    @PostMapping("/aroundSearch")
    public ResponseResult<List<TerminalResponse>> aroundSearch(String center, Integer radius) {
        return terminalService.aroundSearch(center, radius);
    }

    /**
     * 轨迹查询
     * @param tid
     * @param starttime
     * @param endtime
     * @return
     */
    @GetMapping("/trsearch")
    public ResponseResult<TrsearchResponse> trsearch(String tid, Long starttime, Long endtime) {

        return terminalService.trsearch(tid, starttime, endtime);
    }
}
