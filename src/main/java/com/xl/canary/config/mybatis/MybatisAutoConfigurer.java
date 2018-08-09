package com.xl.canary.config.mybatis;

import com.github.pagehelper.PageInterceptor;
import com.xl.canary.config.mybatis.MybatisProperties;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by gqwu on 2017/6/29.
 * 配置Mybatis
 */
@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
@EnableTransactionManagement
public class MybatisAutoConfigurer implements TransactionManagementConfigurer {

    @Autowired
    private MybatisProperties properties;

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired
    private DataSource dataSource;

    //@Bean
    //public SqlSessionFactory sqlSessionFactory () throws Exception {
    //    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    //    factory.setConfigLocation(this.properties.getConfigLocation());
    //    factory.setDataSource(this.dataSource);
    //    factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
    //    //factory.setMapperLocations(this.properties.getMapperLocations());
    //
    //    if (this.interceptors != null && this.interceptors.length > 0) {
    //        factory.setPlugins(interceptors);
    //    }
    //    return factory.getObject();
    //}

    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        //以默认方式执行分页，有需要时进行修改
        properties.setProperty("reasonable", "true");
        interceptor.setProperties(properties);
        return interceptor;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
