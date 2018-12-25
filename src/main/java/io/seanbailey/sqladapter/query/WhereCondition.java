package io.seanbailey.sqladapter.query;

import java.util.StringJoiner;

/**
 * Represents a single condition within an SQL query.
 */
class WhereCondition {

  /**
   * An enum which defines how this condition should be chained with the
   * previous condition.
   */
  public enum Type { AND, OR }

  private final String condition;
  private final Object object;
  private final Type type;

  /**
   * Constructs a new where condition.
   * @param condition Conditional expression.
   * @param object Object to compare against.
   */
  WhereCondition(String condition, Object object) {
    this(condition, object, Type.AND);
  }

  /**
   * Constructs a new query condition.
   * @param condition Conditional expression.
   * @param object Object to compare against.
   * @param type Condition type.
   */
  WhereCondition(String condition, Object object, Type type) {
    // Add default operator if not already defined
    if (!condition.contains("?")) {
      condition += " = ?";
    }

    this.condition = condition;
    this.object = object;
    this.type = type;
  }

  /**
   * Constructs and returns a representation of this condition as a string.
   * @param shouldIncludeType Whether to include the condition type.
   * @return A string representation of this condition.
   */
  String toString(boolean shouldIncludeType) {
    StringJoiner joiner = new StringJoiner(" ");

    // Add type if necessary
    if (shouldIncludeType) {
      joiner.add(type.toString());
    }

    joiner.add(condition.replace("?", "\"" + object.toString() + "\""));
    return joiner.toString();
  }

  String getCondition() {
    return condition;
  }

  Object getObject() {
    return object;
  }

  Type getType() {
    return type;
  }
}
