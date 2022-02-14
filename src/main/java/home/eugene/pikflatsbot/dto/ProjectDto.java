package home.eugene.pikflatsbot.dto;

import java.util.List;

public record ProjectDto(Integer id, String guid, String code, String name, List<BuildingDto> buildings) {}
