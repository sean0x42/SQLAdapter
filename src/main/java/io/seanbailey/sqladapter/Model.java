package io.seanbailey.sqladapter;

/**
 * A data model, representing a single table within an SQL database.
 * You can use this class to effectively query, insert, and destroy information
 * within an SQL table.
 */
public abstract class Model {

  /**
   * Starts an SQL query chain, to retrieve all instances of this model.
   * @param clazz Model class.
   * @return an SQL query.
   */
  public static SQLQuery all(Class<? extends Model> clazz) {
    return new SQLQuery(clazz);
  }

  /**
   * Starts an SQL query, and defines a where condition.
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
   * @see io.seanbailey.sqladapter.SQLQuery
   * @param clazz {@link io.seanbailey.sqladapter.Model Model} class.
   * @param attribute Attribute to find.
   * @param object Object to compare against.
   * @return an SQL query.
   */
  public static SQLQuery where(Class<? extends Model> clazz, String attribute,
      Object object) {
    return new SQLQuery(clazz).where(attribute, object);
  }
}
