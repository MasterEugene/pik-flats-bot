package home.eugene.pikflatsbot.bot;

import home.eugene.pikflatsbot.properties.TelegramBotProperties;
import home.eugene.pikflatsbot.service.MessageProcessService;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class PikBot extends TelegramWebhookBot {
  Logger logger = LoggerFactory.getLogger(getClass());

  private final TelegramBotProperties botProperties;
  private final MessageProcessService messageProcessService;

  public PikBot(TelegramBotProperties botProperties, MessageProcessService messageProcessService) {
    super();
    this.botProperties = botProperties;
    this.messageProcessService = messageProcessService;
  }

  public void sendMessage(SendMessage message) {
    try {
      execute(message);
    } catch (TelegramApiException e) {
      logger.error("Cant send message " + message, e);
    }
  }

  @Override
  public String getBotUsername() {
    return "@" + botProperties.getUserName();
  }

  @Override
  public String getBotToken() {
    return botProperties.getBotToken();
  }

  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    return messageProcessService.processMessage(update);
  }

  @Override
  public String getBotPath() {
    return botProperties.getWebHookPath();
  }
}
