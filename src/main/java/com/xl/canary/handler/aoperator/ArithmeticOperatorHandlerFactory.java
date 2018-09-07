package com.xl.canary.handler.aoperator;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 数学运算符工厂
 * Created by xzhang on 2018/9/7.
 */
@Component("arithmeticOperatorFactory")
public class ArithmeticOperatorHandlerFactory {

    private static final Logger logger = LoggerFactory.getLogger(ArithmeticOperatorHandlerFactory.class);

    @Autowired
    private ApplicationContext springContext;

    private static Map<ArithmeticOperatorEnum, IArithmeticOperatorHandler> arithmeticOperatorHandlers = new HashMap<ArithmeticOperatorEnum, IArithmeticOperatorHandler>();

    @PostConstruct
    public void init() {
        Collection states = this.springContext.getBeansWithAnnotation(ArithmeticOperatorHandler.class).values();
        for (Object object : states) {
            if (!(object instanceof IArithmeticOperatorHandler)) {
                continue;
            }
            IArithmeticOperatorHandler arithmeticOperator = (IArithmeticOperatorHandler) object;
            ArithmeticOperatorHandler annotation = arithmeticOperator.getClass().getAnnotation(ArithmeticOperatorHandler.class);
            arithmeticOperatorHandlers.put(annotation.operator(), arithmeticOperator);
            logger.info("数学操作符：[{}]，处理类：[{}]",annotation.operator().name(), arithmeticOperator.getClass());
        }
    }

    public static IArithmeticOperatorHandler instance (ArithmeticOperatorEnum operator) throws NotExistException {
        IArithmeticOperatorHandler iArithmeticOperator = arithmeticOperatorHandlers.get(operator);
        if (iArithmeticOperator == null) {
            logger.debug("状态类型[{}]没有匹配的处理类", operator.name());
            throw new NotExistException("数学操作符"+ operator.name() + "没有匹配的处理类");
        } else {
            return iArithmeticOperator;
        }
    }
}
