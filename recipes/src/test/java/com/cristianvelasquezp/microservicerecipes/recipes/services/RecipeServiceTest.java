package com.cristianvelasquezp.microservicerecipes.recipes.services;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.CategoryEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.Entities.IngredientEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.exceptions.RecipeNotFoundException;
import com.cristianvelasquezp.microservicerecipes.recipes.repositories.RecipeRepository;
import com.cristianvelasquezp.microservicerecipes.recipes.utils.RecipeTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private List<RecipeEntity> recipes;


    @BeforeEach
    void setUp() {
        RecipeTestUtils recipeTestUtils = new RecipeTestUtils();
        recipes = recipeTestUtils.createRecipeEntities();
    }


    @DisplayName("Should return a list of recipe entities when there are recipes in the database")
    @Test
    void testGetAllRecipes_whenIHaveRecipesInTheDatabase_returnListOfRecipeEntities() {
        // Given
        when(recipeRepository.findAll()).thenReturn(recipes);

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

    // Test for getRecipeById

    @DisplayName("Should return a recipe entity when the recipe exists in the database")
    @Test
    void testGetRecipeById_whenRecipeExists_returnRecipeEntity() {
        // Given
        when(recipeRepository.findById(1L)).thenReturn(java.util.Optional.of(recipes.getFirst()));

        // When
        Optional<RecipeEntity> actualRecipe = recipeService.getRecipeById(1L);

        // Then
        assertTrue(actualRecipe.isPresent());
        assertEquals(1L, actualRecipe.get().getId());
        assertEquals("Spaghetti Bolognese", actualRecipe.get().getName());
        assertEquals("Classic Italian pasta dish with a rich meat sauce.", actualRecipe.get().getDescription());
        assertEquals("Italian", actualRecipe.get().getCategory().getName());
        assertEquals("Cook pasta. Prepare sauce. Combine and serve.", actualRecipe.get().getDirections());
        assertEquals("Spaghetti", actualRecipe.get().getIngredients().getFirst().getValue());
        assertEquals(1, actualRecipe.get().getUserId());
    }

    @DisplayName("Should return an empty optional when the recipe does not exist in the database")
    @Test
    void testGetRecipeById_whenRecipeDoesNotExist_returnEmptyOptional() {
        // Given
        when(recipeRepository.findById(12L)).thenReturn(Optional.empty());

        // When
        Optional<RecipeEntity> actualRecipe = recipeService.getRecipeById(12L);

        // Then
        assertTrue(actualRecipe.isEmpty());
    }

    @DisplayName("Should throw an exception with a specific message when the repository returns an error")
    @Test
    void testGetRecipeById_whenRepositoryThrowsException_throwExceptionWithSpecificMessage() {
        // Given
        long id = 12L;
        when(recipeRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recipeService.getRecipeById(id));
        assertEquals("An error occurred while fetching recipe with id: " + id + " Database error", exception.getMessage());
    }

    // Test for createRecipeEntities

    @DisplayName("Should return a recipe entity with id when the recipe is created")
    @Test
    void testCreateRecipeEntities_whenRecipeIsCreated_returnRecipeEntityWithId() {
        // Given
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName("Recipe 11");
        recipe.setDescription("Description for Recipe 11");
        recipe.setCategory(new CategoryEntity(11L, "Category 11"));
        recipe.setDate(new Timestamp(System.currentTimeMillis()));
        recipe.setDirections("Directions for Recipe 11");
        recipe.setIngredients(List.of(new IngredientEntity(11L, "Ingredient 11")));
        recipe.setUserId(11);
        when(recipeRepository.save(recipe)).thenReturn(getRecipeWithId(recipe));

        // When
        RecipeEntity actualRecipe = recipeService.createRecipe(recipe);

        // Then
        assertNotNull(actualRecipe);
        assertEquals(11L, actualRecipe.getId());
        assertEquals("Recipe 11", actualRecipe.getName());
        assertEquals("Description for Recipe 11", actualRecipe.getDescription());
        assertEquals("Category 11", actualRecipe.getCategory().getName());
        assertEquals("Directions for Recipe 11", actualRecipe.getDirections());
        assertEquals("Ingredient 11", actualRecipe.getIngredients().getFirst().getValue());
        assertEquals(11, actualRecipe.getUserId());
    }

    @DisplayName("Should throw an exception with a specific message when the repository returns an error")
    @Test
    void testCreateRecipeEntities_whenRepositoryThrowsException_throwExceptionWithSpecificMessage() {
        // Given
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName("Recipe 11");
        recipe.setDescription("Description for Recipe 11");
        recipe.setCategory(new CategoryEntity(11L, "Category 11"));
        recipe.setDate(new Timestamp(System.currentTimeMillis()));
        recipe.setDirections("Directions for Recipe 11");
        recipe.setIngredients(List.of(new IngredientEntity(11L, "Ingredient 11")));
        recipe.setUserId(11);
        when(recipeRepository.save(recipe)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recipeService.createRecipe(recipe));
        assertEquals("An error occurred while creating recipe: Database error", exception.getMessage());
    }

    // Test for updateRecipe

    @DisplayName("Should return a recipe entity with updated values when the recipe is updated")
    @Test
    void testUpdateRecipe_whenRecipeIsUpdated_returnRecipeEntityWithUpdatedValues() {
        // Given
        RecipeEntity recipe = recipes.getFirst();
        recipe.setName("Updated Recipe 1");
        recipe.setDescription("Updated Description for Recipe 1");
        recipe.setCategory(new CategoryEntity(11L, "Updated Category 1"));
        recipe.setDate(new Timestamp(System.currentTimeMillis()));
        recipe.setDirections("Updated Directions for Recipe 1");
        recipe.setIngredients(List.of(new IngredientEntity(11L, "Updated Ingredient 1")));
        recipe.setUserId(11);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        // When
        RecipeEntity actualRecipe = recipeService.updateRecipe(recipe);

        // Then
        assertNotNull(actualRecipe);
        assertEquals(1L, actualRecipe.getId());
        assertEquals("Updated Recipe 1", actualRecipe.getName());
        assertEquals("Updated Description for Recipe 1", actualRecipe.getDescription());
        assertEquals("Updated Category 1", actualRecipe.getCategory().getName());
        assertEquals("Updated Directions for Recipe 1", actualRecipe.getDirections());
        assertEquals("Updated Ingredient 1", actualRecipe.getIngredients().getFirst().getValue());
        assertEquals(11, actualRecipe.getUserId());
    }

    @DisplayName("Should throw an exception with a specific message when the recipe does not have an id")
    @Test
    void testUpdateRecipe_whenRecipeDoesNotHaveId_throwExceptionWithSpecificMessage() {
        // Given
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName("Updated Recipe 1");
        recipe.setDescription("Updated Description for Recipe 1");
        recipe.setCategory(new CategoryEntity(11L, "Updated Category 1"));
        recipe.setDate(new Timestamp(System.currentTimeMillis()));
        recipe.setDirections("Updated Directions for Recipe 1");
        recipe.setIngredients(List.of(new IngredientEntity(11L, "Updated Ingredient 1")));
        recipe.setUserId(11);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(recipe));
        assertEquals("Recipe must have an id to be updated", exception.getMessage());
    }

    @DisplayName("Should throw an exception with a specific message when the recipe does not exist in the database")
    @Test
    void testUpdateRecipe_whenRecipeDoesNotExist_throwExceptionWithSpecificMessage() {
        // Given
        RecipeEntity recipe = recipes.getFirst();
        recipe.setId(11L);
        recipe.setName("Updated Recipe 1");
        when(recipeRepository.findById(11L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recipeService.updateRecipe(recipe));
        assertEquals("An error occurred while updating recipe: Recipe with id 11 not found", exception.getMessage());
    }

    @DisplayName("Should throw an exception with a specific message when the repository returns an error")
    @Test
    void testUpdateRecipe_whenRepositoryThrowsException_throwExceptionWithSpecificMessage() {
        // Given
        RecipeEntity recipe = recipes.getFirst();
        recipe.setName("Updated Recipe 1");
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(recipe)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recipeService.updateRecipe(recipe));
        assertEquals("An error occurred while updating recipe: Database error", exception.getMessage());
    }

    // Test for deleteRecipe

    @DisplayName("Should return true when the recipe is deleted")
    @Test
    void testDeleteRecipe_whenRecipeIsDeleted_returnTrue() {
        // Given
        long id = 1L;
        when(recipeRepository.existsById(id)).thenReturn(true);
        // When
        boolean isDeleted = recipeService.deleteRecipe(id);

        // Then
        assertTrue(isDeleted);
    }

    @DisplayName("Should throw an exception with a specific message when the id does not exist in the database")
    @Test
    void testDeleteRecipe_whenIdDoesNotExist_throwExceptionWithSpecificMessage() {
        // Given
        long id = 11L;
        when(recipeRepository.existsById(id)).thenReturn(false);
        // When & Then
        RuntimeException exception = assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteRecipe(id));
        assertEquals("An error occurred while deleting recipe: Recipe with id 11 not found", exception.getMessage());
    }

    private RecipeEntity getRecipeWithId(RecipeEntity recipe) {
        recipe.setId(11L);
        return recipe;
    }
}