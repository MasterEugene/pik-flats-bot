package home.eugene.pikflatsbot.service;

import home.eugene.pikflatsbot.dto.BookingStatus;
import home.eugene.pikflatsbot.dto.BuildingDto;
import home.eugene.pikflatsbot.dto.FlatDto;
import home.eugene.pikflatsbot.dto.ProjectDto;
import home.eugene.pikflatsbot.entity.SubscriptionCarEntity;
import home.eugene.pikflatsbot.model.AuthToken;
import home.eugene.pikflatsbot.repository.SubscriptionCarRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
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

  public List<SendMessage> checkSubscriptionUpdates() {
    List<SubscriptionCarEntity> all = subscriptionCarRepository.findAll();
    var subsByChat = all.stream().collect(groupingBy(SubscriptionCarEntity::getChatId));
    return subsByChat.entrySet().stream()
        .map(e -> getMessageForChatId(e.getKey(), e.getValue(), true))
        .filter(Objects::nonNull)
        .toList();
  }

  public BotApiMethod<?> processMessage(Update update) {
    if (update.getMessage() == null) {
      logger.warn("message is null update: {}", update);
      return null;
    }
    Long chatId = update.getMessage().getChatId();
    List<SubscriptionCarEntity> subscriptions = subscriptionCarRepository.findByChatId(chatId);
    return getMessageForChatId(chatId, subscriptions, false);
  }

  public SendMessage getMessageForChatId(Long chatId, List<SubscriptionCarEntity> subscriptions, boolean onlyChanged) {
    AuthToken token = pikTokenService.getToken(chatId);
    if (token == null) {
      return new SendMessage(chatId.toString(), "Отсутствуют данные авторизации на pik.ru");
    }
    var subsByProject = subscriptions.stream().collect(groupingBy(SubscriptionCarEntity::getProjectCode));
    StringBuilder result = new StringBuilder();

    List<String> stringList = subsByProject.entrySet().stream()
        .map(e -> getFlatsByProject(e.getKey(), token, e.getValue(), onlyChanged))
        .filter(Objects::nonNull)
        .toList();
    if (stringList.isEmpty()) {
      return null;
    }
    if (onlyChanged) {
      result.append("ОБНОВЛЕНИЕ!!!").append("\n").append("\n");
    }
    stringList.forEach(result::append);
    return new SendMessage(chatId.toString(), result.toString());
  }

  private String getFlatsByProject(String projectCode, AuthToken token, List<SubscriptionCarEntity> subscriptions, boolean onlyChanged) {
    ProjectDto carsInfo = carFlatsService.getCarsInfo(projectCode, token.getToken());
    var subsByBuilding = subscriptions.stream().collect(groupingBy(SubscriptionCarEntity::getBuilding));
    Map<Integer, List<FlatDto>> flatsByBuilding = subsByBuilding.entrySet().stream()
        .collect(toMap(Map.Entry::getKey, e -> getFlatsByBuilding(carsInfo, e.getValue(), e.getKey(), onlyChanged)));

    flatsByBuilding.entrySet().removeIf(e -> e.getValue().isEmpty());

    if (flatsByBuilding.isEmpty()){
      return null;
    }

    StringBuilder result = new StringBuilder();
    result.append("Машиноместа в ").append(projectCode).append("\n");

    flatsByBuilding.forEach((key, value) -> {
      Optional<BuildingDto> buildingDtoOptional = carsInfo.buildings().stream().filter(b -> b.id().equals(key)).findFirst();
      buildingDtoOptional.ifPresent(buildingDto -> result.append(buildingDto.name()).append("\n"));
      result.append(generateTextForFlats(value)).append("\n");
    });
    result.append("\n");
    return result.toString();
  }

  private List<FlatDto> getFlatsByBuilding(ProjectDto carsInfo, List<SubscriptionCarEntity> subscriptions, Integer buildingId, boolean onlyChanged) {
    Optional<BuildingDto> buildingDtoOptional = carsInfo.buildings().stream()
        .filter(b -> b.id().equals(buildingId))
        .findFirst();
    if (buildingDtoOptional.isEmpty()) {
      return List.of();
    }
    BuildingDto buildingDto = buildingDtoOptional.get();
    var flatsByNumber = buildingDto.flats().stream().collect(toMap(FlatDto::number, identity()));

    List<FlatDto> flatDtos = subscriptions.stream()
        .filter(s -> !onlyChanged || isNotEqualsStates(flatsByNumber.get(s.getFlatNumber()), s))
        .map(s -> flatsByNumber.getOrDefault(s.getFlatNumber(), new FlatDto(-1, "guid", -1, s.getFlatNumber(), BookingStatus.REMOVED, -1L)))
        .toList();

    updateSubscriptions(subscriptions, flatsByNumber);
    return flatDtos;
  }

  private boolean isNotEqualsStates(FlatDto flatDto, SubscriptionCarEntity subscription) {
    return flatDto == null ||
        !Objects.equals(flatDto.bookingStatus().name(), subscription.getBookingStatus()) ||
        !Objects.equals(flatDto.price(), subscription.getPrice());
  }

  private void updateSubscriptions(List<SubscriptionCarEntity> subscriptions, Map<String, FlatDto> flatsByNumber) {
    List<SubscriptionCarEntity> changedSubscriptions = subscriptions.stream()
        .filter(s -> isNotEqualsStates(flatsByNumber.get(s.getFlatNumber()), s))
        .peek(s -> {
          FlatDto flatDto = flatsByNumber.get(s.getFlatNumber());
          s.setBookingStatus(flatDto.bookingStatus().name());
          s.setPrice(flatDto.price());
        }).toList();
    subscriptionCarRepository.saveAll(changedSubscriptions);
  }

  private String generateTextForFlats(List<FlatDto> flats) {
    StringBuilder sb = new StringBuilder();
    flats.forEach(f -> {
      sb.append("    Место ").append(f.number()).append(": ").append(f.bookingStatus().getStatusText()).append(" Цена: ").append(f.price())
          .append("\n");
    });
    return sb.toString();
  }
}
