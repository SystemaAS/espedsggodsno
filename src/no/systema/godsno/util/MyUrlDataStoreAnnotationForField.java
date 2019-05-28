package no.systema.godsno.util;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface MyUrlDataStoreAnnotationForField {
    public String name();
    public String description();
    
}
