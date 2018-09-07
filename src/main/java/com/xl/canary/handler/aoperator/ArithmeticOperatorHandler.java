package com.xl.canary.handler.aoperator;

import com.xl.canary.enums.ArithmeticOperatorEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArithmeticOperatorHandler {

    ArithmeticOperatorEnum operator();
}
