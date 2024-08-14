package ru.michaelshell.telegrambot.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.michaelshell.telegrambot.model.Response;
import ru.michaelshell.telegrambot.model.ResponseType;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseSenderTest {

    @Mock
    private TelegramClient telegramClient;

    @InjectMocks
    private ResponseSender responseSender;

    @Test
    void sendResponse_TelegramClientShouldCallExecuteAsync_IfResponseTypeSendTextMessageAsync() throws TelegramApiException {
        Response response = Response.builder()
                .type(ResponseType.SEND_TEXT_MESSAGE_WITH_KEYBOARD_ASYNC)
                .chatId(1L)
                .message("message")
                .build();

        responseSender.sendResponse(response);

        verify(telegramClient).executeAsync(any(SendMessage.class));
    }

    @Test
    void sendResponse_TelegramClientShouldCallExecute_IfResponseTypeSendTextMessage() throws TelegramApiException {
        Response response = Response.builder()
                .type(ResponseType.SEND_TEXT_MESSAGE)
                .chatId(1L)
                .message("message")
                .build();

        responseSender.sendResponse(response);

        verify(telegramClient).execute(any(SendMessage.class));
    }

    @Test
    void sendResponse_TelegramClientShouldCallExecute_IfResponseTypeSendTextMessageWithKeyboard() throws TelegramApiException {
        Response response = Response.builder()
                .type(ResponseType.SEND_TEXT_MESSAGE_WITH_KEYBOARD)
                .chatId(1L)
                .message("message")
                .build();

        responseSender.sendResponse(response);

        verify(telegramClient).execute(any(SendMessage.class));
    }

    @Test
    void sendResponse_TelegramClientShouldCallExecute_IfResponseTypeEditTextMessage() throws TelegramApiException {
        Response response = Response.builder()
                .type(ResponseType.EDIT_TEXT_MESSAGE)
                .chatId(1L)
                .message("message")
                .build();

        responseSender.sendResponse(response);

        verify(telegramClient).execute(any(EditMessageText.class));
    }

    @Test
    void sendResponse_TelegramClientShouldCallExecute_IfResponseTypeEditTextMessageWithKeyboard() throws TelegramApiException {
        Response response = Response.builder()
                .type(ResponseType.EDIT_TEXT_MESSAGE_WITH_KEYBOARD)
                .chatId(1L)
                .message("message")
                .build();

        responseSender.sendResponse(response);

        verify(telegramClient).execute(any(EditMessageText.class));
    }

    @Test
    void sendResponse_ShouldNotThrowAnyException_IfTelegramClientThrowsException() throws TelegramApiException {
        Response response = Response.builder()
                .type(ResponseType.EDIT_TEXT_MESSAGE_WITH_KEYBOARD)
                .chatId(1L)
                .message("message")
                .build();
        when(telegramClient.execute(any(EditMessageText.class))).thenThrow(TelegramApiException.class);

        assertThatNoException().isThrownBy(() -> responseSender.sendResponse(response));

        verify(telegramClient).execute(any(EditMessageText.class));
    }
}
