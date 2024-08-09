package ru.michaelshell.telegrambot.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.michaelshell.telegrambot.client.SampoBotServiceClient;

@Component
public class TelegramBotImpl extends TelegramLongPollingBot {

    private final SampoBotServiceClient sampoBotServiceClient;

    @Value("${bot.username}")
    private String botUserName;

    public TelegramBotImpl(@Value("${bot.token}") String token,
                           SampoBotServiceClient sampoBotServiceClient) {
        super(token);
        this.sampoBotServiceClient = sampoBotServiceClient;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        // TODO: 09.08.2024
        long chatId = update.getMessage().getChatId();
        String response = sampoBotServiceClient.greet(chatId);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();
        execute(sendMessage);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
