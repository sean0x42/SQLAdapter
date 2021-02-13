package io.seanbailey.sqladapter.query;

import io.seanbailey.sqladapter.Model;
import java.util.LinkedList;
import java.util.StringJoiner;

/**
 * A chain of SQL operations, that can be used to generate queries.
 * Chaining functions together provides lots of control over the resulting SQL,
 * allowing you to generate any query you can imagine.
 */
public class Query {

  private final Class<? extends Model> clazz;

  private Mode mode = Mode.NORMAL;
  private Integer limit = null;
  private Integer offset = null;
  private Integer page = null;
  private LinkedList<WhereCondition> wheres;
  private LinkedList<OrderClause> orders;

  /**
   * Constructs a new SQL query.
   * @param clazz Model class.
   */
  public Query(Class<? extends Model> clazz) {
    this.clazz = clazz;
    wheres = new LinkedList<>();
    orders = new LinkedList<>();
  }

  /**
   * Defines a condition that the model must conform to.
   *
   * By default, this function assumes you wish perform a simple comparison
   * between the attribute and object. So calling
   *
   * <pre>
   * {@code Article.where("title", "The quick brown fox");}
   * </pre>
   *
   * Will generate the following SQL
   *
   * <pre>
   * {@code SELECT * FROM articles WHERE title = "The quick brown fox";}
   * </pre>
   *
   * If you need more control over the operator that is used in the where
   * clause, you can simple write your own operator within the attribute.
   *
   * <pre>
   * {@code Atricle.where("published_at >= ?", new Date(2018, 12, 24));}
   * </pre>
   * 
   * Which would result in the following SQL
   *
   * <pre>
   * {@code SELECT * FROM articles WHERE published_at >= '2018/12/24';}
   * </pre>
   *
   * It's also worth noting that this function assumes that you wish to chain
   * conditions with the AND operator. If this is not the case, use the
   * {@link #or(String, Object) or} method instead.
   *
   * @param attribute Attribute to find.
   * @param object Object to compare against.
   * @return An SQL query for chaining.
   */
  public Query where(String attribute, Object object) {
    wheres.add(new WhereCondition(attribute, object));
    return this;
  }

  /**
   * Adds a where condition chained with the OR operator.
   * @param attribute Attribute to find.
   * @param object Object to compare against.
   * @return An Query for chaining.
   */
  public Query or(String attribute, Object object) {
    wheres.add(new WhereCondition(attribute, object, WhereCondition.Type.OR));
    return this;
  }

  /**
   * Removes all previously defined orders.
   * @return An Query for chaining.
   */
  public Query order() {
    orders.clear();
    return this;
  }

  /**
   * Order according to the given attribute.
   * @param attribute Attribute to order by.
   * @return An Query for chaining.
   */
  public Query order(String attribute) {
    return order(attribute, Order.ASCENDING);
  }

  /**
   * Defines how the results should be ordered.
   * @param attribute Attribute to order by.
   * @param order How to order results.
   * @return an Query for chaining.
   */
  public Query order(String attribute, Order order) {
    orders.add(new OrderClause(attribute, order));
    return this;
  }

  /**
   * Clears any defined orders, then adds the given order.
   * @see #order(String)
   * @param attribute Attribute to order by.
   * @return an Query for chaining.
   */
  public Query reorder(String attribute) {
    orders.clear();
    return order(attribute);
  }

  /**
   * Clears any defined orders, then adds the given order.
   * @see #order(String, Order)
   * @param attribute Attribute to order by.
   * @param order How to order results.
   * @return an Query for chaining.
   */
  public Query reorder(String attribute, Order order) {
    orders.clear();
    return order(attribute, order);
  }

  /**
   * Retrieves the total number of instances saved in the database.
   * @return An Query for chaining.
   */
  public Query count() {
    mode = Mode.COUNT;
    return this;
  }

  /**
   * Determines whether a model matching the given requirements exists.
   * @return An Query for chaining.
   */
  public Query exists() {
    mode = Mode.EXISTS;
    return this;
  }

  /**
   * Reset the record limit.
   * @return An Query for chaining.
   */
  public Query limit() {
    return limit(null);
  }

