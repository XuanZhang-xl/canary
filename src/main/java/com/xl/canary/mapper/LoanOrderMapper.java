package com.xl.canary.mapper;

import com.xl.canary.bean.condition.LoanOrderCondition;
import com.xl.canary.entity.LoanOrderEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * created by XUAN on 2018/08/07
 */
@org.apache.ibatis.annotations.Mapper
public interface LoanOrderMapper extends Mapper<LoanOrderEntity>{

    /**
     * 条件查询
     * @param condition
     * @return
     */
    List<LoanOrderEntity> fetchLoanOrders(LoanOrderCondition condition);
}
