package com.cristianvelasquezp.microservicerecipes.recipes.services;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;

import java.util.List;

public interface RecipeService {

    public List<RecipeEntity> getAllRecipes();
}
