package io.seanbailey.adapter.util;

/**
 * An enum that outlines
 */
public enum Order {

  ASCENDING("ASC"),
  DESCENDING("DESC");

  private String sql;

  /**
   * Creates a new order.
   *
   * @param sql The SQL version of this order.
   */
  Order(String sql) {
    this.sql = sql;
  }

  public String toSQL() {
    return sql;
  }

}
