package com.cristianvelasquezp.microservicerecipes.recipes.services;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.exceptions.DatabaseConnectionException;
import com.cristianvelasquezp.microservicerecipes.recipes.exceptions.RecipeNotFoundException;
import com.cristianvelasquezp.microservicerecipes.recipes.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RecipeServiceImpl implements RecipeService {

    RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    public List<RecipeEntity> getAllRecipes() {
        try {
            return recipeRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseConnectionException("An error occurred while fetching recipes: " + e.getMessage(), e);
        }
    }

    public Optional<RecipeEntity> getRecipeById(Long id) {
        try {
            return recipeRepository.findById(id);
        } catch (Exception e) {
            throw new DatabaseConnectionException("An error occurred while fetching recipe with id: " + id + " " + e.getMessage(), e);
        }
    }

    public RecipeEntity createRecipe(RecipeEntity recipe) {
        try {
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            Logger.getGlobal().severe("An error occurred while creating recipe: " + e.getMessage());
            throw new DatabaseConnectionException("An error occurred while creating recipe: " + e.getMessage(), e);
        }
    }

    public RecipeEntity updateRecipe(RecipeEntity recipe) {
        if (recipe.getId() == null) {
            throw new IllegalArgumentException("Recipe must have an id to be updated");
        }
        recipeRepository.findById(recipe.getId()).orElseThrow(() -> new RecipeNotFoundException("An error occurred while updating recipe: Recipe with id " + recipe.getId() + " not found"));
        try {
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new DatabaseConnectionException("An error occurred while updating recipe: " + e.getMessage(), e);
        }
    }

    public boolean deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException("An error occurred while deleting recipe: Recipe with id " + id + " not found");
        }
        try {
            recipeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DatabaseConnectionException("An error occurred while deleting recipe with id: " + id + " " + e.getMessage(), e);
        }
    }
}
