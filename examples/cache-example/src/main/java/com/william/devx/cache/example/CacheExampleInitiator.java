package com.william.devx.mybatisplus.cache.example;

import com.william.devx.mybatisplus.cache.example.dto.CacheExampleDTO;
import com.william.devx.mybatisplus.cache.example.service.CacheExampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class CacheExampleInitiator implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CacheExampleService cacheExampleService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("=======Save=======");
        CacheExampleDTO dto = cacheExampleService.save(new CacheExampleDTO().setId("1").setName("银泰城"));
        assert dto.getId().equals("1");
        // Cache hit!
        dto = cacheExampleService.getById("1");
        assert dto.getName().equals("银泰城");

        log.info("=======Update=======");
        cacheExampleService.update("1", dto.setName("xx集团"));
        // Cache hit!
        dto = cacheExampleService.getById("1");
        assert dto.getName().equals("xx集团");

        log.info("=======Delete=======");
        cacheExampleService.delete("1");
        dto = cacheExampleService.getById("1");
        assert dto == null;

        log.info("=======Find=======");
        cacheExampleService.save(new CacheExampleDTO().setId("2").setName("银泰城"));
        cacheExampleService.save(new CacheExampleDTO().setId("3").setName("xx"));
        List<CacheExampleDTO> dtos = cacheExampleService.find();
        assert dtos.size() == 2;
        // Cache hit!
        dtos = cacheExampleService.find();
        assert dtos.size() == 2;
        cacheExampleService.deleteAll();
        dtos = cacheExampleService.find();
        assert dtos.size() == 0;
    }
}
