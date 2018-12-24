package io.seanbailey.sqladapter;

import io.seanbailey.sqladapter.config.Case;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the core components of the SQL adapter.
 * @see io.seanbailey.sqladapter.Adapter
 */
public class AdapterTests {

  @Test
  public void testTableNameInference() {
    // Step 1: snake_case
    SQLAdapter.setTableNamingConvention(Case.SNAKE);
    // assertEquals(Adapter.inferTableName(TestModel.class), "test_model");

    // Step 2: CamelCase
    SQLAdapter.setTableNamingConvention(Case.CAMEL);
    assertEquals(Adapter.inferTableName(TestModel.class), "TestModel");

    // Step 3: KebabCase
    SQLAdapter.setTableNamingConvention(Case.KEBAB);
    // assertEquals(Adapter.inferTableName(TestModel.class), "test-model");
  }
}
