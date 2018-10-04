package com.xl.canary.enums.pay;

import com.xl.canary.enums.ResponseNutEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.exception.InnerException;

import java.util.ArrayList;
import java.util.List;

/**
 * 入账顺序枚举
 * 注意: 一般情况下, 本金都时最后还的, 分期/订单是否还清的判断依据就是本金是否还清, 如果改变这个顺序, 代码就要做相应变化
 * created by XUAN on 2018/08/19
 */
public enum EntrySequenceEnum {

    /**
     * 一般入账序列, 测试用
     */
    GENERAL_SEQUENCE(
        LoanOrderElementEnum.GENERATE_ADDRESS_FEE,
        LoanOrderElementEnum.BIND_ADDRESS_FEE,
        LoanOrderElementEnum.SERVICE_FEE,
        LoanOrderElementEnum.PENALTY,
        LoanOrderElementEnum.INTEREST,
        LoanOrderElementEnum.PRINCIPAL
    ),

    ;

    /**
     * 第一入账顺位
     */
    private LoanOrderElementEnum first;
    /**
     * 第二入账顺位
     */
    private LoanOrderElementEnum second;
    /**
     * 第三入账顺位
     */
    private LoanOrderElementEnum third;
    /**
     * 第四入账顺位
     */
    private LoanOrderElementEnum fourth;
    /**
     * 第五入账顺位
     */
    private LoanOrderElementEnum fifth;
    /**
     * 第六入账顺位
     */
    private LoanOrderElementEnum sixth;

    EntrySequenceEnum(
            LoanOrderElementEnum elementOne,
            LoanOrderElementEnum elementTwo,
            LoanOrderElementEnum elementThree,
            LoanOrderElementEnum elementFour,
            LoanOrderElementEnum elementFive,
            LoanOrderElementEnum elementSix) {
        this.first = elementOne;
        this.second = elementTwo;
        this.third = elementThree;
        this.fourth = elementFour;
        this.fifth = elementFive;
        /**
         * 最后一个入账元素比较特别, 如果这个元素为0, 代表还清, 如果以后增加元素, 请特别注意
         */
        this.sixth = elementSix;
    }

    public static List<LoanOrderElementEnum> getDefaultEntrySequence () {
        List<LoanOrderElementEnum> entrySequence = new ArrayList<LoanOrderElementEnum>();
        entrySequence.add(GENERAL_SEQUENCE.first);
        entrySequence.add(GENERAL_SEQUENCE.second);
        entrySequence.add(GENERAL_SEQUENCE.third);
        entrySequence.add(GENERAL_SEQUENCE.fourth);
        entrySequence.add(GENERAL_SEQUENCE.fifth);
        entrySequence.add(GENERAL_SEQUENCE.sixth);
        return entrySequence;
    }

    public static List<LoanOrderElementEnum> getEntrySequence (EntrySequenceEnum entrySequenceEnum) {
        List<LoanOrderElementEnum> entrySequence = new ArrayList<LoanOrderElementEnum>();
        entrySequence.add(entrySequenceEnum.fifth);
        entrySequence.add(entrySequenceEnum.second);
        entrySequence.add(entrySequenceEnum.third);
        entrySequence.add(entrySequenceEnum.fourth);
        entrySequence.add(entrySequenceEnum.fifth);
        entrySequence.add(entrySequenceEnum.sixth);
        return entrySequence;
    }

    public LoanOrderElementEnum getTheLastElement () {
        List<LoanOrderElementEnum> entrySequence = getDefaultEntrySequence();
        LoanOrderElementEnum theLast = entrySequence.get(entrySequence.size() - 1);
        if (LoanOrderElementEnum.THE_LASTS.contains(theLast)) {
            return theLast;
        }
        throw new InnerException(ResponseNutEnum.ERROR_LAST_ELEMENT);
    }

    public LoanOrderElementEnum getFirst() {
        return first;
    }

    public LoanOrderElementEnum getSecond() {
        return second;
    }

    public LoanOrderElementEnum getThird() {
        return third;
    }

    public LoanOrderElementEnum getFourth() {
        return fourth;
    }

    public LoanOrderElementEnum getFifth() {
        return fifth;
    }

    public LoanOrderElementEnum getSixth() {
        return sixth;
    }
}
