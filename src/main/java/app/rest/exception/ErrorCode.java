package app.rest.exception;

public enum ErrorCode {
    CONFLICT("Entity already exists"),
    NOT_FOUND("Entity doesn't found"),
    INVALID_REQUEST_BODY("Couldn't serialize request body to object"),
    MISSING_REQUEST_BODY("Request body is missing"),
    WRONG_PARAMETER_TYPE("Wrong parameter type")
    ;

    private final String defaultMessage;

    ErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getMessage() {
        return defaultMessage;
    }
}
