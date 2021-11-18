package ru.dwerd.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.dwerd.weather.bot.WeatherTelegramBot;
import ru.dwerd.weather.feign.WeatherFeignClient;


@RestController
@RequiredArgsConstructor
public class BotController {
    private final WeatherTelegramBot telegramBot;
    @PostMapping("/")
    @SneakyThrows
    public BotApiMethod<?> getWeather(@RequestBody Update update) {
     return telegramBot.onWebhookUpdateReceived(update);
    }
}
