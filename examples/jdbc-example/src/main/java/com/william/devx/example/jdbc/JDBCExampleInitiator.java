package com.william.devx.example.jdbc;


import com.william.devx.Devx;
import com.william.devx.common.Page;
import com.william.devx.example.jdbc.dao.CustomerDao;
import com.william.devx.example.jdbc.dao.OrderDao;
import com.william.devx.example.jdbc.dao.PetDao;
import com.william.devx.example.jdbc.entity.Customer;
import com.william.devx.example.jdbc.entity.Pet;
import com.william.devx.jdbc.DevxDS;
import com.william.devx.example.jdbc.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Component
public class JDBCExampleInitiator {

    @Autowired
    private PetDao petDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CustomerDao customerDao;

    @PostConstruct
    public void init() {
        // =============== DS 示例 ===============
        // 初始宠物表
        ((DevxDS) Devx.ds()).jdbc().execute("CREATE TABLE pet\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "type varchar(50),\n" +
                "price decimal(11,4) not null,\n" +
                "create_time datetime,\n" +
                "update_time datetime,\n" +
                "enabled bool\n" +
                ")");
        // 初始化订单表
        ((DevxDS) Devx.ds()).jdbc().execute("CREATE TABLE t_order\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "pet_id int,\n" +
                "customer_id int,\n" +
                "price decimal(11,4) not null,\n" +
                "create_time datetime \n" +
                ")");

        Pet pet = new Pet();
        pet.setType("狗");
        pet.setPrice(new BigDecimal(1000));
        pet.setEnabled(true);
        // insert
        int id = (int) Devx.ds().insert(pet);
        // getById
        pet = Devx.ds().getById(id, Pet.class);
        assert pet.getType().equals("狗");

        // =============== jdbc 示例 ===============
        // insert by dao
        pet = new Pet();
        pet.setType("猫");
        pet.setPrice(new BigDecimal(2000));
        pet.setEnabled(true);
        id = petDao.insert(pet);
        // getById by dao
        pet = petDao.getById(id);
        assert pet.getType().equals("猫");

        // =============== @Select 示例 ===============
        Page<Order> catOrders = orderDao.findOrders("猫", 1, 10);
        assert catOrders.getRecordTotal() == 1;

        // =============== 多数据源 示例 ===============
        // 初始化客户表，来自另一个数据源
        ((DevxDS) Devx.ds("other")).jdbc().execute("CREATE TABLE customer\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "name varchar(50)\n" +
                ")");
        Customer customer = new Customer();
        customer.setName("张三");
        // insert
        id = (int) Devx.ds("other").insert(customer);
        // getById
        customer = Devx.ds("other").getById(id, Customer.class);
        assert customer.getName().equals("张三");
        // insert by dao
        customer = new Customer();
        customer.setName("李四");
        id = customerDao.insert(customer);
        // getById by dao
        customer = customerDao.getById(id);
        assert customer.getName().equals("李四");

    }

}
