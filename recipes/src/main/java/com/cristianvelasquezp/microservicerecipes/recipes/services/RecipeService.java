package com.cristianvelasquezp.microservicerecipes.recipes.services;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeEntity> getAllRecipes() {
        return recipeRepository.findAll();
    }
}
