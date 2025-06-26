package example.endpoints;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.*;

public class ApiEndpoints {

    public static final HttpRequestActionBuilder session = http("Session")
            .get("/session")
            .check(status().is(200))
            .check(jmesPath("sessionId").saveAs("SessionId"));

    public static final HttpRequestActionBuilder products = http("Get products")
            .get("/products")
            .queryParam("page", "#{pageNumber}")
            .queryParam("search", "#{searchKey}")
            .check(status().is(200))
            .check(jmesPath("products").saveAs("Products"));

    public static final HttpRequestActionBuilder login = http("Login")
            .post("/login")
            .asFormUrlEncoded()
            .formParam("username", "#{username}")
            .formParam("password", "#{password}")
            .check(status().is(200))
            .check(jmesPath("accessToken").saveAs("AccessToken"));

    public static final HttpRequestActionBuilder addToCart = http("Add to cart")
            .post("/cart")
            .asJson()
            .body(ElFileBody("bodies/cart.json"))
            .check(status().is(200));

    public static final HttpRequestActionBuilder checkout = http("Checkout")
            .post("/checkout")
            .header("Authorization", "#{AccessToken}")
            .asJson()
            .body(ElFileBody("bodies/cart.json"))
            .check(status().is(200));

}
