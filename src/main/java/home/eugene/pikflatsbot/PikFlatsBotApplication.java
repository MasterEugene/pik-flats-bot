package home.eugene.pikflatsbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PikFlatsBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(PikFlatsBotApplication.class, args);
  }

}
