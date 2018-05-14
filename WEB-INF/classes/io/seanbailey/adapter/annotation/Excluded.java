package io.seanbailey.adapter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation which marks a field as excluded. When generating SQL for inserting or updating,
 * this field is effectively ignored.
 *
 * @since 2018-05-14
 */
@SuppressWarnings("unused")
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excluded {

}
