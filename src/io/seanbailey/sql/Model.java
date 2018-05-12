package io.seanbailey.sql;

/**
 * Models can be automatically translated to and from SQL. You can also use the methods defined in
 * this class to effectively query the database using an {@code SQLChain}.
 *
 * @author Sean Bailey
 * @version 1.0
 * @see SQLChain
 * @since 2018-05-10
 */
public abstract class Model {


  /**
   * Starts an {@code SQLChain}, that can return every instance of a model.
   *
   * @param clazz Class to retrieve
   * @return an {@code SQLChain} for further additions.
   */
  public static SQLChain all(Class<? extends Model> clazz) {
    return new SQLChain(clazz);
  }


  /**
   * Starts an {@code SQLChain}, that can be used to query the database for a particular set of
   * models. Internally this makes use of prepared statements, so you don't need to worry about SQL
   * injections.
   *
   * @param clazz Class to retrieve.
   * @param condition a condition that all retrieved models will meet. It should be written in the
   * form {@code "attribute = ?"} or {@code "attribute > ?"}. A question mark is used to mark the
   * point that the object should be inserted. You may also use the shorthand form {@code
   * "attribute"}, in which case {@code "attribute = ?"} will be inferred.
   * @param object Any Java object that can be inserted into a prepared statement. It takes the
   * place of the question mark in the condition.
   * @return an {@code SQLChain} for further additions.
   */
  public static SQLChain where(Class<? extends Model> clazz, String condition,
      Object object) {
    return new SQLChain(clazz).where(condition, object);
  }


  /**
   * Starts an {@code SQLChain}, that lets you find a single instance of a model.
   *
   * @param clazz Class to retrieve.
   * @param attribute Attribute to check object against. Must be unique.
   * @param object Object to compare to attribute.
   * @return an {@code SQLChain} for further additions.
   */
  public static SQLChain find(Class<? extends Model> clazz, String attribute, Object object) {
    return new SQLChain(clazz).where(attribute, object).limit(1);
  }


  protected void beforeSave() {}

  protected void afterSave() {}

}
