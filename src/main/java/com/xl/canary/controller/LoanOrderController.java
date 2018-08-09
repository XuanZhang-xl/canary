package com.xl.canary.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xl.canary.bean.condition.LoanOrderCondition;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.service.LoanOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * created by XUAN on 2018/08/09
 */
@RestController
@RequestMapping("/loan")
public class LoanOrderController {

    Logger logger = LoggerFactory.getLogger(LoanOrderController.class);

    @Autowired
    private LoanOrderService loanOrderService;

    @GetMapping("/fetch_loan_orders")
    public JSONObject fetchLoanOrders(
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "userCode", required = false) String userCode,
            @RequestParam(value = "lentTimeBegin",required = false) Long lentTimeBegin,
            @RequestParam(value = "lentTimeEnd", required = false) Long lentTimeEnd,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber
    ) {
        logger.info("进入fetch_loan_orders方法");
        LoanOrderCondition condition = new LoanOrderCondition();
        condition.setOrderId(orderId);
        condition.setUserCode(userCode);
        condition.setLentTimeBegin(lentTimeBegin);
        condition.setLentTimeEnd(lentTimeEnd);
        condition.setPageNumber(pageNumber);
        condition.setPageSize(pageSize);
        List<LoanOrderEntity> loanOrders = loanOrderService.fetchLoanOrders(condition);
        JSONObject result = new JSONObject();
        PageInfo<LoanOrderEntity> pageInfo = new PageInfo<>(loanOrders);
        result.put("orders", pageInfo);
        return result;
    }
}
