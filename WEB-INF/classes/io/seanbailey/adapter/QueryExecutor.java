package io.seanbailey.adapter;

import io.seanbailey.adapter.SQLChain.Finisher;
import io.seanbailey.adapter.exception.SQLAdapterException;
import io.seanbailey.adapter.generator.Generator;
import io.seanbailey.adapter.generator.InsertGenerator;
import io.seanbailey.adapter.generator.SelectGenerator;
import io.seanbailey.adapter.generator.UpdateGenerator;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * A class for executing SQL queries.
 *
 * @author Sean Bailey
 * @see Model
 * @since 2018-05-14
 */
class QueryExecutor {

  private static Logger LOGGER;

  static {
    LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
  }


  /**
   * Inserts a model into the database.
   *
   * @param model Model to insert into database
   * @since 2018-05-14
   */
  static <T extends Model> void save(T model) throws SQLAdapterException, SQLException {
    executeWithGenerator(new InsertGenerator(model));
  }


  /**
   * Updates a model in the database.
   *
   * @param model Model to insert into database
   * @since 2018-05-14
   */
  static <T extends Model> void update(T model) throws SQLAdapterException, SQLException {
    executeWithGenerator(new UpdateGenerator(model));
  }


  /**
   * Retrieves a result set and returns a count.
   *
   * @param chain SQL Chain to count results of.
   * @return The number of models matching the conditions of the SQL chain.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @throws SQLAdapterException if the generated SQL cannot be mapped to Java for some reason.
   * @see SQLChain
   * @since 2018-05-14
   */
  static int getCount(SQLChain chain) throws SQLException, SQLAdapterException {

    // Use try-with-resources to automatically close result package
    try (ResultPackage pack = executeChain(chain, Finisher.COUNT)) {

      ResultSet results = pack.getResults();

      // Handle result set
      if (results == null) {
        throw new SQLAdapterException("Generated ResultSet was null.");
      } else if (results.first()) {
        return results.getInt(1);
      }
    }

    throw new SQLAdapterException(
        "Generated ResultSet was empty. This will only occur under catastrophic circumstances.");

  }


  /**
   * Determines whether the given model exists.
   *
   * @param chain SQL Chain that defines SQL to be generated.
   * @return Whether a model that matches the chain's operations exists.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @throws SQLAdapterException if the generated SQL cannot be mapped to Java for some reason.
   * @see SQLChain
   * @since 2018-05-14
   */
  static boolean exists(SQLChain chain) throws SQLException, SQLAdapterException {

    // Get results
    try (ResultPackage pack = executeChain(chain, Finisher.EXISTS)) {

      ResultSet results = pack.getResults();

      // Handle result set
      if (results == null) {
        throw new SQLAdapterException("Generated ResultSet was null.");
      } else if (results.first()) {
        return results.getInt(1) == 1;
      }

    }

    throw new SQLAdapterException(
        "Generated ResultSet was empty. This will only occur under catastrophic circumstances.");

  }


  /**
   * Retrieves all matching models.
   *
   * @param chain SQL Chain that defines which models should be matched.
   * @return An array of all matching models.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @throws SQLAdapterException if the generated SQL cannot be mapped to Java for some reason.
   * @see SQLChain
   * @since 2018-05-14
   */
  static List<Model> execute(SQLChain chain) throws SQLException, SQLAdapterException {

    // Create new array list of models
    List<Model> models = new ArrayList<>();

    // Get results
    try (ResultPackage pack = executeChain(chain, Finisher.EXECUTE)) {

      // Get results
      ResultSet results = pack.getResults();
      List<String> attributes = Adapter.getAttributesFromResults(results);

      // Handle results
      if (results == null) {
        throw new SQLAdapterException("Generated ResultSet was null.");
      }

      // Iterate over results
      while (results.next()) {
        models.add(Adapter.createModel(chain.getClazz(), attributes, results));
      }

    }

    return models;

  }


  /**
   * Executes an SQL chain and returns a results package.
   *
   * @param chain SQL Chain to execute.
   * @param finisher The operation that finished this chain.
   * @return A Results Package, containing various elements such as connection and result sets.
   * @since 2018-05-14
   */
  private static ResultPackage executeChain(SQLChain chain, Finisher finisher)
      throws SQLException, SQLAdapterException {

    // Generate and log
    SelectGenerator generator = new SelectGenerator(chain, finisher);
    logExecution(generator);

    try {

      // Establish connection
      Connection connection = ConnectionManager.getConnection();
      PreparedStatement statement = connection.prepareStatement(generator.getSql());

      // Iterate over and add prepared objects
      List<Object> objects = generator.getObjects();
      for (int i = 0; i < objects.size(); i++) {
        statement.setObject(i + 1, objects.get(i));
      }

      return new ResultPackage(connection, statement, statement.executeQuery());

    } catch (NamingException e) {
      throw new SQLAdapterException(String.format(
          "Failed to execute SQL Chain of type %s. Exception: %s",
          chain.getClazz().getSimpleName(),
          e.getMessage())
      );
    }

  }


  /**
   * Executes SQL generated by a generator.
   *
   * @param generator Generator, whose SQL should be executed.
   * @since 2018-05-14
   */
  private static void executeWithGenerator(Generator generator)
      throws SQLAdapterException, SQLException {

    logExecution(generator);

    // Use a try-with-resources so that conn is automatically closed
    try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(generator.getSql())) {

      // Iterate over and add prepared objects
      List<Object> objects = generator.getObjects();
      for (int i = 0; i < objects.size(); i++) {
        statement.setObject(i + 1, objects.get(i));
      }

      statement.execute();

    } catch (NamingException e) {
      e.printStackTrace();
      throw new SQLAdapterException(String.format(
          "Failed to execute SQL. Exception: %s",
          e.getMessage())
      );
    }

  }


  /**
   * Logs the generated SQL according to a standard format.
   *
   * @param generator SQL generator.
   * @see Generator
   * @since 2018-05-14
   */
  private static void logExecution(Generator generator) {

    // Join prepared objects
    StringJoiner joiner = new StringJoiner(", ");
    for (Object object : generator.getObjects()) {
      joiner.add(object.toString());
    }

    LOGGER.info(String.format("%s [%s]", generator.getSql(), joiner.toString()));

  }

}
