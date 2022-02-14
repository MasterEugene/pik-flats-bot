package home.eugene.pikflatsbot.config;

import home.eugene.pikflatsbot.bot.BotResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
  public JerseyConfig() {
    register(BotResource.class);
  }
}