  /**
   * Limits the total number of returned records.
   * @param limit Maximum number of returned records.
   * @return An Query for chaining.
   */
  public Query limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Removes any offset.
   * @return An Query for chaining.
   */
  public Query offset() {
    return offset(null);
  }

  /**
   * Offsets the returns results.
   * @param offset Number of records to offset by.
   * @return An Query for chaining.
   */
  public Query offset(Integer offset) {
    this.offset = offset;
    page = null; // Reset page so that last function to be called takes priority
    return this;
  }

  /**
   * Resets the number of records per page.
   * @return An Query for chaining.
   */
  public Query per() {
    return per(null);
  }

  /**
   * An alias to limit, which makes semantic sense when paging.
   * @see #limit(Integer)
   * @param per Number of records per page.
   * @return An Query for chaining.
   */
  public Query per(Integer per) {
    return limit(per); 
  }

  /**
   * Resets the current page.
   * @return An Query for chaining.
   */
  public Query page() {
    return page(null);
  }

  /**
   * Defines which page should be retrieved.
   * Ideally this should be used in conjunction with per, but will work as long
   * as a limit as been defined.
   * @see #per(Integer)
   * @param page Page to view.
   * @return An Query for chaining.
   */
  public Query page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Generate and return the SQL query as a string.
   * This function is useful for previewing the resulting SQL.
   * @return A representation of this query as a string.
   */
  @Override
  public String getPreview() {
    StringJoiner joiner = new StringJoiner(" ");

    // Step 1: Define how the statement should start
    switch (mode) {
      case NORMAL:
        joiner.add("SELECT * FROM");
        break;
      case COUNT:
      case EXISTS:
        joiner.add("SELECT COUNT(*) FROM");
    }

    // Step 2: Add class
    joiner.add("_");//Adapter.inferTableName(clazz));

    // Step 3: Handle where conditions
    generateWhere(joiner);

    // Step 4: Handle ordering
    generateOrdering(joiner);

    // Step 5: Handle offsets and limits
    generatePaging(joiner);

    // Step 6: Semicolon
    return joiner.toString() + ";";
  }

  /**
   * Generates the WHERE component of an SQL statement.
   * @param joiner StringJoiner to append WHERE to.
   */
  private void generateWhere(StringJoiner joiner) {
    // Ensure where conditions have been defined.
    if (wheres.size() == 0) {
      return;
    } 

    joiner.add("WHERE");
    boolean first = true;

    for (WhereCondition condition : wheres) {
      joiner.add(condition.toString(!first));
      first = false;
    }
  }

  /**
   * Generates the ordering component of an SQL statement.
   * @param joiner StringJoiner to append ORDER BY clause to.
   */
  private void generateOrdering(StringJoiner joiner) {
    // Ensure order clauses have been defined
    if (orders.size() == 0) {
      return;
    }

    joiner.add("ORDER BY");
    StringJoiner clauseJoiner = new StringJoiner(", ");

    for (OrderClause clause : orders) {
      clauseJoiner.add(String.format(
            "%s %s",
            clause.getAttribute(),
            clause.getOrder().getSQL()
      ));
    }

    joiner.add(clauseJoiner.toString());
  }

  /**
   * Generates the paging component of an SQL statement.
   * Determines what values should be displayed under LIMIT and OFFSET.
   * @param joiner StringJoiner to append LIMIT and OFFSET to.
   */
  private void generatePaging(StringJoiner joiner) {
    if (limit != null && limit >= 0) {
      joiner.add("LIMIT " + limit);

      // Handle defined page
      if (page != null) {
        joiner.add("OFFSET " + Math.max((page - 1) * limit, 0));
        return;
      }
    }

    if (offset != null && offset >= 0) {
      joiner.add("OFFSET " + offset);
    }
  }

  /**
   * Defines all possible orders that query results can be returned.
   * @see Query
   */
  public enum Order {
    ASCENDING("ASC"),
    DESCENDING("DESC");

    private final String sql;

    /**
     * Constructs a new order enum constant.
     * @param sql Representation of this constant as an SQL string.
     */
    Order(String sql) {
      this.sql = sql;
    }

    public String getSQL() {
      return sql;
    }
  }

  /**
   * An enum which defines the mode of operation for an SQL query.
   * @see Query
   */
  enum Mode {
    NORMAL, COUNT, EXISTS
  }
}
