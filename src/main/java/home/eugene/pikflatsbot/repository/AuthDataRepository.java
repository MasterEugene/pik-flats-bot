package home.eugene.pikflatsbot.repository;

import home.eugene.pikflatsbot.entity.AuthDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthDataRepository extends JpaRepository<AuthDataEntity, Long> {
}
