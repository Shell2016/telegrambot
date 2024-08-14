package ru.michaelshell.telegrambot.model;

import lombok.Builder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Builder
public record Response(Long chatId,
                       Integer messageId,
                       String message,
                       ResponseType type,
                       ReplyKeyboard keyboard) {
}
