package com.william.devx.jdbc;

import com.william.devx.Devx;
import com.william.devx.jdbc.proxy.ProxyInvoker;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by 迹_Jason on 2017/7/26.
 * 接口动态实现
 */
public class DaoFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public T getObject() throws Exception {
        return Devx.applicationContext.containsBean(mapperInterface.getName()) ?
                Devx.applicationContext.getBean(mapperInterface) : new ProxyInvoker().getInstance(mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
