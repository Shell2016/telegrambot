package ru.michaelshell.telegrambot.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.michaelshell.telegrambot.client.BotServiceClient;
import ru.michaelshell.telegrambot.model.Response;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotImplTest {

    @Mock
    private BotServiceClient botServiceClient;
    @Mock
    private ResponseSender responseSender;
    @InjectMocks
    private TelegramBotImpl telegramBot;

    @Test
    void consumeEmptyUpdate_ShouldNotCallAnyService() {
        Update update = new Update();

        telegramBot.consume(update);

        verifyNoInteractions(botServiceClient);
        verifyNoInteractions(responseSender);
    }

    @Test
    void consumeUpdateWithMessage_ShouldCallClient() {
        Update update = new Update();
        update.setMessage(new Message());

        telegramBot.consume(update);

        verify(botServiceClient).processUpdate(update);
        verifyNoInteractions(responseSender);
    }

    @Test
    void consumeUpdateWithCallbackQuery_ShouldCallClient() {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());

        telegramBot.consume(update);

        verify(botServiceClient).processUpdate(update);
        verifyNoInteractions(responseSender);
    }

    @Test
    void consumeUpdateWithMessage_ShouldNotCallResponseSender_IfEmptyResponseList() {
        Update update = new Update();
        update.setMessage(new Message());
        when(botServiceClient.processUpdate(update)).thenReturn(Collections.emptyList());

        telegramBot.consume(update);

        verify(botServiceClient).processUpdate(update);
        verifyNoInteractions(responseSender);
    }

    @Test
    void consumeUpdateWithMessage_ShouldCallResponseSenderOneTime_IfResponseListHasLength1() {
        Update update = new Update();
        update.setMessage(new Message());
        Response response = Response.builder().build();
        when(botServiceClient.processUpdate(update)).thenReturn(List.of(response));

        telegramBot.consume(update);

        verify(botServiceClient).processUpdate(update);
        verify(responseSender, times(1)).sendResponse(response);
    }

    @Test
    void consumeUpdateWithMessage_ShouldCallResponseSenderTwoTimes_IfResponseListHasLength2() {
        Update update = new Update();
        update.setMessage(new Message());
        Response response = Response.builder().build();
        when(botServiceClient.processUpdate(update)).thenReturn(List.of(response, response));

        telegramBot.consume(update);

        verify(botServiceClient).processUpdate(update);
        verify(responseSender, times(2)).sendResponse(any(Response.class));
    }
}