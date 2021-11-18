package ru.dwerd.weather.bot.api.facade;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.dwerd.weather.bot.config.BotState;
import ru.dwerd.weather.bot.config.context.BotStateContext;


import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class TelegramFacade {
    private final BotStateContext botStateContext;
    //private final UserDataCache userDataCache;


    public Optional<BotApiMethod<?>> handleUpdate(Update update) {
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data1: {}", update.getCallbackQuery().getFrom().getUserName(),
                callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


       else  if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
                message.getFrom().getUserName(), message.getChatId(), message.getText());
            Optional<SendMessage> optionalSendMessage = handleInputMessage(message);
            if (optionalSendMessage.isPresent()) {
                return Optional.of(optionalSendMessage.get());
            }
        }
        else if(update.getMessage().getLocation()!=null) {
            log.info("Отправка коррднатов");
            return Optional.ofNullable(botStateContext.processInputMessage(BotState.OTHER_CITY,update.getMessage()));
        }

        return Optional.empty();
    }

    private Optional<SendMessage> handleInputMessage(Message message) {
        String inputMsg = message.getText();
        final Long userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.MOSCOW;
                break;
            case "/Moscow":
            case "/moscow":
           // case "/today@hse_ebot":
                // case "СЕГОДНЯ":
                botState = BotState.MOSCOW;
                break;
            case "/Saint_Petersburg":
            case "/saint_petersburg":
                botState = BotState.SAINT_PETERSBURG;
                break;
            case "/other_city":
            case "/Other_city":
                botState = BotState.OTHER_CITY;
      /*      case "/Tomorrow":
            case "/tomorrow":
            case "/tomorrow@hse_ebot":
                botState = BotState.TOMMOROW;
                break;
            case "/week":
            case "/week@hse_ebot":
                botState = BotState.WEEK;
                break;
            case "/wmn":
            case "/wmn@hse_ebot":
                botState = BotState.WEATHER_TODAY;
                break;
            case "/wmtodtom":
            case "/wmtodtom@hse_ebot":
                botState = BotState.WEATHER_TODAY_AND_TOMORROW;
                break;
                */
            default:
                return Optional.empty();

        }

       // userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return Optional.of(replyMessage);
    }

    private Optional<BotApiMethod<?>> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final Long userId = buttonQuery.getFrom().getId();
        SendMessage replyMessage;
        switch (buttonQuery.getData()) {
            case "buttonMoscow":
              //  userDataCache.getUsersCurrentBotState(userId);
                replyMessage = botStateContext.processButton(BotState.MOSCOW, buttonQuery.getMessage(), chatId);
                return Optional.of(replyMessage);
            case "buttonPetersburg":
                replyMessage = botStateContext.processButton(BotState.SAINT_PETERSBURG, buttonQuery.getMessage(), chatId);
                return Optional.of(replyMessage);
            case "buttonOtherCity":
                replyMessage = botStateContext.processButton(BotState.OTHER_CITY, buttonQuery.getMessage(), chatId);
                return  Optional.of(replyMessage);
        }
          /*  case "buttonTomorrow":
                userDataCache.getUsersCurrentBotState(userId);
                replyMessage = botStateContext.processButton(BotState.TOMMOROW, chatId);
                return Optional.of(replyMessage);
            case "buttonWeek":
                userDataCache.getUsersCurrentBotState(userId);
                replyMessage = botStateContext.processButton(BotState.WEEK, chatId);
                return Optional.of(replyMessage);
            case "buttonWeatherToday":
                userDataCache.getUsersCurrentBotState(userId);
                replyMessage = botStateContext.processButton(BotState.WEATHER_TODAY, chatId);
                return Optional.of(replyMessage);
            case "buttonWeatherTodayAndTomorrow":
                userDataCache.getUsersCurrentBotState(userId);
                replyMessage = botStateContext.processButton(BotState.WEATHER_TODAY_AND_TOMORROW, chatId);
                return Optional.of(replyMessage);
        } */
        return Optional.empty();
    }
}