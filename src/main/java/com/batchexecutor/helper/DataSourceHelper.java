package com.batchexecutor.helper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;


public class DataSourceHelper {
    @Getter
    @Setter
    private static DataSource mainDataSource;
    @Getter
    @Setter
    private static DataSource subDataSource;
}