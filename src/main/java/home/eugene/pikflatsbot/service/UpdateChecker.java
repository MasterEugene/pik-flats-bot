package home.eugene.pikflatsbot.service;

import home.eugene.pikflatsbot.bot.PikBot;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UpdateChecker {
  private final PikBot pikBot;

  public UpdateChecker(PikBot pikBot) {
    this.pikBot = pikBot;
  }

  //@Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
  public void check() {
    pikBot.sendMessage("301781586", "check");
  }
}
