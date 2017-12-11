package com.leibangzhu.iris.core.annotation;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {
    String value() default "";
    Class<?> interfaceClass();
}
