package app.core.apiversion;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Component
public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        ApiVersion annotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(annotation);
    }

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion annotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createCondition(annotation);
    }

    private RequestCondition<?> createCondition(ApiVersion annotation) {
        List<String> versions = new ArrayList<>();
        if(annotation != null){
            versions.addAll(Arrays.asList(annotation.versions()));
            versions.add(annotation.value());
            versions.stream()
                    .filter(v -> !v.isEmpty())
                    .collect(Collectors.toList());
        }

        //return obtainApplicationContext().getBean(ApiVersionCondition.class, version);
        return (versions != null && !versions.isEmpty() ? new ApiVersionCondition(versions) : null);
    }
}
