package com.xl.canary.handle.aoperator;

import com.xl.canary.enums.ArithmeticOperatorEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArithmeticOperator {

    ArithmeticOperatorEnum operator();
}
