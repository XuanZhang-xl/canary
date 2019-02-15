package com.xl.canary.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.xl.canary.bean.BaseResponse;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.service.LoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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

    @GetMapping("/testRedis/get")
    public BaseResponse<String> testRedisGet (@RequestParam("key") String key) {
        BaseResponse<String> response = new BaseResponse<String>();

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object o = valueOperations.get(key);
        return response.buildSuccessResponse(o == null ? null : o.toString());
    }

    @GetMapping("/testRedis/set")
    public BaseResponse<String> testRediset (@RequestParam("key") String key, @RequestParam("value") String value) {
        BaseResponse<String> response = new BaseResponse<String>();

        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
        return response.buildSuccessResponse("OK");
    }

    public static void main(String[] args){
        GenericFastJsonRedisSerializer serializer = new GenericFastJsonRedisSerializer();
        Object deserialize = serializer.deserialize("[{\"user\":\"a\"}]".getBytes());
        System.out.println("44444444");
    }
}
