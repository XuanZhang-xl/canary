package com.xl.canary.controller;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.enums.CurrencyEnum;
import com.xl.canary.enums.LoanOrderTypeEnum;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadPoolExecutor;

public class LoanOrderControllerTest implements Runnable {

    Logger logger = LoggerFactory.getLogger(LoanOrderControllerTest.class);

    private String localhost = "http://localhost:8080/canary";

    @Test
    public void fetchLoanOrders() {
        RestTemplate restTemplate = new RestTemplate();
        //header参数
        HttpHeaders headers = new HttpHeaders();
        //放入body中的json参数
        JSONObject content = new JSONObject();
        content.put("userCode","1");
        String newUrl = localhost + "/loan/fetch_loan_orders";
        HttpEntity<JSONObject> request = new HttpEntity<JSONObject>(content,headers); //组装
        ResponseEntity<String> response = restTemplate.exchange(newUrl, HttpMethod.GET,request,String.class);
        logger.info(response.getBody());
    }

    @Test
    public void applyLoanOrderTest() {
        logger.info("进入applyLoanOrderTest");
        RestTemplate restTemplate = new RestTemplate();
        //header参数
        HttpHeaders headers = new HttpHeaders();
        //放入body中的json参数
        JSONObject content = new JSONObject();
        content.put("userCode","1");
        content.put("loanOrderType", LoanOrderTypeEnum.GENERAL_TYPE.name());
        content.put("applyCurrency", CurrencyEnum.USDT.name());
        content.put("amount", "12312");
        content.put("instalment", "12");
        content.put("timeZone", "480");
        String newUrl = localhost + "/loan/apply_loan_order";
        HttpEntity<JSONObject> request = new HttpEntity<JSONObject>(content,headers); //组装
        ResponseEntity<String> response = restTemplate.exchange(newUrl, HttpMethod.POST,request,String.class);
        logger.info(response.getBody());
    }

    @Override
    public void run() {
        applyLoanOrderTest();
    }

    public static void main(String[] args) {
        // 多线程测试 锁
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new LoanOrderControllerTest());
            thread.start();
        }
    }
}