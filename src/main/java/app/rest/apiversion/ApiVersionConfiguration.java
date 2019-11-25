package app.rest.apiversion;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class ApiVersionConfiguration implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        RequestMappingHandlerMapping apiVersionMappingHandler = new ApiVersionRequestMappingHandlerMapping();
        apiVersionMappingHandler.setOrder(0);
        return apiVersionMappingHandler;
    }
}
