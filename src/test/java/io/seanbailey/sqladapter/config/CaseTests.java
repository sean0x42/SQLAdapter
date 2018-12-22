package io.seanbailey.sqladapter.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests case conversion tools.
 */
public class CaseTests {

  @Test
  public void testSingleLetter() {
    final String string = "A";
    // assertEquals("a", Case.toSnakeCase(string));
    // assertEquals("A", Case.toCamelCase(string));
    // assertEquals("a", Case.toKebabCase(string));
  }

  @Test
  public void testClassNames() {
    final String standardClass = "CaseTests";
    // assertEquals("case_tests", Case.toSnakeCase(standardClass));
    // assertEquals("CaseTests", Case.toCamelCase(standardClass));
    // assertEquals("case-tests", Case.toKebabCase(standardClass));

    final String initialismClass = "HTMLEntity";
    // assertEquals("html_entity", Case.toSnakeCase(initialismClass));
    // assertEquals("HTMLEntity", Case.toCamelCase(initialismClass));
    // assertEquals("html-entity", Case.toKebabCase(initialismClass));
  }

  @Test
  public void testRegularPhrase() {
    final String phrase = "The quick brown fox";
    // assertEquals("the_quick_brown_fox", Case.toSnakeCase(phrase));
    // assertEquals("TheQuickBrownFox", Case.toCamelCase(phrase));
    // assertEquals("the-quick-brown-fox", Case.toKebabCase(phrase));
  }
}
