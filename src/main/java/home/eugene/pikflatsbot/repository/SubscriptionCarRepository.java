package home.eugene.pikflatsbot.repository;

import home.eugene.pikflatsbot.entity.SubscriptionCarEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionCarRepository extends JpaRepository<SubscriptionCarEntity, Integer> {
  List<SubscriptionCarEntity> findByChatId(Long chatId);
}
