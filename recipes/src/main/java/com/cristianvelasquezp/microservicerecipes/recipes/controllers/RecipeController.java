package com.cristianvelasquezp.microservicerecipes.recipes.controllers;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.services.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecipeController {

    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public List<RecipeEntity> getAllRecipes() {
        return recipeService.getAllRecipes();
    }
}
