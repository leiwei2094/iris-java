package com.leiwei2094.iris.core.annotation;

import java.lang.annotation.*;

/**
 * @author wei.lei
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {
    Class<?> interfaceClass();
}
