package com.william.devx.jdbc.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public abstract class PkEntity<P> implements Serializable {

    @ApiModelProperty("主键")
    @PkColumn
    protected P id;

    public P getId() {
        return id;
    }

    public void setId(P id) {
        this.id = id;
    }
}
