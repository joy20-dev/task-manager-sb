package com.JoyBoy.ToDo.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    public ResponseEntity<?> handleIllegallArgument (IllegalArgumentException ex){
        return ResponseEntity
                .badRequest()
                .body(Map.of("error",ex.getMessage()));
    }

    public ResponseEntity<String> handleRunTimeException( RuntimeException ex){
        return ResponseEntity
                .badRequest()
                .body("something went wrong" + ex.getMessage());
    }
    
}
