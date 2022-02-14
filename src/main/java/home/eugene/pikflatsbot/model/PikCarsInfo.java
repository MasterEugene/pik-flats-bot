package home.eugene.pikflatsbot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PikCarsInfo(NonResidential nonResidential) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static record NonResidential(List<BulkInfo> bulks,
                                      Objects objects,
                                      Project current){ }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static record BulkInfo(Integer id, String guid) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static record Objects(Map<String, Bulk> list) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static record Bulk(Integer bulkId, String bulkName, String sectionName, String level, List<Flat> flats) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static record Flat(Integer id, String guid, Integer type_id, Long price, String number, String bookingStatus) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static record Project(Integer id, String guid, String name, String url) {}
}