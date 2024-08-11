package ru.michaelshell.telegrambot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.michaelshell.telegrambot.client.BotServiceClient;
import ru.michaelshell.telegrambot.model.Response;

import java.util.List;

@Component
public class TelegramBotImpl implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final ResponseSender responseSender;
    private final BotServiceClient botServiceClient;
    private final String token;

    public TelegramBotImpl(@Value("${bot.token}") String token,
                           BotServiceClient botServiceClient,
                           ResponseSender responseSender) {
        this.responseSender = responseSender;
        this.botServiceClient = botServiceClient;
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
            List<Response> responseList = botServiceClient.processUpdate(update);
            responseList.forEach(responseSender::sendResponse);
        }
    }
}
