package io.seanbailey.sqladapter;

/**
 * A data model, representing a single table within an SQL database.
 * You can use this class to effectively query, insert, and destroy information
 * within an SQL table.
 */
public abstract class Model {

  /**
   * Starts an SQL query chain designed to retrieve all instances of this model.
   * @param clazz Model class.
   * @return an SQL query.
   */
  public static SQLQuery all(Class<? extends Model> clazz) {
    return new SQLQuery(clazz);
  }
}
