package com.xl.canary.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xl.canary.utils.ReqHeaderParams;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

public class PayOrderControllerTest {

    Logger logger = LoggerFactory.getLogger(PayOrderControllerTest.class);

    private String localhost = "http://localhost:8080/canary";

    @Test
    public void shouldPay() {
        RestTemplate restTemplate = new RestTemplate();
        //header参数
        HttpHeaders headers = new HttpHeaders();
        headers.add(ReqHeaderParams.ACCOUNT_CODE, "1");
        //放入body中的json参数
        JSONObject content = new JSONObject();
        content.put("orderId","1");
        String newUrl = localhost + "/pay/should_pay?orderId=" + "239762215237472256";
        HttpEntity<JSONObject> request = new HttpEntity<JSONObject>(content,headers); //组装
        ResponseEntity<String> response = restTemplate.exchange(newUrl, HttpMethod.GET,request,String.class);
        logger.info(response.getBody());
    }

    @Test
    public void repayment() {

        RestTemplate restTemplate = new RestTemplate();
        //header参数
        HttpHeaders headers = new HttpHeaders();
        headers.add(ReqHeaderParams.ACCOUNT_CODE, "1");
        //放入body中的json参数
        JSONObject content = new JSONObject();
        content.put("userCode","1");
        content.put("applyCurrency","ETH");
        content.put("amount","1");
        content.put("loanOrderId","239762215237472256");
        String newUrl = localhost + "/pay/pay_order";
        HttpEntity<JSONObject> request = new HttpEntity<JSONObject>(content,headers); //组装
        ResponseEntity<String> response = restTemplate.exchange(newUrl, HttpMethod.POST,request,String.class);
        logger.info(response.getBody());
    }
}