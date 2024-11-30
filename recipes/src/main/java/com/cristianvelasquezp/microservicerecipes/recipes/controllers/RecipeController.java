package com.cristianvelasquezp.microservicerecipes.recipes.controllers;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.exceptions.RecipeNotFoundException;
import com.cristianvelasquezp.microservicerecipes.recipes.services.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeEntity> getRecipeById(@PathVariable String id) {
        Long idLong = Long.parseLong(id);
        Optional<RecipeEntity> recipe = recipeService.getRecipeById(idLong);
        if (recipe.isPresent()) {
            return ResponseEntity.ok(recipe.get());
        }else {
            throw new RecipeNotFoundException("Recipe with id: " + idLong + " not found");
        }
    }

    @PostMapping("/recipes")
    public RecipeEntity createRecipe(@RequestBody RecipeEntity recipe) {
        return recipeService.createRecipe(recipe);
    }

    @PutMapping("/recipes")
    public RecipeEntity updateRecipe(@RequestBody RecipeEntity recipe) {
        return recipeService.updateRecipe(recipe);
    }

    @DeleteMapping("/recipes/{id}")
    public boolean deleteRecipe(@PathVariable String id) {
        Long idLong = Long.parseLong(id);
        return recipeService.deleteRecipe(idLong);
    }
}
