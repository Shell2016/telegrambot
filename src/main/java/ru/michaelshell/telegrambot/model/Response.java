package ru.michaelshell.telegrambot.model;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public record Response(Long chatId,
                       Integer messageId,
                       String message,
                       ResponseType type,
                       ReplyKeyboard keyboard) {
}
