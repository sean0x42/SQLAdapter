package io.seanbailey.sqladapter.query;

/**
 * Represents a single order statement within an SQL expression.
 */
class OrderClause {

  private final String attribute;
  private final Query.Order order;

  /**
   * Constructs a new query order.
   * @param attribute Model attribute to order by.
   * @param order How to order the returned results.
   */
  OrderClause(String attribute, Query.Order order) {
    this.attribute = attribute;
    this.order = order;
  }

  String getAttribute() {
    return attribute;
  }

  Query.Order getOrder() {
    return order;
  }
}
