package com.cristianvelasquezp.microservicerecipes.users.controllers;

import com.cristianvelasquezp.microservicerecipes.users.exceptions.DatabaseConnectionException;
import com.cristianvelasquezp.microservicerecipes.users.exceptions.UserNotFoundException;
import com.cristianvelasquezp.microservicerecipes.users.models.ErrorResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseModel> handleDatabaseConnectionException(DatabaseConnectionException e) {
        return ResponseEntity.status(500).body(new ErrorResponseModel("Database connection error", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseModel> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(404).body(new ErrorResponseModel("User not found", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseModel> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(new ErrorResponseModel("Bad request", e.getMessage()));
    }

}
