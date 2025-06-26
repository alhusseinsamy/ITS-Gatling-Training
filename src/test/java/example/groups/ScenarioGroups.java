package example.groups;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.core.*;

import static example.endpoints.ApiEndpoints.*;
import static example.endpoints.WebEndpoints.*;
import static example.actions.Actions.*;

public class ScenarioGroups {

  private static final FeederBuilder<Object> usersFeeder = jsonFile("data/users_dev.json").circular();

  public static ChainBuilder homePageAnonymousGroup = group("Homepage anonymous").on(
      homePage,
      session,
      setPageNumber,
      setSearchKey,
      products);

  public static ChainBuilder loginGroup = group("Login").on(
      loginPage,
      feed(usersFeeder),
      // pause(5, 15),
      login);

  public static ChainBuilder homePageAuthenticatedGroup = group("Homepage authenticated").on(
      homePage);

  public static ChainBuilder browseAndAddToCartGroup = group("Browse and add to cart").on(
      products,
      createAddToCartBody,
      // pause(5, 15),
      addToCart);

  public static ChainBuilder checkoutGroup = group("Checkout").on(
      checkout);

}
