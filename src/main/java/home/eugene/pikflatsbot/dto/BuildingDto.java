package home.eugene.pikflatsbot.dto;

import java.util.List;

public record BuildingDto(Integer id,
                          String name,
                          String guid,
                          List<FlatDto> flats) {}
