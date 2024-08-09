package ru.michaelshell.telegrambot.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.michaelshell.telegrambot.client.SampoBotServiceClient;

@Component
public class TelegramBotImpl implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final SampoBotServiceClient sampoBotServiceClient;
    private final String token;

    public TelegramBotImpl(@Value("${bot.token}") String token,
                           SampoBotServiceClient sampoBotServiceClient) {
        this.telegramClient = new OkHttpTelegramClient(token);
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
    @SneakyThrows
    public void consume(Update update) {
        long chatId = update.getMessage().getChatId();
        String response = sampoBotServiceClient.greet(chatId);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();
        telegramClient.execute(sendMessage);
    }
}
