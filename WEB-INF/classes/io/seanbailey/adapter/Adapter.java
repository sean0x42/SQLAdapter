package io.seanbailey.adapter;

import io.seanbailey.adapter.SQLChain.Finisher;
import io.seanbailey.adapter.exception.SQLMappingException;
import io.seanbailey.util.StringUtil;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * A class for converting Java to SQL and back again.
 *
 * @author Sean Bailey
 * @since 2018-05-14
 */
class Adapter {

  private static Logger LOGGER;

  static {
    LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
  }


  /**
   * Retrieves a result set and returns a count.
   *
   * @param chain SQL Chain to count results of.
   * @return The number of models matching the conditions of the SQL chain.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @throws SQLMappingException if the generated SQL cannot be mapped to Java for some reason.
   * @since 2018-05-14
   */
  static int getCount(SQLChain chain) throws SQLException, SQLMappingException {

    // Get results
    ResultSet results = getResultSet(chain, Finisher.COUNT);

    // Ensure results aren't null
    if (results == null) {
      throw new SQLMappingException("Generated ResultSet was null.");
    }

    if (results.first()) {
      return results.getInt(1);
    }

    throw new SQLMappingException("Generated ResultSet was empty.");

  }


  /**
   * Determines whether the given model exists.
   *
   * @param chain SQL Chain that defines SQL to be generated.
   * @return Whether a model that matches the chain's operations exists.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @throws SQLMappingException if the generated SQL cannot be mapped to Java for some reason.
   * @since 2018-05-14
   */
  static boolean exists(SQLChain chain) throws SQLException, SQLMappingException {

    // Get results
    ResultSet results = getResultSet(chain, Finisher.EXISTS);

    // Ensure results aren't null
    if (results == null) {
      throw new SQLMappingException("Generated ResultSet was null.");
    }

    if (results.first()) {
      return results.getInt(1) == 1;
    }

    throw new SQLMappingException("Generated ResultSet was empty.");

  }


  /**
   * Retrieves all matching models.
   *
   * @param chain SQL Chain that defines which models should be matched.
   * @return An array of all matching models.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @throws SQLMappingException if the generated SQL cannot be mapped to Java for some reason.
   * @since 2018-05-14
   */
  static List<Object> execute(SQLChain chain) throws SQLException, SQLMappingException {

    // Get results
    ResultSet results = getResultSet(chain, Finisher.EXECUTE);
    List<String> columns = new ArrayList<>();
    List<Object> objects = new ArrayList<>();
    ResultSetMetaData metaData;

    // Ensure results aren't null
    if (results == null) {
      throw new SQLMappingException("Generated ResultSet was null.");
    }

    // Determine columns
    metaData = results.getMetaData();
    for (int i = 0; i <= metaData.getColumnCount(); i++) {
      columns.add(metaData.getColumnName(i));
    }


    try {

      while (results.next()) {

        // Create new object instance
        Object object = chain.getClazz().newInstance();

        // Iterate over columns, and use reflection to set values
        for (int i = 0; i < columns.size(); i++) {

          // Use reflection to set data
          String column = columns.get(i);
          Object data = results.getObject(i + 1);

          // Check if a setter is available
          try {
            Method method = chain.getClazz().getMethod(
                StringUtil.toSetter(column),
                data.getClass()
            );
            method.invoke(object, data);
            continue;
          } catch (NoSuchMethodException ignored) {}

          // No setter was found, set by field
          chain.getClazz().getField(column).set(object, data);

        }

        objects.add(object);

      }

    } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {

      // Handle the numerous possible exceptions
      throw new SQLMappingException(String.format(
          "Failed to map from SQL to Java for class %s. Exception: %s",
          chain.getClazz().getSimpleName(),
          e.getMessage()
      ));

    }

    return objects;

  }


  /**
   * Executes an SQL chain and retrieves it's result set.
   *
   * @param chain SQL Chain to execute.
   * @param finisher The chain operation that finished this chain.
   * @return A result set, generated from the SQL chain.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @since 2018-05-14
   */
  private static ResultSet getResultSet(SQLChain chain, Finisher finisher) throws SQLException {

    // Create a new query generator
    QueryGenerator generator = new QueryGenerator(chain, finisher);
    logQuery(generator);

    // Establish connection
    try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(generator.getSQL())) {

      // Iterate over and add prepared objects
      int preparedIndex = 1;
      for (Object object : generator.getPreparedObjects()) {
        statement.setObject(preparedIndex, object);
        preparedIndex++;
      }

      // Execute and return
      return statement.executeQuery();

    } catch (NamingException e) {
      e.printStackTrace();
    }

    return null;

  }


  /**
   * Logs the generated SQL according to a standard format.
   *
   * @param generator SQL generator.
   * @since 2018-05-14
   */
  private static void logQuery(QueryGenerator generator) {

    // Join prepared objects
    StringJoiner joiner = new StringJoiner(", ");

    for (Object object : generator.getPreparedObjects()) {
      joiner.add(object.toString());
    }

    LOGGER.info(String.format("%s [%s]", generator.getSQL(), joiner.toString()));

  }

}
