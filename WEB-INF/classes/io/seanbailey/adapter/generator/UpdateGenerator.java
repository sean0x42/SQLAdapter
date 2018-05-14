package io.seanbailey.adapter.generator;

import io.seanbailey.adapter.Adapter;
import io.seanbailey.adapter.Model;
import io.seanbailey.adapter.exception.SQLAdapterException;
import io.seanbailey.adapter.util.SQLUtil;
import java.util.List;
import java.util.StringJoiner;

/**
 * Generates SQL update statements.
 *
 * @author Sean Bailey
 * @see Model
 * @see io.seanbailey.adapter.annotation.Excluded
 * @since 2018-05-14
 */
public class UpdateGenerator extends Generator {


  private Model model;
  private List<String> attributes;


  /**
   * Constructs a new Update Generator.
   *
   * @param model Instance of a model to update.
   * @since 2018-05-14
   * @throws SQLAdapterException if the model cannot be mapped to SQL for some reason.
   */
  public <T extends Model> UpdateGenerator(T model) throws SQLAdapterException {
    this.model = model;
    this.attributes = Adapter.getAttributesFromModel(model.getClass());
    this.objects = Adapter.getAttributesFromInstance(model, attributes);
    generate();
  }


  /**
   * Generates SQL and a list of objects for a prepared statement.
   *
   * @since 2018-05-14
   */
  @Override
  protected void generate() throws SQLAdapterException {

    // Create start of statement
    StringJoiner joiner = new StringJoiner(" ");
    joiner.add("UPDATE");
    joiner.add(SQLUtil.getTableName(model.getClass()));

    // Add attributes
    joiner.add("SET");
    StringJoiner attributeJoiner = new StringJoiner(", ");
    for (String attribute : attributes) {
      attributeJoiner.add(String.format("%s = ?", attribute));
    }
    joiner.add(attributeJoiner.toString());

    // Add condition
    joiner.add("WHERE");
    String primaryKey = Adapter.getPrimaryKey(model.getClass());
    joiner.add(String.format("%s = ?", primaryKey));
    objects.add(Adapter.getAttributeFromInstance(model, primaryKey));

    this.sql = joiner.toString() + ";";

  }

}
