package home.eugene.pikflatsbot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "auth_data")
public class AuthDataEntity {

  public AuthDataEntity() {
  }

  public AuthDataEntity(Long chatId, String login, String password) {
    this.chatId = chatId;
    this.login = login;
    this.password = password;
  }

  @Id
  @Column(name = "chat_id", nullable = false)
  private Long chatId;

  @Column(name = "login", nullable = false)
  private String login;

  @Column(name = "password", nullable = false)
  private String password;

  public Long getChatId() {
    return chatId;
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }
}
