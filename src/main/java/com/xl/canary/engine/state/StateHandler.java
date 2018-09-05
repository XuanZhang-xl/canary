package com.xl.canary.engine.state;

import com.xl.canary.enums.StateEnum;

import java.lang.annotation.*;

/**
 * Created by sun on 2017/8/7.
 * 注解状态
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StateHandler {
  StateEnum name();
}
