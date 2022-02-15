package home.eugene.pikflatsbot;

import home.eugene.pikflatsbot.model.AuthToken;
import home.eugene.pikflatsbot.service.PikTokenService;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PikTokenServiceTest {

  @Inject
  public PikTokenService pikTokenService;

  @Test
  public void authDataNotFoundTest() {
    AuthToken token = pikTokenService.getToken(-1L);
    assertNull(token);
  }

}
