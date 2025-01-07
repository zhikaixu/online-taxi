package com.zhikaixu.servicemap.controller;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.internalcommon.response.TerminalResponse;
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

    @PostMapping("/add")
    public ResponseResult<TerminalResponse> add(String name, String desc) {
        return terminalService.add(name, desc);
    }

    @PostMapping("/aroundSearch")
    public ResponseResult<List<TerminalResponse>> aroundSearch(String center, Integer radius) {
        return terminalService.aroundSearch(center, radius);
    }

    @GetMapping("/trsearch")
    public ResponseResult trsearch(String tid, Long starttime, Long endtime) {

        return terminalService.trsearch(tid, starttime, endtime);
    }
}
