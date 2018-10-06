package com.xl.canary.mapper;

import com.xl.canary.entity.StrategyEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by xzhang on 2018/9/10.
 */
@org.apache.ibatis.annotations.Mapper
public interface StrategyMapper  extends Mapper<StrategyEntity> {

    /**
     * 根据主体获得策略
     * @param subject   主体
     * @param time      当前时间
     * @return
     */
    List<StrategyEntity> listAvailableStrategiesBySubject(@Param("subject") String subject, @Param("time")long time);
}
