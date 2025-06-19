package com.batchexecutor.service;

import com.batchexecutor.enumeration.bat.BatchResult;
import com.batchexecutor.exception.NotReadyToExecuteException;
import com.batchexecutor.helper.DataSourceHelper;
import com.batchexecutor.logging.Loggable;

import java.io.FileNotFoundException;
import java.sql.Connection;

abstract class AbstractService implements Loggable, Service {

    public BatchResult start() {
        try (Connection conn = DataSourceHelper.getMainDataSource().getConnection()) {
            conn.setAutoCommit(false); // 明示的なトランザクション制御
            exec(conn);
            return BatchResult.SUCCESS;
        } catch (NotReadyToExecuteException e) {
            return BatchResult.WAIT;
        } catch (Exception e) {
            return BatchResult.ERROR;
        }
    }
}
