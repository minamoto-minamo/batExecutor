package com.batchexecutor;

import com.batchexecutor.helper.DataSourceHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);

    }
    @Bean
    public CommandLineRunner dataSourceInitializer(
            @Qualifier("mainDataSource") DataSource mainDataSource,
            @Qualifier("logDataSource") DataSource logDataSource) {
        return args -> {
            DataSourceHelper.setMainDataSource(mainDataSource);
            DataSourceHelper.setSubDataSource(logDataSource);
        };
    }
}