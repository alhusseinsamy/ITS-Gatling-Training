package example;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static example.endpoints.ApiEndpoints.*;
import static example.endpoints.WebEndpoints.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class ItsSimulation extends Simulation {

  private record Product(
      int id,
      String name,
      String color,
      String price,
      int quantity,
      String imageSrc,
      String imageAlt) {

  }

  private static final FeederBuilder<Object> usersFeeder = jsonFile("data/users_dev.json").circular();

  // Load VU count from system properties
  // Reference: https://docs.gatling.io/guides/passing-parameters/
  private static final int vu = Integer.getInteger("vu", 1);

  // Define HTTP configuration
  // Reference: https://docs.gatling.io/reference/script/protocols/http/protocol/
  private static final HttpProtocolBuilder httpProtocol = http.baseUrl("https://api-ecomm.gatling.io")
      .acceptHeader("application/json")
      .userAgentHeader(
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  // Define scenario
  // Reference: https://docs.gatling.io/reference/script/core/scenario/
  private static final ScenarioBuilder scenario = scenario("Scenario").exec(
      homePage,
      session,
      exec(session -> session.set("pageNumber", "0")),
      exec(session -> session.set("searchKey", "")),
      products,
      loginPage,
      feed(usersFeeder),
      login,
      homePage,
      products);

  // Define assertions
  // Reference: https://docs.gatling.io/reference/script/core/assertions/
  private static final Assertion assertion = global().failedRequests().count().lt(1L);

  // Define injection profile and execute the test
  // Reference: https://docs.gatling.io/reference/script/core/injection/
  {
    setUp(scenario.injectOpen(atOnceUsers(vu))).assertions(assertion).protocols(httpProtocol);
  }
}
