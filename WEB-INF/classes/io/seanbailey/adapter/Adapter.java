package io.seanbailey.adapter;

import io.seanbailey.adapter.annotation.Excluded;
import io.seanbailey.adapter.annotation.PrimaryKey;
import io.seanbailey.adapter.exception.SQLAdapterException;
import io.seanbailey.util.StringUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for converting Java to SQL and back again.
 *
 * @author Sean Bailey
 * @see SQLChain
 * @since 2018-05-14
 */
public class Adapter {


  /**
   * Uses reflection to create a new model. Then populates it's contents with the current result in
   * the result set.
   *
   * @param clazz Class to instantiate.
   * @param attributes An array of all attributes.
   * @param results A Result Set.
   * @return A new model. Created with reflection.
   * @since 2018-05-14
   */
  public static Model createModel(Class<? extends Model> clazz, List<String> attributes,
      ResultSet results) throws SQLException, SQLAdapterException {

    try {

      // Instantiate
      Model model = clazz.newInstance();

      // Iterate over attributes
      for (int i = 0; i < attributes.size(); i++) {

        // Get attribute name and data
        String attribute = attributes.get(i);
        Object data = results.getObject(i + 1);

        setAttribute(model, attribute, data);

      }

      model.setSaved(true);
      return model;

    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
      throw new SQLAdapterException(String.format(
          "Could not create model of type %s. Reason: %s",
          clazz.getSimpleName(),
          e.getMessage()
      ));
    }

  }


  /**
   * Gets a list of attributes from a result set.
   *
   * @param results Result Set.
   * @return A list of attributes.
   * @since 2018-05-14
   */
  public static List<String> getAttributesFromResults(ResultSet results) throws SQLException {

    // Ensure result set isn't null
    if (results == null) {
      return null;
    }

    // Retrieve metadata
    ResultSetMetaData meta = results.getMetaData();
    List<String> attributes = new ArrayList<>();

    // Determine columns
    for (int i = 0; i < meta.getColumnCount(); i++) {
      attributes.add(meta.getColumnName(i + 1));
    }

    return attributes;

  }


  /**
   * Uses reflection to retrieve a list of all attributes from a model. Any field that is annotated
   * as excluded will be ignored.
   *
   * @param clazz Model class to retrieve columns from.
   * @return A list of all SQL columns.
   * @see Excluded
   * @since 2018-05-14
   */
  public static List<String> getAttributesFromModel(Class<? extends Model> clazz) {

    // Create list
    List<String> attributes = new ArrayList<>();

    // Get and iterate over private fields
    for (Field field : clazz.getDeclaredFields()) {
      String name = field.getName();

      // Iterate over annotations
      boolean excluded = false;
      for (Annotation annotation : field.getDeclaredAnnotations()) {

        // Look for @Excluded annotations
        if (annotation.annotationType().equals(Excluded.class)) {
          excluded = true;
          break;
        }
      }

      // Add attribute
      if (!excluded) {
        attributes.add(name);
      }

    }

    return attributes;

  }


  /**
   * Retrieves all attribute values from a model, according to a list of attributes. Useful for
   * prepared statements.
   *
   * @param model Model to retrieve values from.
   * @param attributes A list of attributes.
   * @return A list of objects, found at each corresponding field.
   * @throws SQLAdapterException if the model cannot be mapped to SQL for some reason.
   * @see #getAttributeFromInstance(T, String)
   * @since 2018-05-14
   */
  public static <T extends Model> List<Object> getAttributesFromInstance(T model,
      List<String> attributes)
      throws SQLAdapterException {

    List<Object> objects = new ArrayList<>();

    // Iterate over attributes
    for (String column : attributes) {
      objects.add(getAttributeFromInstance(model, column));
    }

    return objects;

  }


  /**
   * Uses reflection to retrieve a model's attribute.
   *
   * @param model Model to retrieve values from.
   * @param attribute Attribute to retrieve.
   * @return Corresponding attribute value.
   * @throws SQLAdapterException if the model cannot be mapped to SQL for some reason.
   * @see #getAttributesFromInstance(T, List)
   * @since 2018-05-14
   */
  @SuppressWarnings("WeakerAccess")
  public static <T extends Model> Object getAttributeFromInstance(T model, String attribute)
      throws SQLAdapterException {

    try {

      // Search for getter first
      try {
        Method method = model.getClass().getMethod(StringUtil.toGetter(attribute));
        return method.invoke(model);
      } catch (NoSuchMethodException ignored) {
      }

      // No getter is available, get it directly from declared field
      Field field = model.getClass().getDeclaredField(attribute);
      field.setAccessible(true);
      return field.get(model);

    } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
      throw new SQLAdapterException(String.format(
          "Could not retrieve attribute value from %s. Exception: %s",
          model.getClass().getSimpleName(),
          e.getMessage()
      ));
    }

  }


  /**
   * Uses reflection to set a model's attribute.
   *
   * @param model Model to update.
   * @param attribute Attribute to set.
   * @param value Value to set.
   * @throws SQLAdapterException if the model cannot be mapped to SQL for some reason.
   * @since 2018-05-14
   */
  @SuppressWarnings("WeakerAccess")
  public static <T extends Model> void setAttribute(T model, String attribute, Object value)
      throws SQLAdapterException {

    try {

      // Search for setter first
      try {
        Method method = model.getClass().getMethod(
            StringUtil.toSetter(attribute),
            value.getClass()
        );
        method.invoke(model, value);
        return;
      } catch (NoSuchMethodException ignored) {
      }

      // No setter was found, set by field
      Field field = model.getClass().getDeclaredField(attribute);
      field.setAccessible(true);
      field.set(model, value);

    } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
      throw new SQLAdapterException(String.format(
          "Could not set attribute for %s. Exception: %s",
          model.getClass().getSimpleName(),
          e.getMessage()
      ));
    }

  }


  /**
   * Returns the annotated primary key from the given model.
   *
   * @param clazz Model to search for primary key in.
   * @return The name of the primary key attribute.
   * @since 2018-05-14
   * @throws SQLAdapterException if the class does not have a primary key.
   */
  public static String getPrimaryKey(Class<? extends Model> clazz) throws SQLAdapterException {

    // Get all declared fields
    for (Field field : clazz.getDeclaredFields()) {

      // Iterate over annotations
      for (Annotation annotation : field.getDeclaredAnnotations()) {
        if (annotation.annotationType().equals(PrimaryKey.class)) {
          return field.getName();
        }
      }

    }

    throw new SQLAdapterException(String.format(
        "The model %s does not have a primary key defined.",
        clazz.getSimpleName()
    ));

  }

}
