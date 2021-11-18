package ru.dwerd.weather.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.dwerd.weather.bot.config.BotState;
import ru.dwerd.weather.feign.WeatherFeignClient;
import ru.dwerd.weather.model.Condition;
import ru.dwerd.weather.model.Fact;
import ru.dwerd.weather.model.Weather;
import ru.dwerd.weather.service.WeatherOtherServices;

import java.util.Locale;
@Service
@RequiredArgsConstructor
public class WeatherOtherServiceImpl implements WeatherOtherServices {
    private final WeatherFeignClient weatherFeignClient;
    private final InlineKeyboardMarkup inlineMessageButtons;
    private final String yandexApiKey;
    @Override
    public SendMessage handle(Message message) {
        Weather weather = weatherFeignClient.getWeather(yandexApiKey,String.valueOf(message.getLocation().getLatitude()), String.valueOf( message.getLocation().getLongitude()),true);
        String meaasageWeather = getWeatherSaintPersburgNowFromYandexApiMessage(weather.getFact(),weather);
        SendMessage sendMessage =new SendMessage(String.valueOf(message.getChatId()),meaasageWeather);
        sendMessage.setReplyMarkup(inlineMessageButtons);
        return sendMessage;
    }


    @Override
    public BotState getHandlerName() {
        return BotState.OTHER_CITY;
    }
    private String getWeatherSaintPersburgNowFromYandexApiMessage(Fact fact, Weather weather) {
        StringBuilder weatherStringBuilder = new StringBuilder();
        weatherStringBuilder.append("Погода в вашем городе сейчас:\n");
        weatherStringBuilder.append("Температура: ").append(fact.getTemp()).append("°C\n");
        weatherStringBuilder.append("Ощущаемая температура: ").append(fact.getFeelsLike()).append("°C\n");
        Condition condition = Condition.valueOf(weather.getFact().getCondition().toUpperCase(Locale.ROOT));
        weatherStringBuilder.append("Погодное описание: ").append(condition.getCondition()).append("\n");
        weatherStringBuilder.append("Давление (в мм рт. ст.): ").append(fact.getPressureMm()).append("\n");
        weatherStringBuilder.append("Влажность воздуха: ").append(fact.getHumidity()).append("%\n");
        weatherStringBuilder.append("Фазы Луны: ").append(weather.getForecastsList().get(0).getMoonCodeInText()).append("\n");
        weatherStringBuilder.append("Восход Солнца: ").append(weather.getForecastsList().get(0).getSunrise()).append("\n");
        weatherStringBuilder.append("Закат Солнца: ").append(weather.getForecastsList().get(0).getSunset());
        return  weatherStringBuilder.toString();
    }

    @Override
    public SendMessage handle(long chatId, Message message) {
        Weather weather = weatherFeignClient.getWeather(yandexApiKey,String.valueOf(message.getLocation().getLatitude()), String.valueOf( message.getLocation().getLongitude()),true);
        String meaasageWeather = getWeatherSaintPersburgNowFromYandexApiMessage(weather.getFact(),weather);
        SendMessage sendMessage =new SendMessage(String.valueOf(message.getChatId()),meaasageWeather);
        sendMessage.setReplyMarkup(inlineMessageButtons);
        return sendMessage;
    }
}
