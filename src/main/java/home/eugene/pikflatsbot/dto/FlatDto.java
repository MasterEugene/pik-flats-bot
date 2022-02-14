package home.eugene.pikflatsbot.dto;

public record FlatDto(Integer id, String guid, Integer type, String number, BookingStatus bookingStatus, Long price) {}
