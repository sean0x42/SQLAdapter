package io.seanbailey.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to return both a result set and metadata from an SQL chain execution. Without
 * it, retrieving the SQL metadata at a later time will fail, as the connection will have been
 * automatically closed.
 *
 * @author Sean Bailey
 * @since 2018-04-14
 */
public class ResultPackage implements AutoCloseable {

  private Connection connection;
  private PreparedStatement statement;
  private ResultSet results;


  /**
   * Constructs a new result package.
   *
   * @param connection A connection to the SQL server.
   * @param statement Prepared statement.
   * @param results An SQL result set.
   */
  public ResultPackage(Connection connection, PreparedStatement statement, ResultSet results) {
    this.connection = connection;
    this.statement = statement;
    this.results = results;
  }


  /**
   * Closes the connection.
   */
  @Override
  public void close() throws SQLException {
    results.close();
    statement.close();
    connection.close();
  }


  public ResultSet getResults() {
    return results;
  }

}
