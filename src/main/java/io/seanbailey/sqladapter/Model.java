package io.seanbailey.sqladapter;

import io.seanbailey.sqladapter.query.Query;

/**
 * A data model, representing a single table within an SQL database.
 * You can use this class to effectively query, insert, and destroy information
 * within an SQL table.
 */
public abstract class Model {

  /**
   * Retrieves all instances of this model.
   * @param clazz Model class.
   * @return an SQL query for chaining.
   */
  public static Query all(Class<? extends Model> clazz) {
    return new Query(clazz);
  }

  /**
   * Retrieves all instances of this model which match the given condition.
   *
   * By default, this function assumes you wish perform a simple comparison
   * between the attribute and object. So calling
   *
   * <pre>
   * {@code Article.where("title", "The quick brown fox");}
   * </pre>
   *
   * Will generate the following SQL
   *
   * <pre>
   * {@code SELECT * FROM articles WHERE title = "The quick brown fox";}
   * </pre>
   *
   * If you need more control over the operator that is used in the where
   * clause, you can simple write your own operator within the attribute.
   *
   * <pre>
   * {@code Atricle.where("published_at >= ?", new Date(2018, 12, 24));}
   * </pre>
   * 
   * Which would result in the following SQL
   *
   * <pre>
   * {@code SELECT * FROM articles WHERE published_at >= '2018/12/24';}
   * </pre>
   *
   * @see Query
   * @param clazz {@link io.seanbailey.sqladapter.Model Model} class.
   * @param attribute Attribute to find.
   * @param object Object to compare against.
   * @return an SQL query chaining.
   */
  public static Query where(Class<? extends Model> clazz, String attribute,
      Object object) {
    return new Query(clazz).where(attribute, object);
  }

  /**
   * Finds a single instance of this model which matches the given conditions.
   * @param clazz {@link io.seanbailey.sqladapter.Model Model} class.
   * @param attribute Attribute to find.
   * @param object Object to compare against.
   * @return an SQL query for chaining.
   */
  public static Query find(Class<? extends Model> clazz, String attribute,
      Object object) {
    return new Query(clazz).where(attribute, object).limit(1);
  }
}
