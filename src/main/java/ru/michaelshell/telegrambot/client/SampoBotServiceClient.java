package ru.michaelshell.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.michaelshell.telegrambot.bot.Response;

@FeignClient(name = "${feign.name}", url = "${feign.url}")
public interface SampoBotServiceClient {

    @PostMapping
    Response processUpdate(@RequestBody Update update);
}
