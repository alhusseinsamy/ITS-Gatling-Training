package example.endpoints;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class WebEndpoints {

  public static final HttpRequestActionBuilder homePage = http("Homepage")
      .get("https://ecomm.gatling.io/")
      .check(status().in(200, 304));

  public static final HttpRequestActionBuilder loginPage = http("Login page")
      .get("https://ecomm.gatling.io/login")
      .check(status().in(200, 304));

}
