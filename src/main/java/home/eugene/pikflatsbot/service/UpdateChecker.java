package home.eugene.pikflatsbot.service;

import home.eugene.pikflatsbot.bot.PikBot;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UpdateChecker {
  private final PikBot pikBot;
  private final MessageProcessService messageProcessService;

  public UpdateChecker(PikBot pikBot, MessageProcessService messageProcessService) {
    this.pikBot = pikBot;
    this.messageProcessService = messageProcessService;
  }

  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
  public void check() {
    messageProcessService.checkSubscriptionUpdates()
        .forEach(pikBot::sendMessage);
  }
}
