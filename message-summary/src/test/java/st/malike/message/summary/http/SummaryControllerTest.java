package st.malike.message.summary.http;

import com.google.gson.Gson;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import st.malike.message.summary.MessageSummaryMain;
import st.malike.message.summary.util.Enums.JSONResponseMessage;

/**
 * @autor malike_st
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MessageSummaryMain.class)
@SpringBootTest
public class SummaryControllerTest {

  Map data;
  @Autowired
  private SummaryController summaryController;
  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Before
  public void setUp() throws Exception {

    data = new HashMap<>();

    RestAssuredMockMvc.standaloneSetup(summaryController);

  }

  @Test
  public void testSearchMessage() {
    data.put("param", "sample");
    RestAssuredMockMvc.given()
        .log().all().contentType("application/json")
        .body(new Gson().toJson(data))
        .when()
        .post("/message/search")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("status", Matchers.is(true))
        .body("message", Matchers.is("SUCCESS"));
  }

  @Test
  public void testSearchMessageMissingRequiredParam() {
    RestAssuredMockMvc.given()
        .log().all().contentType("application/json")
        .body(new Gson().toJson(data))
        .when()
        .post("/message/search")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("status", Matchers.is(false))
        .body("message", Matchers.is(JSONResponseMessage.MISSING_DATA_REQUIRED.toString()));
  }

  @Test
  public void testGetMessageSummary() {
    data.put("channel", "sms");
    data.put("startDate",simpleDateFormat.format(new DateTime().minusDays(10).toDate()));
    data.put("endDate",simpleDateFormat.format(new DateTime().toDate()));
    RestAssuredMockMvc.given()
        .log().all().contentType("application/json")
        .body(new Gson().toJson(data))
        .when()
        .post("/message/summary")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("status", Matchers.is(true))
        .body("message", Matchers.is("SUCCESS"));
  }

  @Test
  public void testGetMessageSummaryNoChannelException() {
    RestAssuredMockMvc.given()
        .log().all().contentType("application/json")
        .body(new Gson().toJson(data))
        .when()
        .post("/message/summary")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("status", Matchers.is(false))
        .body("message", Matchers.is(JSONResponseMessage.MISSING_DATA_REQUIRED.toString()));
  }


}
