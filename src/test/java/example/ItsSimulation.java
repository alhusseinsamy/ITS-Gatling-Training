package example;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static example.endpoints.ApiEndpoints.*;
import static example.endpoints.WebEndpoints.*;
import static example.actions.Actions.*;
import static example.groups.ScenarioGroups.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class ItsSimulation extends Simulation {

  // Load VU count from system properties
  // Reference: https://docs.gatling.io/guides/passing-parameters/
  private static final int vu = Integer.getInteger("vu", 1);

  private static final int minPause = Integer.getInteger("minPause", 5);

  private static final int maxPause = Integer.getInteger("maxPause", 15);

  private static final String testType = System.getProperty("testType", "smoke");

  private static final int duration = Integer.getInteger("duration", 10);

  // Define HTTP configuration
  // Reference: https://docs.gatling.io/reference/script/protocols/http/protocol/
  private static final HttpProtocolBuilder httpProtocol = http.baseUrl("https://api-ecomm.gatling.io")
      .acceptHeader("application/json")
      .userAgentHeader(
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  // Define scenario
  // Reference: https://docs.gatling.io/reference/script/core/scenario/
  private static final ScenarioBuilder scenario = scenario("Scenario").exec(
      homePageAnonymousGroup,
      // pause(minPause, maxPause),
      loginGroup,
      homePageAuthenticatedGroup,
      initiateCartItems,
      removePageNumber,
      repeat(4, "pageNumber").on(
          browseAndAddToCartGroup),
      // pause(5, 15),
      checkoutGroup);

  // Define assertions
  // Reference: https://docs.gatling.io/reference/script/core/assertions/
  static final List<Assertion> assertions = List.of(
      global().responseTime().percentile(90.0).lt(500),
      global().failedRequests().percent().lt(5.0));

  static final PopulationBuilder injectionProfile(ScenarioBuilder scn) {
    return switch (testType) {
      case "smoke" -> scn.injectOpen(atOnceUsers(1));
      case "stress" -> scn.injectOpen(stressPeakUsers(vu).during(duration));
      default -> scn.injectOpen(atOnceUsers(vu));
    };
  }

  static final List<Assertion> getAssertions() {
    return switch (testType) {
      case "stress" -> assertions;
      case "smoke" -> List.of(global().failedRequests().count().lt(1L));
      default -> assertions;
    };
  }

  // Define injection profile and execute the test
  // Reference: https://docs.gatling.io/reference/script/core/injection/
  {
    setUp(injectionProfile(scenario)).assertions(getAssertions()).protocols(httpProtocol);
  }
}
