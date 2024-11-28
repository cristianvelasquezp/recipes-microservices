package com.cristianvelasquezp.microservicerecipes.recipes.services;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.CategoryEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.Entities.IngredientEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {

    }


    @DisplayName("Should return a list of recipe entities when there are recipes in the database")
    @Test
    void testGetAllRecipes_whenIHaveRecipesInTheDatabase_returnListOfRecipeEntities() {
        // Given
        List<RecipeEntity> expectedRecipes = createRecipeEntities();
        when(recipeRepository.findAll()).thenReturn(expectedRecipes);

        // When
        List<RecipeEntity> actualRecipes = recipeService.getAllRecipes();

        // Then
        assertFalse(actualRecipes.isEmpty());
        assertTrue(actualRecipes.stream().allMatch(recipe -> recipe != null));
    }

    @DisplayName("Should return an empty list when there are no recipes in the database")
    @Test
    void testGetAllRecipes_whenNoRecipesInTheDatabase_returnEmptyList() {
        // Given
        List<RecipeEntity> expectedRecipes = new ArrayList<>();
        when(recipeRepository.findAll()).thenReturn(expectedRecipes);

        // When
        List<RecipeEntity> actualRecipes = recipeService.getAllRecipes();

        // Then
        assertTrue(actualRecipes.isEmpty());
    }

    @DisplayName("Should throw an exception with a specific message when the repository returns an error")
    @Test
    void testGetAllRecipes_whenRepositoryThrowsException_throwExceptionWithSpecificMessage() {
        // Given
        when(recipeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recipeService.getAllRecipes());
        assertEquals("An error occurred while fetching recipes: Database error", exception.getMessage());
    }


    public static List<RecipeEntity> createRecipeEntities() {
        List<RecipeEntity> recipeEntities = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            RecipeEntity recipe = new RecipeEntity();
            recipe.setId((long) i);
            recipe.setName("Recipe " + i);
            recipe.setDescription("Description for Recipe " + i);
            recipe.setCategory(new CategoryEntity((long) i, "Category " + i));
            recipe.setDate(new Timestamp(System.currentTimeMillis()));
            recipe.setDirections("Directions for Recipe " + i);
            recipe.setIngredients(List.of(new IngredientEntity((long) i, "Ingredient " + i)));
            recipe.setUserId(i);

            recipeEntities.add(recipe);
        }

        return recipeEntities;
    }
}