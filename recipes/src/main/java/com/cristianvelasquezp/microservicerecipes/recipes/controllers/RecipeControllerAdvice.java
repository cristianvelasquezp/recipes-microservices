package com.cristianvelasquezp.microservicerecipes.recipes.controllers;

import com.cristianvelasquezp.microservicerecipes.recipes.exceptions.DatabaseConnectionException;
import com.cristianvelasquezp.microservicerecipes.recipes.exceptions.RecipeNotFoundException;
import com.cristianvelasquezp.microservicerecipes.recipes.models.ErrorResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class RecipeControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseModel> handleDatabaseConnectionException(DatabaseConnectionException e) {
        return ResponseEntity.status(500).body(new ErrorResponseModel("Database connection error", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseModel> handleRecipeNotFoundException(RecipeNotFoundException e) {
        return ResponseEntity.status(404).body(new ErrorResponseModel("Recipe not found", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseModel> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(new ErrorResponseModel("Bad request", e.getMessage()));
    }

}
