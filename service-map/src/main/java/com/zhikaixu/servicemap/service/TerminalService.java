package com.zhikaixu.servicemap.service;

import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.servicemap.remote.TerminalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {

    @Autowired
    private TerminalClient terminalClient;

    public ResponseResult add(String name) {

        return terminalClient.add(name);
    }
}
