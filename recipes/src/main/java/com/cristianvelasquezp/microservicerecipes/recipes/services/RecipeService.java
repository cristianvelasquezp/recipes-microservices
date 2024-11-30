package com.cristianvelasquezp.microservicerecipes.recipes.services;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    public List<RecipeEntity> getAllRecipes();

    public Optional<RecipeEntity> getRecipeById(Long id);

    public RecipeEntity createRecipe(RecipeEntity recipe);

    public RecipeEntity updateRecipe(RecipeEntity recipe);

    public boolean deleteRecipe(Long id);
}
