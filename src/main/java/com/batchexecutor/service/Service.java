package com.batchexecutor.service;

import java.sql.Connection;

public interface Service {
    void exec(Connection conn) throws Exception;
}
