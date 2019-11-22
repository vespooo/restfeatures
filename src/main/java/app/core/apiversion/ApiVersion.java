package app.core.apiversion;

import java.lang.annotation.*;

/**
 * Maps requests by custom header api-version
 * Example:
 * api-version: 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    /**
     *API version list
     */
   String value() default "";
   String[] versions() default {};
}

