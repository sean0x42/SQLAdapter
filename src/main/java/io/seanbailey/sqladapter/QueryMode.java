package io.seanbailey.sqladapter;

/**
 * An enum which dcefines the mode of operation for an SQL query.
 * @see io.seanbailey.sqladapter.SQLQuery
 */
public enum QueryMode {
  NORMAL, COUNT, EXISTS
}
