package home.eugene.pikflatsbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import home.eugene.pikflatsbot.model.PikCarsInfo;
import home.eugene.pikflatsbot.model.PikCarsInfo.*;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class PikModelJsonTest {
  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  public void jsonDeserializeCarsTest() throws JsonProcessingException {
    String s = "{\n" +
        "   \"nonResidential\":{\n" +
        "      \"bulks\":[\n" +
        "         {\n" +
        "            \"id\":7746,\n" +
        "            \"guid\":\"85f74f5c-3569-4173-ac0c-7d465dc2d580\",\n" +
        "            \"house_guid\":\"0f856085-c320-4774-a05c-507c1f55cb63\",\n" +
        "            \"name\":\"Корпус 10\"\n" +
        "         },\n" +
        "         {\n" +
        "            \"id\":8735,\n" +
        "            \"guid\":\"4d5b065f-5676-4be9-b32d-9d7193988185\",\n" +
        "            \"house_guid\":\"b1c751ff-eb0e-4eab-b2e8-747d84cc0537\",\n" +
        "            \"name\":\"Корпус 12\"\n" +
        "         }\n" +
        "      ],\n" +
        "      \"objects\":{\n" +
        "         \"list\":{\n" +
        "            \"18068_-1\":{\n" +
        "               \"bulkId\":7746,\n" +
        "               \"bulkName\":\"Корпус 10\",\n" +
        "               \"sectionName\":\"Секция 1\",\n" +
        "               \"level\":\"-1\",\n" +
        "               \"flats\":[\n" +
        "                  {\n" +
        "                     \"id\":689660,\n" +
        "                     \"guid\":\"008702f1-4e36-4b97-b9d6-cfb3345f50dc\",\n" +
        "                     \"type_id\":5,\n" +
        "                     \"price\":1799000,\n" +
        "                     \"area\":13.3,\n" +
        "                     \"apartment_number\":\"219\",\n" +
        "                     \"number\":\"219\",\n" +
        "                     \"bookingStatus\":\"disable\"\n" +
        "                  }\n" +
        "               ],\n" +
        "               \"plans\":\"//0.db-estate.cdn.pik-service.ru/section_plan/2020/11/04/10_-1_36xz2xfC1x38qcJS.png\",\n" +
        "               \"plan\":\"//0.db-estate.cdn.pik-service.ru/section_plan/floor_plan_svg/2021/04/05/10_-1_J6uCo3YG6JqfLu5o.svg\",\n" +
        "               \"planRx\":null,\n" +
        "               \"relationship\":false,\n" +
        "               \"price\":1799000,\n" +
        "               \"min_area_on_floor\":13.3\n" +
        "            },\n" +
        "            \"20101_-1\":{\n" +
        "               \"bulkId\":8735,\n" +
        "               \"bulkName\":\"Корпус 12\",\n" +
        "               \"sectionName\":\"Секция 5\",\n" +
        "               \"level\":\"-1\",\n" +
        "               \"flats\":[\n" +
        "                  {\n" +
        "                     \"id\":784757,\n" +
        "                     \"guid\":\"0e733e62-c388-4e85-8fec-c6f7fe124929\",\n" +
        "                     \"type_id\":5,\n" +
        "                     \"isAuction\":false,\n" +
        "                     \"booking\":null,\n" +
        "                     \"price\":0,\n" +
        "                     \"number\":\"м9\",\n" +
        "                     \"bookingStatus\":\"disable\",\n" +
        "                     \"hasKlaus\":0\n" +
        "                  }\n" +
        "               ],\n" +
        "               \"plans\":null,\n" +
        "               \"plan\":\"//0.db-estate.cdn.pik-service.ru/section_plan/floor_plan_svg/2021/12/28/12_-1_uE1k4dxCV8PSZ6pX.svg\",\n" +
        "               \"planRx\":null,\n" +
        "               \"relationship\":true,\n" +
        "               \"price\":2226000,\n" +
        "               \"min_area_on_floor\":26.6\n" +
        "            }\n" +
        "         },\n" +
        "         \"ids\":[\n" +
        "            \"18068_-1\",\n" +
        "            \"20101_-1\"\n" +
        "         ],\n" +
        "         \"idsWithPlans\":[\n" +
        "            \"18068_-1\",\n" +
        "            \"20101_-1\"\n" +
        "         ]\n" +
        "      },\n" +
        "      \"current\":{\n" +
        "         \"id\":320,\n" +
        "         \"guid\":\"1c7b76f2-2b50-11e8-ac2a-001ec9d8c6a2\",\n" +
        "         \"name\":\"Holland park\",\n" +
        "         \"name_prepositional\":\"\",\n" +
        "         \"url\":\"/hp\",\n" +
        "         \"isPremium\":false\n" +
        "      }\n" +
        "   }\n" +
        "}\n";
    mapper.readValue(s, PikCarsInfo.class);
  }

  @Test
  public void jsonSerializeCarsTest() throws JsonProcessingException {
    PikCarsInfo pikCarsInfo = new PikCarsInfo(
        new NonResidential(List.of(new BulkInfo(1, "sds")),
            new Objects(
                Map.of("sa",
                    new Bulk(1, "s", "s", "s", List.of(
                        new Flat(1, "d", 3, 34L, "33", "233"))
                    )
                )
            ),
            new Project(2, "545", "343", "/ete")
        )
    );
    String s = mapper.writeValueAsString(pikCarsInfo);
    System.out.println(s);
  }
}
