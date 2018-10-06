package com.xl.canary.mapper;

import com.xl.canary.entity.LoanInstalmentEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * created by XUAN on 2018/08/20
 */
@org.apache.ibatis.annotations.Mapper
public interface LoanInstalmentMapper extends Mapper<LoanInstalmentEntity>{

    /**
     * 根据分期号获取分期
     * @param instalmentId
     * @return
     */
    LoanInstalmentEntity getByInstalmentId(@Param("instalmentId") String instalmentId);
}
