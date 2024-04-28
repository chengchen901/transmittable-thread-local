package com.example.transmittablethreadlocaldemo;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequestMapping("/v3")
@RestController
public class SendEmailV3Controller {
    ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));
    TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();
    @GetMapping("/send")
    public String send(@RequestParam String msg) {
        try {
            threadLocal.set(msg);
            executorService.execute(() -> {
                log.info("我是子线程，发送邮件,msg:[{}]", threadLocal.get());
            });
            log.info("我是父线程，发送邮件提交任务到线程池完成,msg:[{}]", threadLocal.get());
        } finally {
            threadLocal.remove();
        }
        return "ok";
    }
}
