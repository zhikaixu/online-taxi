package com.zhikaixu.serviceprice.controller;


import com.zhikaixu.internalcommon.dto.PriceRule;
import com.zhikaixu.internalcommon.dto.ResponseResult;
import com.zhikaixu.serviceprice.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhikaixu
 * @since 2024-12-30
 */
@RestController
@RequestMapping("/price-rule")
public class PriceRuleController {

    @Autowired
    private PriceRuleService priceRuleService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody PriceRule priceRule) {

        return priceRuleService.add(priceRule);
    }

    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody PriceRule priceRule) {

        return priceRuleService.edit(priceRule);
    }

    /**
     * 查询最新的计价规则
     * @param fareType
     * @return
     */
    @GetMapping("/get-lastest-version")
    public ResponseResult getLatestVersion(@RequestParam String fareType) {

        return priceRuleService.getLatestVersion(fareType);
    }

    /**
     * 判断计价规则是否是最新
     * @param fareType
     * @param fareVersion
     * @return
     */
    @GetMapping("/is-new")
    public ResponseResult<Boolean> isNew(@RequestParam String fareType, Integer fareVersion) {

        return priceRuleService.isNew(fareType, fareVersion);
    }
}
