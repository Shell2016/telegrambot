package ru.michaelshell.telegrambot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.michaelshell.telegrambot.model.Response;

@Component
@Slf4j
public class ResponseSender {

    private final TelegramClient telegramClient;

    public ResponseSender(@Value("${bot.token}") String token) {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    public void sendResponse(Response response) {
        switch (response.type()) {
            case SEND_TEXT_MESSAGE -> sendTextMessage(response.chatId(), response.message());
            case SEND_TEXT_MESSAGE_WITH_KEYBOARD ->
                    sendTextMessageWithKeyboard(response.chatId(), response.message(), response.keyboard());
            case EDIT_TEXT_MESSAGE -> editTextMessage(response.chatId(), response.messageId(), response.message());
            case EDIT_TEXT_MESSAGE_WITH_KEYBOARD ->
                    editTextMessageWithKeyboard(response.chatId(), response.messageId(), response.message(), response.keyboard());
            case SEND_TEXT_MESSAGE_WITH_KEYBOARD_ASYNC ->
                sendTextMessageWithKeyboardAsync(response.chatId(), response.message(), response.keyboard());
        }
    }

    private void sendTextMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendTextMessageWithKeyboard(Long chatId, String message, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage(chatId.toString(), message);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboard);
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendTextMessageWithKeyboardAsync(Long chatId, String message, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage(chatId.toString(), message);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboard);
        try {
            telegramClient.executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void editTextMessage(Long chatId, Integer messageId, String msg) {
        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(msg)
                .build();
        try {
            telegramClient.execute(editMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void editTextMessageWithKeyboard(Long chatId, Integer messageId, String msg, ReplyKeyboard keyboard) {
        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(msg)
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup((InlineKeyboardMarkup) keyboard)
                .build();
        try {
            telegramClient.execute(editMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
