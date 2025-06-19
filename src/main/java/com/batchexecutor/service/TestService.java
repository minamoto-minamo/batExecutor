package com.batchexecutor.service;


import org.springframework.stereotype.Service;


import java.sql.Connection;
import java.sql.SQLException;

@Service
public class TestService extends AbstractService {

    @Override
    public void exec(Connection conn) throws Exception {
        try {



        }catch(Exception e){
            throw e;
        }

    }
}