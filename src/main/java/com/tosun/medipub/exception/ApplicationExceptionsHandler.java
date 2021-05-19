package com.tosun.medipub.exception;

import com.tosun.medipub.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

//@ControllerAdvice
public class ApplicationExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception e, WebRequest request){

        if(e.getLocalizedMessage() != null){
            ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getLocalizedMessage());
            return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            return new ResponseEntity<>(e.toString(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e, WebRequest request){

        if(e.getLocalizedMessage() != null){
            ErrorMessage errorMessage = new ErrorMessage(new Date(), "Null Pointer Exception1 : " + e.getLocalizedMessage());
            return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            return new ResponseEntity<>("Null Pointer Exception2 : " + e.toString(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
