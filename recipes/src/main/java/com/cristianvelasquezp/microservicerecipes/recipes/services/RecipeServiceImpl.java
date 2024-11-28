package com.cristianvelasquezp.microservicerecipes.recipes.services;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            throw new RuntimeException("An error occurred while fetching recipes: " + e.getMessage(), e);
        }
    }

    public Optional<RecipeEntity> getRecipeById(Long id) {
        try {
            return recipeRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching recipe with id: " + id + " " + e.getMessage(), e);
        }
    }

    public RecipeEntity createRecipeEntities(RecipeEntity recipe) {
        try {
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating recipe: " + e.getMessage(), e);
        }
    }

    public RecipeEntity updateRecipe(RecipeEntity recipe) {
        try {
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating recipe: " + e.getMessage(), e);
        }
    }

    public boolean deleteRecipe(Long id) {
        try {
            recipeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting recipe with id: " + id + " " + e.getMessage(), e);
        }
    }
}
