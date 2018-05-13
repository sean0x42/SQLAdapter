package io.seanbailey.controller;

import io.seanbailey.adapter.exception.SQLMappingException;
import io.seanbailey.model.Model;
import io.seanbailey.model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
      List<Object> users = Model.all(User.class).execute();
      for (Object object : users) {
        LOGGER.info(((User) object).getUsername());
      }
    } catch (SQLException | SQLMappingException e) {
      e.printStackTrace();
    }

    LOGGER.info("Done!");

  }

}
