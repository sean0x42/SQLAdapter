package io.seanbailey.adapter.generator;

import io.seanbailey.adapter.SQLChain;
import io.seanbailey.adapter.SQLChain.Finisher;
import io.seanbailey.adapter.util.Order;
import io.seanbailey.adapter.util.SQLUtil;
import io.seanbailey.adapter.util.WhereOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Generates an SQL query based off of an SQL chain.
 *
 * @author Sean Bailey
 * @see SQLChain
 * @since 2018-05-14
 */
@SuppressWarnings("WeakerAccess")
public class SelectGenerator extends Generator {

  private SQLChain chain;
  private Finisher finisher;


  /**
   * Constructs a new SQL Query Generator.
   *
   * @param chain SQL Chain to generate query from.
   * @param finisher The operation that finished this chain.
   * @since 2018-05-14
   */
  public SelectGenerator(SQLChain chain, Finisher finisher) {
    this.chain = chain;
    this.finisher = finisher;
    generate();
  }


  /**
   * Generates SQL.
   *
   * @since 2018-05-14
   */
  @Override
  protected void generate() {

    this.objects = new ArrayList<>();

    // init
    StringJoiner joiner = new StringJoiner(" ");
    joiner.add(generateQueryStart());

    // Iterate over wheres
    List<WhereOperation> wheres = chain.getWheres();
    boolean first = true;
    for (WhereOperation where : wheres) {

      // Add predicate
      if (first) {
        joiner.add("WHERE");
      } else {
        joiner.add(where.getType().toString());
      }

      joiner.add(where.getCondition());
      objects.add(where.getObject());
      first = false;

    }

    // Handle orders
    if (!chain.getOrders().isEmpty()) {
      joiner.add(generateOrders());
    }

    // Limit
    if (chain.getLimit() != null) {
      joiner.add("LIMIT " + chain.getLimit());
    }

    // Offset
    if (chain.getPage() != null || chain.getOffset() != null) {
      joiner.add(generateOffset());
    }

    sql = joiner.toString() + ";";

  }


  /**
   * Generates the start of a query, including the table name.
   *
   * @return The start of an SQL query.
   * @since 2018-05-14
   */
  private String generateQueryStart() {

    StringJoiner joiner = new StringJoiner(" ");

    // Determine what to select based on finisher
    switch (finisher) {
      case COUNT:
      case EXISTS:
        joiner.add("SELECT COUNT(*) FROM");
        break;
      case EXECUTE:
      default:
        joiner.add("SELECT * FROM");
    }

    // Add table name
    joiner.add(SQLUtil.getTableName(chain.getClazz()));

    return joiner.toString();

  }


  /**
   * Generates SQL that is used for ordering results.
   *
   * @return An SQL String containing an order by statement.
   * @since 2018-05-14
   */
  private String generateOrders() {

    StringJoiner joiner = new StringJoiner(" ");
    Map<String, Order> orders = chain.getOrders();

    joiner.add("ORDER BY");

    // Iterate over hashmap
    for (String attribute : orders.keySet()) {
      joiner.add(attribute);
      joiner.add(orders.get(attribute).toSQL() + ",");
    }

    // Remove final character (will always be a trailing comma).
    String out = joiner.toString();
    return out.substring(0, out.length() - 1);

  }


  /**
   * Generates the SQL responsible for offsetting the result set by n rows.
   *
   * @return SQL that will offset the result set.
   * @since 2018-05-14
   */
  private String generateOffset() {

    int offset = 0;

    // Handle offset via page
    if (chain.getPage() != null && chain.getLimit() != null) {
      offset = chain.getPage() * chain.getLimit();
    }

    // Handle offset via offset
    if (chain.getOffset() != null) {
      offset = chain.getOffset();
    }

    return "OFFSET " + offset;

  }


}
