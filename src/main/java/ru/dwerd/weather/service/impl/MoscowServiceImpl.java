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
import ru.dwerd.weather.service.WeatherMoscowService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MoscowServiceImpl implements WeatherMoscowService {

    private final WeatherFeignClient weatherFeignClient;
    private final InlineKeyboardMarkup inlineMessageButtons;
    private final String yandexApiKey;
    @Override
    public SendMessage handle(final long chatId) {
        Weather weather = weatherFeignClient.getWeather(yandexApiKey,"55.75396","37.620393",true);
        String meaasageWeather = getWeatherMoscowNowFromYandexApiMessage(weather.getFact(),weather);
        SendMessage sendMessage =new SendMessage(String.valueOf(chatId),meaasageWeather);
        sendMessage.setReplyMarkup(inlineMessageButtons);
        return sendMessage;
    }

    @Override
    public SendMessage handle(Message message) {
        final long chatId = message.getChatId();
        Weather weather = weatherFeignClient.getWeather(yandexApiKey,"55.75396","37.620393",true);
        String meaasageWeather = getWeatherMoscowNowFromYandexApiMessage(weather.getFact(),weather);
        SendMessage replyToUser = new SendMessage(String.valueOf(chatId),meaasageWeather);
        replyToUser.setReplyMarkup(inlineMessageButtons);
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MOSCOW;
    }

    private String getWeatherMoscowNowFromYandexApiMessage(Fact fact, Weather weather) {
        StringBuilder weatherStringBuilder = new StringBuilder();
        weatherStringBuilder.append("???????????? ?? ???????????? ????????????:\n");
        weatherStringBuilder.append("??????????????????????: ").append(fact.getTemp()).append("??C\n");
        weatherStringBuilder.append("?????????????????? ??????????????????????: ").append(fact.getFeelsLike()).append("??C\n");
        Condition condition = Condition.valueOf(weather.getFact().getCondition().toUpperCase(Locale.ROOT));
        weatherStringBuilder.append("???????????????? ????????????????: ").append(condition.getCondition()).append("\n");
        weatherStringBuilder.append("???????????????? (?? ???? ????. ????.): ").append(fact.getPressureMm()).append("\n");
        weatherStringBuilder.append("?????????????????? ??????????????: ").append(fact.getHumidity()).append("%\n");
        weatherStringBuilder.append("???????? ????????: ").append(weather.getForecastsList().get(0).getMoonCodeInText()).append("\n");
        weatherStringBuilder.append("???????????? ????????????: ").append(weather.getForecastsList().get(0).getSunrise()).append("\n");
        weatherStringBuilder.append("?????????? ????????????: ").append(weather.getForecastsList().get(0).getSunset());
        return  weatherStringBuilder.toString();
    }
}
