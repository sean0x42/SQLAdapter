package io.seanbailey.sqladapter;

import io.seanbailey.sqladapter.util.ReflectionUtils;
import java.util.logging.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the reflection utils class.
 */
public class ReflectionTest {

  private static final Logger log = Logger.getLogger(ReflectionTest.class.getName());

  /**
   * Tests the utility's ability to infer class.
   */
  @Test
  public void testClassInferring() {
    SQLQuery query = TestModel.all();
    assertEquals(query.getClazz(), TestModel.class);
    log.info("Class: " + query.getClazz());
  }
}
