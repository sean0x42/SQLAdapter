package io.seanbailey.sqladapter;

import java.util.LinkedList;
import java.util.StringJoiner;

/**
 * A chain of SQL operations, that can be used to generate queries.
 * Chaining functions together provides lots of control over the resulting SQL,
 * allowing you to generate any query you can imagine.
 */
public class SQLQuery {

  private final Class<? extends Model> clazz;

  private QueryMode mode = QueryMode.NORMAL;
  private Integer limit = null;
  private Integer offset = null;
  private Integer page = null;
  private LinkedList<QueryCondition> wheres;

  /**
   * Constructs a new SQL query.
   * @param clazz Model class.
   */
  public SQLQuery(Class<? extends Model> clazz) {
    this.clazz = clazz;
    wheres = new LinkedList<>();
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
  public SQLQuery where(String attribute, Object object) {
    wheres.add(new QueryCondition(attribute, object));   
    return this;
  }

  /**
   * Adds a where condition chained with the OR operator.
   * @param attribute Attribute to find.
   * @param object Object to compare against.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery or(String attribute, Object object) {
    wheres.add(new QueryCondition(attribute, object, QueryCondition.Type.OR));
    return this;
  }

  /**
   * Retrieves the total number of instances saved in the database.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery count() {
    mode = QueryMode.COUNT;
    return this;
  }

  /**
   * Determines whether a model matching the given requirements exists.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery exists() {
    mode = QueryMode.EXISTS;
    return this;
  }

  /**
   * Reset the record limit.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery limit() {
    return limit(null);
  }

  /**
   * Limits the total number of returned records.
   * @param limit Maximum number of returned records.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Removes any offset.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery offset() {
    return offset(null);
  }

  /**
   * Offsets the returns results.
   * @param offset Number of records to offset by.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery offset(Integer offset) {
    this.offset = offset;
    page = null; // Reset page so that last function to be called takes priority
    return this;
  }

  /**
   * Resets the number of records per page.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery per() {
    return per(null);
  }

  /**
   * An alias to limit, which makes semantic sense when paging.
   * @see #limit(Integer)
   * @param per Number of records per page.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery per(Integer per) {
    return limit(per); 
  }

  /**
   * Resets the current page.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery page() {
    return page(null);
  }

  /**
   * Defines which page should be retrieved.
   * Ideally this should be used in conjunction with per, but will work as long
   * as a limit as been defined.
   * @see #per(Integer)
   * @param page Page to view.
   * @return An SQLQuery for chaining.
   */
  public SQLQuery page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Generate and return the SQL query as a string.
   * Note that this function should only be used for debug/output purposes.
   * Please use prepared statements instead.
   * @return A representation of this query as a string.
   */
  @Override
  public String toString() {
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

    // Step 4: Handle offsets and limits
    generatePaging(joiner);

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

    for (QueryCondition condition : wheres) {
      joiner.add(condition.toString(!first));
      first = false;
    }
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
}
