package home.eugene.pikflatsbot.dto;

public enum BookingStatus {
  ACTIVE ("Доступно"),
  RESERVE ("Зарезервировано"),
  DISABLE ("Недоступно");

  String statusText;

  BookingStatus(String statusText) {
    this.statusText = statusText;
  }

  public String getStatusText() {
    return statusText;
  }
}
