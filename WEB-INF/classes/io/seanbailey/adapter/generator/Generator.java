package io.seanbailey.adapter.generator;

import io.seanbailey.adapter.exception.SQLAdapterException;
import java.util.List;

public abstract class Generator {

  protected String sql;
  protected List<Object> objects;


  /**
   * Generates SQL and a list of objects for a prepared statement.
   *
   * @since 2018-05-14
   */
  protected abstract void generate() throws SQLAdapterException;


  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public List<Object> getObjects() {
    return objects;
  }

  public void setObjects(List<Object> objects) {
    this.objects = objects;
  }
}
