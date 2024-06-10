package com.faroc.gymanager.infrastructure.beans;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class JooqConfig {

    private final DataSource dataSource;

    public JooqConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DSLContext dslContext() {
        return DSL.using(transactionAwareDataSource(), SQLDialect.POSTGRES);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        return new DataSourceTransactionManager(dataSource);
    }

    private TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource);
    }
}
