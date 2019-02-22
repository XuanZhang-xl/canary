package com.xl.canary.bean.structure;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.SchemaTypeEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.enums.pay.PayTypeEnum;
import com.xl.canary.exception.BaseException;
import com.xl.canary.utils.TimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 混合schema
 * created by zhangxuan9 on 2019/2/21
 */
public class MixedSchema extends Schema {

    /**
     * 保存应还的金额
     */
    private Schema shouldPaySchema;

    /**
     * 保存已还的金额
     */
    private Schema entrySchema;

    /**
     * 获得应还schema
     * @return
     */
    public Schema getShouldPaySchema() {
        if (schemaMap == null) {
            return null;
        }
        if (shouldPaySchema != null) {
            return shouldPaySchema;
        }
        shouldPaySchema = this.getTargetSchema(shouldPaySelect);
        return shouldPaySchema;
    }

    /**
     * 获得已还schema
     * @return
     */
    public Schema getEntrySchema() {
        if (schemaMap == null) {
            return null;
        }
        if (entrySchema != null) {
            return entrySchema;
        }
        entrySchema = this.getTargetSchema(entrySelect);
        return entrySchema;
    }

    public MixedSchema() {
    }

    public MixedSchema(SchemaTypeEnum schemaTypeEnum) {
        super(schemaTypeEnum);
    }
}
