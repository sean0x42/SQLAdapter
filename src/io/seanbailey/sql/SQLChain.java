package io.seanbailey.sql;

import io.seanbailey.sql.util.Order;
import io.seanbailey.sql.util.Where;
import io.seanbailey.sql.util.Where.Type;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SQLChain {

  private Class<? extends Model> clazz;
  private Integer limit = null;
  private Integer offset = null;
  private Integer page = null;
  private Map<String, Order> orders = new LinkedHashMap<>();
  private List<Where> wheres = new ArrayList<>();


  /**
   * Constructs a new SQL Chain.
   *
   * @param clazz Model class.
   */
  public SQLChain(Class<? extends Model> clazz) {
    this.clazz = clazz;
  }


  /**
   * An enum containing all possible finisher operations. Note that in most circumstances, EXISTS is
   * an alias of COUNT.
   */
  enum Finisher {
    COUNT, EXISTS, EXECUTE
  }


  /**
   * Adds a condition that must be met by all returned models.
   *
   * @param condition Condition that must be met.
   * @param object Object to insert into condition at question mark.
   * @return An SQLChain for further chaining.
   */
  public SQLChain where(String condition, Object object) {
    wheres.add(new Where(condition, object));
    return this;
  }


  /**
   * Adds a condition that must be met by all returned models.
   *
   * @param condition Condition that must be met.
   * @param object Object to insert into condition at question mark.
   * @return An SQLChain for further chaining.
   */
  public SQLChain or(String condition, Object object) {
    wheres.add(new Where(condition, object, Type.OR));
    return this;
  }


  /**
   * Removes any limits from earlier in the chain.
   *
   * @return An SQLChain for further chaining.
   */
  public SQLChain limit() {
    this.limit = null;
    return this;
  }


  /**
   * Sets the maximum number of instances returned by this chain.
   *
   * @param limit Maximum number of instances.
   * @return An SQLChain for further chaining.
   */
  public SQLChain limit(int limit) {
    this.limit = limit;
    return this;
  }


  /**
   * Sets the number of elements per result set.
   *
   * @param per Maximum number of elements in a result set.
   * @return An SQLChain for further chaining.
   */
  public SQLChain per(int per) {
    return limit(per);
  }


  /**
   * Removes any offset, that may have been added earlier in the chain.
   *
   * @return An SQLChain for further chaining.
   */
  public SQLChain offset() {
    this.offset = null;
    return this;
  }


  /**
   * Offsets the results by the given number of rows. You would usually want to use this in
   * conjunction with limit.
   *
   * @param offset Number of rows to offset results by.
   * @return An SQLChain for further chaining.
   */
  public SQLChain offset(int offset) {
    this.offset = offset;
    return this;
  }


  /**
   * OSets which page the result set should be returned from.
   *
   * @param page The page to return.
   * @return An SQLChain for further chaining.
   */
  public SQLChain page(int page) {
    this.page = page;
    return this;
  }


  /**
   * Applies a new order. You may chain more than one, and they will be applied in the order that
   * they are chained.
   *
   * @param attribute Attribute to order by.
   * @return An SQLChain for further chaining.
   */
  public SQLChain order(String attribute) {
    return order(attribute, Order.ASCENDING);
  }


  /**
   * Applies a new order. You may chain more than one, and they will be applied in the order that
   * they are chained.
   *
   * @param attribute Attribute to order by.
   * @param order Direction to order by.
   * @return An SQLChain for further chaining.
   */
  public SQLChain order(String attribute, Order order) {
    orders.put(attribute, order);
    return this;
  }


  /**
   * Returns the total number of models that match the chain.
   *
   * @return The number of models.
   * @throws SQLException if the generated SQL is malformed or some other error occurs.
   */
  public int count() throws SQLException {

    // Get results
    ResultSet set = generateResults(Finisher.COUNT);
    return set.next() ? set.getInt(1) : 0;
  }


  /**
   * Determines whether a given model exists.
   *
   * @return A boolean indicating whether or not a model matching the conditions outlined by the SQL
   * chain was found.
   * @throws SQLException if the generated SQL is malformed or some other error occurs.
   */
  public boolean exists() throws SQLException {

    // Set limit
    limit(1);
    return count() == 1;
  }


  /**
   * Finishes a chain, and returns the results.
   *
   * @return An array of models that match the conditions outlined by the SQL chain.
   * @throws SQLException if the generated SQL is malformed or some other error occurs.
   */
  public Object[] execute() throws SQLException {

    // Get results
    ResultSet set = generateResults(Finisher.EXECUTE);
    Object[] objects = (Object[]) Array.newInstance(clazz);

    // Iterate over result set
    while (set.next()) {

    }

    return objects;

  }


  /**
   * Generates a result set, based off of the operations performed on this SQL chain.
   *
   * @param finisher Finisher operation. This can result in different SQL being generated.
   * @return A result set.
   */
  private ResultSet generateResults(Finisher finisher) {

    // Create a new array list to hold anything for the prepared statement
    List<Object> preparedObjects;
    String sql;

    SQLQueryGenerator generator = new SQLQueryGenerator(this, finisher);
    preparedObjects = generator.getPreparedObjects();
    sql = generator.getSQL();



  }


  public Class<? extends Model> getClazz() {
    return clazz;
  }

  public Integer getLimit() {
    return limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public Integer getPage() {
    return page;
  }

  public Map<String, Order> getOrders() {
    return orders;
  }

  public List<Where> getWheres() {
    return wheres;
  }

}
