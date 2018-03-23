package com.william.devx.example.jdbc.dao;


import com.william.devx.example.jdbc.entity.Customer;
import com.william.devx.jdbc.DevxDao;

public interface CustomerDao extends DevxDao<Integer, Customer> {
    @Override
    default String ds() {
        // 其它数据必须指时数据源的名称
        return "other";
    }
}
