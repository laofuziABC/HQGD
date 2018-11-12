package com.hqgd.pms.common;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MysqlDataSourceConfig {

    private static final String MAPPER_PATH = "classpath:mybatis/mapper/*.xml";

    private static final String ENTITY_PACKAGE = "com.hqgd.pms.domain";


    @Bean(name = "dataSource")
    @Primary
    @Qualifier("dataSource")
    @ConfigurationProperties(prefix="spring.datasource.first")
    public DataSource getMyDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "first.SqlSessionTemplate")
    @Primary
    public SqlSessionFactory devSqlSessionFactory(
            @Qualifier("dataSource") DataSource ddataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ddataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources(MAPPER_PATH));
        sessionFactory.setTypeAliasesPackage(ENTITY_PACKAGE);
        return sessionFactory.getObject();
    }

    @Bean
    @Primary
    public PlatformTransactionManager mysqlTransactionManager(@Qualifier("dataSource") DataSource mysqlDataSource)
    {
        return new DataSourceTransactionManager(mysqlDataSource);
    }
}