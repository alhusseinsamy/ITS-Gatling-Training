package example.actions;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.gatling.javaapi.http.*;
import io.gatling.javaapi.core.*;

public class Actions {

  private record Product(
      int id,
      String name,
      String color,
      String price,
      int quantity,
      String imageSrc,
      String imageAlt) {

  }

  private static final ObjectMapper mapper = new ObjectMapper();

  public static final ChainBuilder setPageNumber = exec(session -> session.set("pageNumber", "0"));

  public static final ChainBuilder setSearchKey = exec(session -> session.set("searchKey", ""));

  public static final ChainBuilder createAddToCartBody = exec(session -> {
    try {
      List<Product> products = mapper.readValue(
          session.getString("Products"), new TypeReference<List<Product>>() {
          });

      Random rand = new Random();
      Product randomProduct = products.get(rand.nextInt(products.size()));
      List<Product> cartItems = new ArrayList<>();
      cartItems.add(randomProduct);

      // Serialize updated cart list back to session
      String cartItemsJsonString = mapper.writeValueAsString(cartItems);
      return session.set("CartItems", cartItemsJsonString);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  });

}
