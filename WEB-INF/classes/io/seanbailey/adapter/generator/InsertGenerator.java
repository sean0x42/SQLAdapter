package io.seanbailey.adapter.generator;

import io.seanbailey.adapter.Adapter;
import io.seanbailey.adapter.exception.SQLAdapterException;
import io.seanbailey.adapter.Model;
import io.seanbailey.adapter.util.SQLUtil;
import java.util.List;
import java.util.StringJoiner;

/**
 * Generates SQL insert statements.
 *
 * @author Sean Bailey
 * @see Model
 * @see io.seanbailey.adapter.annotation.Excluded
 * @since 2018-05-14
 */
public class InsertGenerator extends Generator {

  private Model model;
  private List<String> attributes;


  /**
   * Constructs a new Insert Generator.
   *
   * @param model Instance of a model to insert.
   * @since 2018-05-14
   * @throws SQLAdapterException if the model cannot be mapped to SQL for some reason.
   */
  public <T extends Model> InsertGenerator(T model) throws SQLAdapterException {
    this.model = model;
    this.attributes = Adapter.getAttributesFromModel(model.getClass());
    this.objects = Adapter.getAttributesFromInstance(model, attributes);
    generate();
  }


  /**
   * Generates an SQL insert statement.
   *
   * @since 2018-05-14
   */
  @Override
  protected void generate() {

    // Create start of statement
    StringJoiner joiner = new StringJoiner(" ");
    joiner.add("INSERT INTO");
    joiner.add(SQLUtil.getTableName(model.getClass()));

    // Add attributes
    StringJoiner attributeJoiner = new StringJoiner(", ", "(", ")");
    for (String attribute : attributes) {
      attributeJoiner.add(attribute);
    }
    joiner.add(attributeJoiner.toString());

    // Add space for prepared values
    joiner.add("VALUES");
    StringJoiner valueJoiner = new StringJoiner(", ", "(", ")");
    for (int i = 0; i < attributes.size(); i++) {
      valueJoiner.add("?");
    }
    joiner.add(valueJoiner.toString());

    this.sql = joiner.toString() + ";";

  }


}
