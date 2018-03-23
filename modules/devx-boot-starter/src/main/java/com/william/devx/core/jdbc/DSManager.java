package com.william.devx.core.jdbc;


import com.william.devx.Devx;

public interface DSManager {

    static DS select(String dsName) {
        if (dsName == null) {
            dsName = "";
        }
        if (dsName.isEmpty()) {
            return (DS) Devx.applicationContext.getBean("ds");
        } else {
            return (DS) Devx.applicationContext.getBean(dsName + "DS");
        }
    }

}
