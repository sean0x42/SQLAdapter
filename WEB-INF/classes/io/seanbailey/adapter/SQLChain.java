package io.seanbailey.adapter;

import io.seanbailey.adapter.exception.SQLAdapterException;
import io.seanbailey.adapter.util.Order;
import io.seanbailey.adapter.util.WhereOperation;
import io.seanbailey.adapter.util.WhereOperation.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A chain of SQL operations, that come together to create a single SQL Query when executed.
 *
 * @author Sean Bailey
 * @since 2018-05-14
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SQLChain {

  private Class<? extends Model> clazz;
  private Integer limit = null;
  private Integer offset = null;
  private Integer page = null;
  private Map<String, Order> orders = new LinkedHashMap<>();
  private List<WhereOperation> wheres = new ArrayList<>();


  /**
   * Constructs a new SQL Chain.
   *
   * @param clazz Model class.
   * @since 2018-05-14
   */
  public SQLChain(Class<? extends Model> clazz) {
    this.clazz = clazz;
  }


  /**
   * An enum containing all possible finisher operations. Note that in most circumstances, EXISTS is
   * an alias of COUNT.
   * @since 2018-05-12
   */
  public enum Finisher {
    COUNT, EXISTS, EXECUTE
  }


  /**
   * Adds a condition that must be met by all returned models.
   *
   * @param condition Condition that must be met.
   * @param object Object to insert into condition at question mark.
   * @return An SQLChain for further chaining.
   * @since 2018-05-14
   */
  public SQLChain where(String condition, Object object) {

    // Add question mark if necessary
    if (!condition.contains("?")) {
      condition += " = ?";
    }

    wheres.add(new WhereOperation(condition, object));
    return this;
  }


  /**
   * Adds a condition that must be met by all returned models.
   *
   * @param condition Condition that must be met.
   * @param object Object to insert into condition at question mark.
   * @return An SQLChain for further chaining.
   * @since 2018-05-14
   */
  public SQLChain or(String condition, Object object) {
    wheres.add(new WhereOperation(condition, object, Type.OR));
    return this;
  }


  /**
   * Removes any limits from earlier in the chain.
   *
   * @return An SQLChain for further chaining.
   * @since 2018-05-14
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
   * @since 2018-05-14
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
   * @since 2018-05-14
   */
  public SQLChain per(int per) {
    return limit(per);
  }


  /**
   * Removes any offset, that may have been added earlier in the chain.
   *
   * @return An SQLChain for further chaining.
   * @since 2018-05-14
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
   * @since 2018-05-14
   */
  public SQLChain offset(int offset) {
    this.offset = offset;
    return this;
  }


  /**
   * Sets which page the result set should be returned from.
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
   * @since 2018-05-14
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
   * @since 2018-05-14
   */
  public SQLChain order(String attribute, Order order) {
    orders.put(attribute, order);
    return this;
  }


  /**
   * Returns the total number of models that match the chain.
   *
   * @return The number of models.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @throws SQLAdapterException if the generated SQL cannot be mapped to Java for some reason.
   * @since 2018-05-14
   */
  public int count() throws SQLException, SQLAdapterException {
    return QueryExecutor.getCount(this);
  }


  /**
   * Determines whether a given model exists.
   *
   * @return A boolean indicating whether or not a model matching the conditions outlined by the SQL
   * chain was found.
   * @throws SQLException if the generated SQL is malformed, or some other SQL related exception is
   * encountered.
   * @throws SQLAdapterException if the generated SQL cannot be mapped to Java for some reason.
   * @since 2018-05-14
   */
  public boolean exists() throws SQLException, SQLAdapterException {
    limit(1);
    return QueryExecutor.exists(this);
  }


  /**
   * Finishes a chain, and returns the results.
   *
   * @return An array of models that match the conditions outlined by the SQL chain.
   * @throws SQLException if the generated SQL is malformed or some other error occurs.
   * @throws SQLAdapterException if the resultant SQL cannot be mapped to Java models.
   * @since 2018-05-14
   */
  public List<Model> execute() throws SQLException, SQLAdapterException {
    return QueryExecutor.execute(this);
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

  public List<WhereOperation> getWheres() {
    return wheres;
  }

}
