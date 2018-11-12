package com.hqgd.pms.common;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SqlServerDataSourceConfig {

    private static final String MAPPER_PATH = "classpath:mybatis/mapper/*.xml";

    private static final String ENTITY_PACKAGE = "com.hqgd.pms.domain";

    @Bean(name = "sqlServerDataSource")
    @Qualifier("sqlServerDataSource")
    @ConfigurationProperties(prefix="spring.datasource.second")
    public DataSource getMyDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("sqlServerDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "second.SqlSessionTemplate")
    public SqlSessionFactory devSqlSessionFactory(
            @Qualifier("sqlServerDataSource") DataSource ddataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ddataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources(MAPPER_PATH));
        sessionFactory.setTypeAliasesPackage(ENTITY_PACKAGE);
        return sessionFactory.getObject();
    }

    @Bean
    public PlatformTransactionManager sqlServerTransactionManager(@Qualifier("sqlServerDataSource") DataSource sqlServerDataSource)
    {
        return new DataSourceTransactionManager(sqlServerDataSource);
    }

}