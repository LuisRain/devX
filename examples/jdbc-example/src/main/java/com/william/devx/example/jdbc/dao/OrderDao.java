package com.william.devx.example.jdbc.dao;


import com.william.devx.common.Page;
import com.william.devx.example.jdbc.entity.Order;
import com.william.devx.jdbc.DevxDao;
import com.william.devx.jdbc.annotations.Param;
import com.william.devx.jdbc.annotations.Select;

public interface OrderDao extends DevxDao<Integer, Order> {

    @Select(value = "SELECT ord.* FROM t_order ord " +
            "INNER JOIN pet p ON p.id = ord.pet_id " +
            "WHERE p.type = #{petType}", entityClass = Order.class)
    Page<Order> findOrders(@Param("petType") String petType,
                           @Param("pageNumber") long pageNumber, @Param("pageSize") int pageSize);

}
