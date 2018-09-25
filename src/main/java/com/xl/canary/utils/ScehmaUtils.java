package com.xl.canary.utils;

import com.xl.canary.bean.structure.Element;
import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.SchemaTypeEnum;
import com.xl.canary.enums.TimeUnitEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.exception.BaseException;
import com.xl.canary.exception.SchemaException;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

/**
 * schema工具类
 * TODO: 复杂, 作废
 * created by XUAN on 2018/09/25
 */
public class ScehmaUtils {

    /**
     * schema的每日结算
     * @param schema     结算开始时的schema
     * @param loanOrder  schema所属的order
     * @param begin      结算开始时间
     * @param end        要计算到的结束时间
     * @return
     * @throws SchemaException
     */
    public static Schema schemaDailySettle(Schema schema, LoanOrderEntity loanOrder, Calendar begin, Calendar end) throws BaseException {
        if (SchemaTypeEnum.LOAN_CURRENT.equals(schema.getSchemaType())) {
            throw new SchemaException("仅有LOAN_CURRENT类型的schema能进行每日结算, 传入schema类型: " + schema.getSchemaType().name());
        }

        int passDays = TimeUtils.passDays(begin, end);

        for (Map.Entry<Integer, Instalment> instalmentEntry : schema.entrySet()) {
            Instalment instalment = instalmentEntry.getValue();
            long repaymentDate = instalment.getRepaymentDate();
            Calendar calendar = TimeUtils.localCalendarTruncateDay(repaymentDate, loanOrder.getTimeZone());
            if (calendar.before(begin)) {
                // 逾期, 增加逾期费
                Element element = instalment.get(LoanOrderElementEnum.PRINCIPAL).get(0);
                Integer instalmentDays = TimeUnitEnum.valueOf(loanOrder.getInstalmentUnit()).getDays();
                BigDecimal addPenalty = element.getAmount()
                        .multiply(loanOrder.getPenaltyRate())
                        .multiply(new BigDecimal(passDays))
                        .divide(new BigDecimal(instalmentDays), LoanLimitation.RESULT_SCALE, LoanLimitation.RESULT_UP);


            } else {
                if (calendar.before(end) || calendar.equals(end)) {
                    // 当期, 增加利息
                } else {
                    // 没到期, do nothing
                }
            }
        }

        return schema;
    }


}
