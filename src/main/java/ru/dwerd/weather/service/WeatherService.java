package ru.dwerd.weather.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dwerd.weather.bot.config.BotState;

public interface WeatherService {
    SendMessage handle(Message message);

    BotState getHandlerName();
}
