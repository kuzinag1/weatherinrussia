package ru.dwerd.weather.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface WeatherOtherServices extends WeatherService {
    SendMessage handle(long chatId, Message message);
}
