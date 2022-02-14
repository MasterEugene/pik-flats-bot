package home.eugene.pikflatsbot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "subscription_cars")
public class SubscriptionCarEntity {

  public SubscriptionCarEntity() {
  }

  public SubscriptionCarEntity(Integer id, Long chatId, String projectCode, Integer building, String flatNumber) {
    this.id = id;
    this.chatId = chatId;
    this.projectCode = projectCode;
    this.building = building;
    this.flatNumber = flatNumber;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "chat_id", nullable = false)
  private Long chatId;

  @Column(name = "project_code", nullable = false)
  private String projectCode;

  @Column(name = "building", nullable = false)
  private Integer building;

  @Column(name = "flat_number", nullable = false)
  private String flatNumber;

  public Integer getId() {
    return id;
  }

  public Long getChatId() {
    return chatId;
  }

  public String getProjectCode() {
    return projectCode;
  }

  public Integer getBuilding() {
    return building;
  }

  public String getFlatNumber() {
    return flatNumber;
  }
}
