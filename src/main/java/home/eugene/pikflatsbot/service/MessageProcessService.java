package home.eugene.pikflatsbot.service;

import home.eugene.pikflatsbot.dto.BuildingDto;
import home.eugene.pikflatsbot.dto.FlatDto;
import home.eugene.pikflatsbot.dto.ProjectDto;
import home.eugene.pikflatsbot.entity.SubscriptionCarEntity;
import home.eugene.pikflatsbot.model.AuthToken;
import home.eugene.pikflatsbot.repository.SubscriptionCarRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageProcessService {
  Logger logger = LoggerFactory.getLogger(getClass());


  private final SubscriptionCarRepository subscriptionCarRepository;
  private final PikTokenService pikTokenService;
  private final CarFlatsService carFlatsService;

  public MessageProcessService(SubscriptionCarRepository subscriptionCarRepository,
                               PikTokenService pikTokenService,
                               CarFlatsService carFlatsService) {
    this.subscriptionCarRepository = subscriptionCarRepository;
    this.pikTokenService = pikTokenService;
    this.carFlatsService = carFlatsService;
  }

  public BotApiMethod<?> processMessage(Update update) {
    if (update.getMessage() == null) {
      logger.warn("message is null update: {}", update);
      return null;
    }
    Long chatId = update.getMessage().getChatId();

    AuthToken token = pikTokenService.getToken(chatId);
    if (token == null) {
      return new SendMessage(chatId.toString(), "Отсутствуют данные авторизации на pik.ru");
    }

    List<SubscriptionCarEntity> subscriptions = subscriptionCarRepository.findByChatId(chatId);
    var subsByProject = subscriptions.stream().collect(groupingBy(SubscriptionCarEntity::getProjectCode));
    StringBuilder result = new StringBuilder();
    subsByProject.forEach((key, value) -> {
      result.append(getFlatsByProject(key, token, value));
    });
    return new SendMessage(chatId.toString(), result.toString());
  }

  private String getFlatsByProject(String projectCode, AuthToken token, List<SubscriptionCarEntity> subscriptions) {
    var subsByBuilding = subscriptions.stream().collect(groupingBy(SubscriptionCarEntity::getBuilding));
    ProjectDto carsInfo = carFlatsService.getCarsInfo(projectCode, token.getToken());
    StringBuilder result = new StringBuilder();
    result.append("Машиноместа в ").append(projectCode).append("\n");
    subsByBuilding.forEach((key, value) -> {
      Optional<BuildingDto> buildingDtoOptional = carsInfo.buildings().stream().filter(b -> b.id().equals(key)).findFirst();
      buildingDtoOptional.ifPresent(buildingDto -> result.append(buildingDto.name()).append("\n"));
      result.append(generateTextForFlats(getFlatsByBuilding(carsInfo, value, key))).append("\n");
    });
    result.append("\n");
    return result.toString();
  }

  private List<FlatDto> getFlatsByBuilding(ProjectDto carsInfo, List<SubscriptionCarEntity> subscriptions, Integer buildingId) {
    List<BuildingDto> buildingDtos = carsInfo.buildings().stream()
        .filter(b -> b.id().equals(buildingId))
        .toList();
    Set<String> flatNumbers = subscriptions.stream().map(SubscriptionCarEntity::getFlatNumber).collect(Collectors.toSet());
    return buildingDtos.stream()
        .flatMap(b -> b.flats().stream())
        .filter(f -> flatNumbers.contains(f.number()))
        .toList();
  }

  private String generateTextForFlats(List<FlatDto> flats) {
    StringBuilder sb = new StringBuilder();
    flats.forEach(f -> {
      sb.append("    Место ").append(f.number()).append(": ").append(f.bookingStatus().getStatusText()).append("\n");
    });
    return sb.toString();
  }
}
