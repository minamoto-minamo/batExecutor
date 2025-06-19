package com.batchexecutor.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
public class DBConfig {
    @Bean(name = "mainDataSource")
    @ConfigurationProperties(prefix = "db.main")
    @Primary
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "subDataSource")
    @ConfigurationProperties(prefix = "db.sub")
    public DataSource subDataSource() {
        return DataSourceBuilder.create().build();
    }
}