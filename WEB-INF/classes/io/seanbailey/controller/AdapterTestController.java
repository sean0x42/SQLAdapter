package io.seanbailey.controller;

import io.seanbailey.adapter.exception.SQLAdapterException;
import io.seanbailey.adapter.util.Order;
import io.seanbailey.adapter.Model;
import io.seanbailey.model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/adapter"})
public class AdapterTestController extends HttpServlet {

  private static Logger LOGGER = Logger
      .getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

  /**
   * Handles GET requests to /adapter
   *
   * @param request HTTP request object.
   * @param response HTTP response object.
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {


    try {

      // Attempt to save a new user
      User test = new User(UUID.randomUUID().toString());
      LOGGER.info("Saved? " + test.save());

      // Get users
      List<Model> users = Model
          .find(User.class, "username", "Johanne")
          .order("id", Order.DESCENDING)
          .execute();

      // Loop and print
      for (Object object : users) {
        User johanne = (User) object;
        LOGGER.info(johanne.getUsername());
        johanne.setUsername("Alfred Wayne");
        johanne.update();
      }

      int i = Model.all(User.class).count();
      LOGGER.info("Found " + i + " users.");

    } catch (SQLException | SQLAdapterException e) {
      e.printStackTrace();
    }

    LOGGER.info("Done!");

  }

}
