package com.xl.canary.config.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * Created by gqwu on 2017/7/7.
 */
@Configuration
public class MapperScannerAutoConfigurer {
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer () {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.xl.canary.mapper");
        Properties properties = new Properties();
        properties.setProperty("mappers", Mapper.class.getName());
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
