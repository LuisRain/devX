package com.william.devx.example.jdbc.entity;

import com.william.devx.jdbc.entity.Column;
import com.william.devx.jdbc.entity.Entity;
import com.william.devx.jdbc.entity.PkColumn;

import java.io.Serializable;

@Entity
public class Customer implements Serializable{

    @PkColumn
    private int id;
    @Column(notNull = true)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
