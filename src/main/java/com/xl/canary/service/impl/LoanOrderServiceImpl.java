package com.xl.canary.service.impl;

import com.github.pagehelper.PageHelper;
import com.xl.canary.bean.condition.LoanOrderCondition;
import com.xl.canary.engine.calculate.siuation.SituationSource;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.mapper.LoanOrderMapper;
import com.xl.canary.service.LoanInstalmentService;
import com.xl.canary.service.LoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * created by XUAN on 2018/08/07
 */
@Service("loanOrderServiceImpl")
public class LoanOrderServiceImpl implements LoanOrderService {

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private LoanInstalmentService loanInstalmentService;

    /**
     * 不用新开事务
     * TODO: 应删掉这个方法
     * @param loanOrder
     * @return
     */
    @Override
    @SituationSource
    @Transactional(rollbackFor = Exception.class)
    public LoanOrderEntity save(LoanOrderEntity loanOrder) {
        loanOrderMapper.insert(loanOrder);
        return loanOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LoanOrderEntity entity) {
        loanOrderMapper.updateByPrimaryKey(entity);
    }

    @Override
    public List<LoanOrderEntity> listByUserCode(String userCode, List<StateEnum> states) {
        Example example = new Example(LoanOrderEntity.class);
        example.createCriteria()
                .andEqualTo("userCode", userCode)
                .andIn("orderState", states);
        return loanOrderMapper.selectByExample(example);
    }

    @Override
    public List<LoanOrderEntity> fetchLoanOrders(LoanOrderCondition condition) {
        PageHelper.startPage(condition.getPageNumber(), condition.getPageSize());
        return loanOrderMapper.fetchLoanOrders(condition);
    }

    @Override
    public LoanOrderEntity getByOrderId(String orderId) {
        return loanOrderMapper.getByOrderId(orderId);
    }
}
