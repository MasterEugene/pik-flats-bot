package home.eugene.pikflatsbot.model;

public class AuthToken {
  String login;
  String token;
  Long timeExpired;

  public AuthToken(String login, String token, Long timeExpired) {
    this.login = login;
    this.token = token;
    this.timeExpired = timeExpired;
  }

  public String getLogin() {
    return login;
  }

  public String getToken() {
    return token;
  }

  public Long getTimeExpired() {
    return timeExpired;
  }


}
