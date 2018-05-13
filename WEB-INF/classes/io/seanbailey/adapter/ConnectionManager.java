package io.seanbailey.adapter;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionManager {

  /**
   * Returns a connection to the MySQL server.
   *
   * @return SQL connection
   */
  public static Connection getConnection() throws NamingException, SQLException {

    // Obtain our environment naming context
    Context initialContext = new InitialContext();
    Context context = (Context) initialContext.lookup("java:comp/env");

    // Look up data source
    DataSource dataSource = (DataSource) context.lookup("SENG2050_2018");

    // Allocate and use a connection from the pool
    return dataSource.getConnection();

  }

}
