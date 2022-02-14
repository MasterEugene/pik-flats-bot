package home.eugene.pikflatsbot.service;

import home.eugene.pikflatsbot.entity.AuthDataEntity;
import home.eugene.pikflatsbot.model.AuthToken;
import home.eugene.pikflatsbot.model.PikAuthRequestObject;
import home.eugene.pikflatsbot.model.PikAuthToken;
import home.eugene.pikflatsbot.repository.AuthDataRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class PikTokenService {
  Logger logger = LoggerFactory.getLogger(getClass());
  private final Map<String, AuthToken> tokenMap = new ConcurrentHashMap<>();

  private final RestTemplate restTemplate;
  private final AuthDataRepository authDataRepository;

  @Value("${pik.api.authUrl}")
  private String authUrl;


  public PikTokenService(RestTemplate restTemplate, AuthDataRepository authDataRepository) {
    this.restTemplate = restTemplate;
    this.authDataRepository = authDataRepository;
  }

  @Transactional
  public AuthToken getToken(Long chatId) {
    AuthDataEntity authData = getAuthData(chatId);
    if (authData == null) {
      return null;
    }

    return tokenMap.compute(authData.getLogin(), (key, oldValue) -> {
      if (oldValue != null && oldValue.getTimeExpired() > System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)) {
        return oldValue;
      }
      return createToken(key, authData.getPassword());
    });
  }

  private AuthToken createToken(String login, String password) {
    var authTokenResponse = restTemplate.postForEntity(authUrl, new PikAuthRequestObject(login, password), PikAuthToken.class, Map.of());
    if(authTokenResponse.getStatusCode() == HttpStatus.OK) {
      PikAuthToken pikAuthToken = authTokenResponse.getBody();
      if (pikAuthToken == null) {
        throw new RuntimeException("Auth token body is null for " + login);
      }
      return new AuthToken(login, pikAuthToken.token(), TimeUnit.SECONDS.toMillis(pikAuthToken.expiresIn()));
    }
    throw new RuntimeException(String.format("Pik auth api return status %s for user %s", authTokenResponse.getStatusCodeValue(), login));
  }

  private AuthDataEntity getAuthData(Long chatId) {
    try {
      return authDataRepository.getById(chatId);
    } catch (EntityNotFoundException e) {
      logger.info("Empty auth data for chat {}", chatId);
      return null;
    }
  }
}
