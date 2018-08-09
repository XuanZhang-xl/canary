package com.xl.canary.controller;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.BaseResponse;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.service.LoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 * created by XUAN on 2018/08/06
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private LoanOrderService loanOrderService;

    @GetMapping("/foo")
    public BaseResponse<JSONObject> foo() {
        BaseResponse<JSONObject> response = new BaseResponse<JSONObject>();
        LoanOrderEntity loanOrder = LoanOrderEntity.foo();
        LoanOrderEntity saved = loanOrderService.save(loanOrder);

        JSONObject json = new JSONObject();
        json.put("entity", saved);
        response.buildSuccessResponse(json);
        return response;
    }

}
