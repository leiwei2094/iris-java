package com.leiwei2094.iris.core.annotation;

import java.lang.annotation.*;

/**
 * @author wei.lei
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Service {
    Class<?> interfaceClass() default void.class;
}
