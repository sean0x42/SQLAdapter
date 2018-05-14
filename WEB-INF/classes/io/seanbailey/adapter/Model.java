package io.seanbailey.adapter;

import io.seanbailey.adapter.exception.SQLAdapterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


  private List<String> errors = new ArrayList<String>();
  private boolean saved = false;


  /**
   * Empty constructor is required for reflection
   */
  public Model() {}


  /**
   * Starts an {@code SQLChain}, that can return every instance of a model.
   *
   * @param clazz Class to query for.
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
   * @param clazz Class to query for.
   * @param condition a condition that all retrieved models will meet. A question mark is used to
   * mark the point that the object should be inserted in the prepared statement. You may also use
   * the shorthand form {@code "attribute"}, in which case {@code "attribute = ?"} will be
   * inferred.
   * @param object Any Java object that can be inserted into a prepared statement.
   * @return an {@code SQLChain} for further additions.
   * @since 2018-05-14
   */
  public static SQLChain where(Class<? extends Model> clazz, String condition, Object object) {
    return new SQLChain(clazz).where(condition, object);
  }


  /**
   * Starts an {@code SQLChain}, that lets you find a single instance of a model.
   *
   * @param clazz Class to query for.
   * @param attribute Attribute to check object against. Must be unique.
   * @param object Object to compare to attribute.
   * @return an {@code SQLChain} for further additions.
   * @since 2018-05-14
   */
  public static SQLChain find(Class<? extends Model> clazz, String attribute, Object object) {
    return new SQLChain(clazz).where(attribute, object).limit(1);
  }


  /**
   * Attempts to validate the model before any database operations are carried out. Also, be sure to
   * include an errors using addError(). Errors will be shown to the user on forms.
   *
   * @return Whether this model is considered valid.
   * @see #beforeValidate()
   * @see #afterValidate()
   * @see #addError(String)
   * @since 2018-05-14
   */
  public abstract boolean validate();


  /**
   * Validates, and then attempts to save this model to the database. Note: you should only call
   * this function when you wish to insert a new model into the database.
   *
   * @return Whether the model could be saved.
   * @see #update()
   * @see #beforeSave()
   * @see #afterSave()
   * @since 2018-05-14
   */
  public boolean save() throws SQLException, SQLAdapterException {

    // Validate
    beforeValidate();
    boolean valid = validate();
    afterValidate();

    // Ensure valid
    if (!valid) {
      return false;
    }

    beforeSave();
    QueryExecutor.save(this);
    afterSave();
    return true;

  }


  /**
   * Validates, and then attempts to update this model in the database. Note: you should only call
   * this function when you wish to update an already existing model.
   *
   * @return Whether the model could be updated.
   * @see #save()
   * @see #beforeUpdate()
   * @see #afterUpdate()
   * @since 2018-05-14
   */
  public boolean update() throws SQLException, SQLAdapterException {

    // Validate
    beforeValidate();
    boolean valid = validate();
    afterValidate();

    // Ensure valid
    if (!valid) {
      return false;
    }

    beforeUpdate();
    QueryExecutor.update(this);
    afterUpdate();
    return true;

  }


  /**
   * A function that is always called just before before validating this model. You may wish to
   * perform some cleanup here.
   *
   * @see #validate()
   * @since 2018-05-14
   */
  @SuppressWarnings("WeakerAccess")
  protected void beforeValidate() {
  }


  /**
   * A function that is always called just before after validating this model. You may wish to
   * perform some cleanup here.
   *
   * @see #validate()
   * @since 2018-05-14
   */
  @SuppressWarnings("WeakerAccess")
  protected void afterValidate() {
  }


  /**
   * A function that is always called just before saving this model to the database. You may wish to
   * perform some cleanup here.
   *
   * @see #save()
   * @since 2018-05-14
   */
  @SuppressWarnings("WeakerAccess")
  protected void beforeSave() {
  }


  /**
   * A function that is always called just after saving this model to the database. You may wish to
   * perform some cleanup here.
   *
   * @see #save()
   * @since 2018-05-14
   */
  @SuppressWarnings("WeakerAccess")
  protected void afterSave() {
  }


  /**
   * A function that is always called just before updating this model in the database. You may wish
   * to perform some cleanup here.
   *
   * @see #update()
   * @since 2018-05-14
   */
  @SuppressWarnings("WeakerAccess")
  protected void beforeUpdate() {
  }


  /**
   * A function that is always called just after updating this model in the database. You may wish
   * to perform some cleanup here.
   *
   * @see #update()
   * @since 2018-05-14
   */
  @SuppressWarnings("WeakerAccess")
  protected void afterUpdate() {
  }


  @SuppressWarnings("unused")
  public List<String> getErrors() {
    return errors;
  }


  /**
   * Adds a validation error.
   *
   * @param error Error message to add.
   * @since 2018-04-15
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  void addError(String error) {
    errors.add(error);
  }

  public boolean isSaved() {
    return saved;
  }

  public void setSaved(boolean saved) {
    this.saved = saved;
  }

}
