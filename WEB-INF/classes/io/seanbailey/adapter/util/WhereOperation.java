package io.seanbailey.adapter.util;

public class WhereOperation {

  private String condition;
  private Object object;
  private Type type;

  /**
   * An enum of possible where types. The default is AND.
   */
  public enum Type { AND, OR }


  /**
   * Constructs a new Where operation.
   *  @param condition Condition that must be met.
   * @param object Object to insert at question mark in condition.
   */
  public WhereOperation(String condition, Object object) {
    this(condition, object, Type.AND);
  }


  /**
   * Constructs a new Where operation.
   *
   * @param condition Condition that must be met.
   * @param object Object to insert at question mark in condition.
   * @param type Type of Where operation.
   */
  public WhereOperation(String condition, Object object, Type type) {
    this.condition = condition;
    this.object = object;
    this.type = type;
  }


  public String getCondition() {
    return condition;
  }

  public Object getObject() {
    return object;
  }

  public Type getType() {
    return type;
  }

}
