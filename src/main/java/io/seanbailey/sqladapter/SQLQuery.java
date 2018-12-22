package io.seanbailey.sqladapter;

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

  /**
   * Constructs a new SQL query.
   * @param clazz Model class.
   */
  public SQLQuery(Class<? extends Model> clazz) {
    this.clazz = clazz;
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
   * Generate and return the SQL query as a string.
   * Note that this function should only be used for debug/output purposes.
   * Please use prepared statements instead.
   * @return A representation of this query as a string.
   */
  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("");

    switch (mode) {
      case NORMAL:
        joiner.add("SELECT * FROM");
        break;
      case COUNT:
      case EXISTS:
        joiner.add("SELECT COUNT(*) FROM");
    }

    return joiner.toString();
  }

  public Class<? extends Model> getClazz() {
    return clazz;
  }
}
