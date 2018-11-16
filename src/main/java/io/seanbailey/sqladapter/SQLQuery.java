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

  public Class<? extends Model> getClazz() {
    return clazz;
  }
}
