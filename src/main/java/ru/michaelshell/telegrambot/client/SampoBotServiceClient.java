package ru.michaelshell.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.name}", url = "${feign.url}")
public interface SampoBotServiceClient {

    @GetMapping( "/greet")
    String greet(@RequestParam long chatId);
}
