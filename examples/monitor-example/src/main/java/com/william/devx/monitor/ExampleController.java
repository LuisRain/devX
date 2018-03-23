package com.william.devx.monitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class ExampleController {

    @GetMapping("/example")
    public String example(@RequestParam("q") String q) throws InterruptedException {
        Thread.sleep(new Random().nextBoolean() ? 0 : 4000);
        return "enjoy!";
    }

    @GetMapping("/hello")
    public String hello() throws Exception {
        if (new Random().nextBoolean()) {
            return "hello!";
        } else {
            throw new Exception("error");
        }
    }

    private String exampleFallback(String q) {
        return "enjoyFallback!";
    }

    private String helloFallback() {
        return "helloFallback!";
    }

}
