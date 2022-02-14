package home.eugene.pikflatsbot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegrambot")
public class TelegramBotProperties {
  String webHookPath;
  String userName;
  String botToken;

  public String getWebHookPath() {
    return webHookPath;
  }

  public void setWebHookPath(String webHookPath) {
    this.webHookPath = webHookPath;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getBotToken() {
    return botToken;
  }

  public void setBotToken(String botToken) {
    this.botToken = botToken;
  }
}

