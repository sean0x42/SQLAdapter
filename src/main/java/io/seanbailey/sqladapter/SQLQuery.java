package io.seanbailey.sqladapter;

/**
 * A chain of SQL operations, that can be used to generate queries.
 * Chaining functions together provides lots of control over the resulting SQL,
 * allowing you to generate any query you can imagine.
 */
public class SQLQuery {

  private final Class<? extends Model> clazz;

  /**
   * Constructs a new SQL query.
   * @param clazz Model class.
   */
  public SQLQuery(Class<? extends Model> clazz) {
    this.clazz = clazz;
  }

  /**
   * Generate and return the SQL query as a string.
   * Note that this function should only be used for debug/output purposes.
   * Please use prepared statements instead.
   * @return A representation of this query as a string.
   */
  @Override
  public String toString() {
    return "SELECT *";      
  }

  public Class<? extends Model> getClazz() {
    return clazz;
  }
}
