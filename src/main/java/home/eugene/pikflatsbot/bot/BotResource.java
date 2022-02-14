package home.eugene.pikflatsbot.bot;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Path("/")
@Controller
public class BotResource {

  private final PikBot pikBot;

  public BotResource(PikBot pikBot) {
    this.pikBot = pikBot;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public BotApiMethod<?> onUpdate(Update update) {
    return pikBot.onWebhookUpdateReceived(update);
  }
}
