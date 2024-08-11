package ru.michaelshell.telegrambot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.michaelshell.telegrambot.client.SampoBotServiceClient;

import java.util.List;

@Component
public class TelegramBotImpl implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final ResponseSender responseSender;
    private final SampoBotServiceClient sampoBotServiceClient;
    private final String token;

    public TelegramBotImpl(@Value("${bot.token}") String token,
                           SampoBotServiceClient sampoBotServiceClient,
                           ResponseSender responseSender) {
        this.responseSender = responseSender;
        this.sampoBotServiceClient = sampoBotServiceClient;
        this.token = token;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.getMessage() != null || update.getCallbackQuery() != null) {
            List<Response> responseList = sampoBotServiceClient.processUpdate(update);
            responseList.forEach(responseSender::sendResponse);
        }
    }
}
