package app.rest.apiversion;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Component
//@Scope("prototype")
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private final List<String> versions;

    public ApiVersionCondition(List<String> versions) {
        this.versions = versions;
    }


    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return merge(versions, other.versions);
    }

    private ApiVersionCondition merge(List<String> versions1, List<String> versions2) {
        List<String> allVersions = Stream.concat(versions1.stream(), versions2.stream())
                .distinct()
                .collect(Collectors.toList());
        return new ApiVersionCondition(allVersions);
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        String apiVersion = httpServletRequest.getHeader("api-version");

        if (apiVersion != null && versions.contains(apiVersion)){
            return this;
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition apiVersion, HttpServletRequest httpServletRequest) {
        return 0;
    }
}
