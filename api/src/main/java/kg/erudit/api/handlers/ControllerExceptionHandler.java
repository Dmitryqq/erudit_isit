package kg.erudit.api.handlers;

import io.jsonwebtoken.security.SignatureException;
import kg.erudit.common.resp.DefaultServiceResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<DefaultServiceResponse> handleException(Exception e) {
        log.error("Exception", e);
        DefaultServiceResponse response = new DefaultServiceResponse(true, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<DefaultServiceResponse> handleException(SignatureException e) {
        log.error("Exception", e);
//        DefaultServiceResponse response = new DefaultServiceResponse(true, e.getMessage());
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<DefaultServiceResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = "";
        for (final FieldError error : e.getBindingResult().getFieldErrors()){
            errorMessage += error.getField() + ": " + error.getDefaultMessage();
        }
        for (final ObjectError error : e.getBindingResult().getGlobalErrors()){
            errorMessage += error.getObjectName() + ": " + error.getDefaultMessage();
        }
        DefaultServiceResponse response = new DefaultServiceResponse(true, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
