package ru.michaelshell.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.michaelshell.telegrambot.model.Response;

import java.util.List;

@FeignClient(name = "${feign.name}", url = "${feign.url}")
public interface BotServiceClient {

    @PostMapping
    List<Response> processUpdate(@RequestBody Update update);
}
