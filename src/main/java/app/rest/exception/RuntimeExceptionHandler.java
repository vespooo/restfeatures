package app.rest.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public ResponseEntity<AppExceptionWrapper> exceptionHandler (AppException ex) {
        return createResponse(ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<AppExceptionWrapper> notReadableExceptionHandler (Exception ex) {
        return createResponse( new AppException(ErrorCode.MISSING_REQUEST_BODY, ex));
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<AppExceptionWrapper> typeMismatchExceptionHandler (Exception ex) {
        return createResponse(new AppException(ErrorCode.WRONG_PARAMETER_TYPE, ex));
    }

    private ResponseEntity<AppExceptionWrapper> createResponse(AppException ex) {
        HttpStatus status;
        switch (ex.getErrorCode()){
            case CONFLICT :  status = HttpStatus.CONFLICT; break;
            case NOT_FOUND : status = HttpStatus.NOT_FOUND; break;
            case INVALID_REQUEST_BODY : status = HttpStatus.BAD_REQUEST; break;
            case MISSING_REQUEST_BODY : status = HttpStatus.BAD_REQUEST; break;
            case WRONG_PARAMETER_TYPE : status = HttpStatus.BAD_REQUEST; break;
            default: status =HttpStatus.INTERNAL_SERVER_ERROR;
        }

        AppExceptionWrapper exception = new AppExceptionWrapper(ex);
        return new ResponseEntity<>(exception, status);
    }
}
