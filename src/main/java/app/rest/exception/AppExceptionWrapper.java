package app.rest.exception;

public class AppExceptionWrapper {


    private String message;
    private String cause;

    public AppExceptionWrapper(String message, String cause) {
        this.message = message;
        this.cause = cause;
    }

    public AppExceptionWrapper(AppException ex) {
        this.message = ex.getMessage();
        if(ex.getCause() != null){
            this.cause = ex.getCause().getMessage();
        }
    }

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }
}
