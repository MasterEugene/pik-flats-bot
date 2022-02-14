package home.eugene.pikflatsbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import home.eugene.pikflatsbot.dto.BookingStatus;
import home.eugene.pikflatsbot.dto.BuildingDto;
import home.eugene.pikflatsbot.dto.FlatDto;
import home.eugene.pikflatsbot.dto.ProjectDto;
import home.eugene.pikflatsbot.model.PikCarsInfo;
import static home.eugene.pikflatsbot.utils.Parser.parseCarsPage;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CarFlatsService {
  private static final String PIK_TOKEN_HEADER = "PIK_TOKEN";
  private static final ObjectMapper mapper = new ObjectMapper();
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${pik.carsUrl}")
  private String carsUrl;

  private final RestTemplate restTemplate;

  public CarFlatsService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ProjectDto getCarsInfo(String projectCode, String token){
    HttpCookie cookie = new HttpCookie(PIK_TOKEN_HEADER, token);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", cookie.toString());

    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    var response = restTemplate.exchange(String.format(carsUrl, projectCode), HttpMethod.GET, entity, String.class, Map.of());
    if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
      throw new RuntimeException("Can't get carsInfo from " + projectCode);
    }
    String jsonString = parseCarsPage(response.getBody());
    try {
      PikCarsInfo pikCarsInfo = mapper.readValue(jsonString, PikCarsInfo.class);
      return convert(pikCarsInfo);
    } catch (JsonProcessingException e) {
      logger.error("Can't parse carsInfo json");
      logger.error(jsonString);
      throw new RuntimeException(e);
    }
  }

  private ProjectDto convert(PikCarsInfo pikCarsInfo) {
    var projectInfo = pikCarsInfo.nonResidential().current();
    var bulkGuids = pikCarsInfo.nonResidential().bulks().stream()
        .collect(toMap(PikCarsInfo.BulkInfo::id, PikCarsInfo.BulkInfo::guid));

    List<BuildingDto> buildings = pikCarsInfo.nonResidential().objects().list().values().stream()
        .map(b -> new BuildingDto(b.bulkId(), b.bulkName() + " level " + b.level(), bulkGuids.get(b.bulkId()), convert(b.flats())))
        .toList();

    return new ProjectDto(projectInfo.id(), projectInfo.guid(), projectInfo.url().substring(1), projectInfo.name(), buildings);
  }

  private List<FlatDto> convert(List<PikCarsInfo.Flat> flatsInfo) {
    return flatsInfo.stream()
        .map(f -> new FlatDto(f.id(), f.guid(), f.type_id(), f.number(), BookingStatus.valueOf(f.bookingStatus().toUpperCase()), f.price()))
        .toList();
  }
}
