package com.xl.canary.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xl.canary.bean.BaseResponse;
import com.xl.canary.bean.condition.LoanOrderCondition;
import com.xl.canary.bean.req.LoanOrderReq;
import com.xl.canary.bean.res.LoanOrderRes;
import com.xl.canary.engine.calculate.siuation.SituationHolder;
import com.xl.canary.engine.event.order.AuditLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.UserEntity;
import com.xl.canary.entity.UserLevelSettingEntity;
import com.xl.canary.enums.*;
import com.xl.canary.enums.loan.*;
import com.xl.canary.lock.Lock;
import com.xl.canary.lock.RedisService;
import com.xl.canary.service.LoanInstalmentService;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.service.UserLevelSettingService;
import com.xl.canary.service.UserService;
import com.xl.canary.simulator.ExchangeRateSimulator;
import com.xl.canary.utils.EssentialConstance;
import com.xl.canary.bean.dto.Fee;
import com.xl.canary.utils.IDWorker;
import com.xl.canary.utils.RedisLockKeySuffix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * created by XUAN on 2018/08/09
 */
@RestController
@RequestMapping("/loan")
public class LoanOrderController {

    Logger logger = LoggerFactory.getLogger(LoanOrderController.class);

    @Autowired
    private IDWorker idWorker;

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private LoanInstalmentService loanInstalmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLevelSettingService userLevelSettingService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IEventLauncher loanOrderEventLauncher;

    @Value("${equivalent.currency}")
    private String equivalent;

    @GetMapping("/fetch_loan_orders")
    public BaseResponse<JSONObject> fetchLoanOrders(
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "userCode", required = false) String userCode,
            @RequestParam(value = "lentTimeBegin",required = false) Long lentTimeBegin,
            @RequestParam(value = "lentTimeEnd", required = false) Long lentTimeEnd,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber
    ) {
        BaseResponse<JSONObject> response = new BaseResponse<JSONObject>();
        LoanOrderCondition condition = new LoanOrderCondition();
        condition.setOrderId(orderId);
        condition.setUserCode(userCode);
        condition.setLentTimeBegin(lentTimeBegin);
        condition.setLentTimeEnd(lentTimeEnd);
        condition.setPageNumber(pageNumber);
        condition.setPageSize(pageSize);
        List<LoanOrderEntity> loanOrders = loanOrderService.fetchLoanOrders(condition);
        JSONObject result = new JSONObject();
        PageInfo<LoanOrderEntity> pageInfo = new PageInfo<LoanOrderEntity>(loanOrders);
        result.put("orders", pageInfo);
        return response.buildSuccessResponse(result);
    }


    @PostMapping("/apply_loan_order")
    public BaseResponse<LoanOrderRes> applyLoanOrder(@RequestBody @Validated LoanOrderReq req) {
        BaseResponse<LoanOrderRes> response = new BaseResponse<LoanOrderRes>();
        String userCode = req.getUserCode();
        UserEntity user = userService.getByUserCode(userCode);
        if (user == null) {
            return response.buildFailedResponse(ResponseNutEnum.NO_USER);
        }
        String level = user.getLevel();
        UserLevelSettingEntity userLevelSetting = userLevelSettingService.getByLevel(level);

        Lock lock = new Lock(RedisLockKeySuffix.LOAN_ORDER_KEY + userCode, String.valueOf(idWorker.generateID()));
        try {
            // 强制锁用户30秒, 以防重复下单
            if (redisService.lock(lock.getName(), lock.getValue(), 30000, TimeUnit.MILLISECONDS)) {
                BigDecimal amount = req.getAmount();
                CurrencyEnum applyCurrency = req.getApplyCurrency();
                LoanOrderTypeEnum loanOrderType = req.getLoanOrderType();
                Integer instalment = req.getInstalment();
                Integer timeZone = req.getTimeZone();

                LoanOrderEntity loanOrder = new LoanOrderEntity();
                long orderId = idWorker.nextId();
                loanOrder.setOrderId(String.valueOf(orderId));
                loanOrder.setUserCode(userCode);
                loanOrder.setOrderState(StateEnum.PENDING.name());
                loanOrder.setOrderType(loanOrderType.name());
                loanOrder.setInstalment(instalment);
                RepaymentDateTypeEnum repaymentDateType = loanOrderType.getRepaymentDateType();
                TimeUnitEnum timeUnit = repaymentDateType.getTimeUnit();
                loanOrder.setInstalmentUnit(timeUnit.name());
                loanOrder.setInstalmentRate(userLevelSetting.getDailyInterestRate().multiply(new BigDecimal(timeUnit.getDays())));
                loanOrder.setPenaltyRate(userLevelSetting.getDailyPenaltyRate().multiply(new BigDecimal(timeUnit.getDays())));
                loanOrder.setLendMode(LendModeEnum.AUTO.name());
                loanOrder.setApplyCurrency(applyCurrency.name());
                loanOrder.setApplyAmount(amount);
                loanOrder.setEquivalent(equivalent);
                BigDecimal ticker = ExchangeRateSimulator.getTicker(applyCurrency.name(), equivalent);
                loanOrder.setEquivalentRate(ticker);
                loanOrder.setEquivalentAmount(loanOrder.getApplyAmount().multiply(ticker));

                // 其他费用
                List<Fee> fee = new ArrayList<Fee>();
                Fee serviceFee = new Fee(FeeAllocateEnum.AVERAGE_IN_INSTALMENT, LoanOrderElementEnum.SERVICE_FEE.name(), BigDecimal.ONE);
                Fee bindAddressFee = new Fee(1, FeeAllocateEnum.FOLLOW_INSTALMENT, LoanOrderElementEnum.BIND_ADDRESS_FEE.name(), BigDecimal.ONE);
                Fee generateAddressFee = new Fee(2, FeeAllocateEnum.FOLLOW_INSTALMENT, LoanOrderElementEnum.GENERATE_ADDRESS_FEE.name(), BigDecimal.ONE);
                fee.add(serviceFee);
                fee.add(bindAddressFee);
                fee.add(generateAddressFee);
                loanOrder.setFee(JSONObject.toJSONString(fee));

                Long now = System.currentTimeMillis();
                loanOrder.setCreateTime(now);
                loanOrder.setUpdateTime(now);
                loanOrder.setTimeZone(timeZone / EssentialConstance.MINUTE_HOUR);
                loanOrder.setIsDeleted(0);
                loanOrderService.save(loanOrder);

                LoanOrderRes res = new LoanOrderRes();
                res.setOrderId(loanOrder.getOrderId());
                response.setData(res);

                // 放款操作, 必须在最后做, 否则可能出现幻读, 虚读等错误
                loanOrderEventLauncher.launch(new AuditLaunchEvent(userCode, loanOrder.getOrderId()));
                logger.info("获取situation ： {}", SituationHolder.getSituation());
                return response;
            } else {
                logger.error("用户[{}]下单锁竞争失败", userCode);
                return response.buildFailedResponse(ResponseNutEnum.LOCK_ERROR);
            }
        } catch (Exception e) {
            logger.error("下单报错:", e);
            return response.buildFailedResponse(ResponseNutEnum.UNKNOWN_EXCEPTION);
        } finally {
            redisService.release(lock);
        }
    }
}
