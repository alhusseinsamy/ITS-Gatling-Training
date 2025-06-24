package example.endpoints;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class WebEndpoints {

  public static final HttpRequestActionBuilder homePage = http("Homepage")
      .get("https://ecomm.gatling.io/")
      .check(status().is(200));

}
