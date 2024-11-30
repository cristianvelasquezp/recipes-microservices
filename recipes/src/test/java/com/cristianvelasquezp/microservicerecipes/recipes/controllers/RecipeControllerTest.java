package com.cristianvelasquezp.microservicerecipes.recipes.controllers;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.IngredientEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.exceptions.DatabaseConnectionException;
import com.cristianvelasquezp.microservicerecipes.recipes.exceptions.RecipeNotFoundException;
import com.cristianvelasquezp.microservicerecipes.recipes.models.ErrorResponseModel;
import com.cristianvelasquezp.microservicerecipes.recipes.services.RecipeService;
import com.cristianvelasquezp.microservicerecipes.recipes.utils.RecipeTestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    RecipeService recipeService;

    RecipeTestUtils recipeTestUtils;

    List<RecipeEntity> recipes;

    @BeforeEach
    void setUp() {
        recipeTestUtils = new RecipeTestUtils();
        recipes = recipeTestUtils.createRecipeEntities();
    }

    @AfterEach
    void tearDown() {
        recipes = null;
    }

    @Test
    @DisplayName("Should return all recipes when there are recipes in the database")
    void testGetAllRecipes_whenThereAreRecipesInDatabase_thenReturnAllRecipes() throws Exception {
        //Given
        when(recipeService.getAllRecipes()).thenReturn(recipes);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/recipes");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<RecipeEntity>> jacksonTypeReference = new TypeReference<List<RecipeEntity>>() {};

        List<RecipeEntity> response = objectMapper.readValue(responseBodyAsString, jacksonTypeReference);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(recipes.size(), response.size());
        assertEquals(1L, response.getFirst().getId());
        assertEquals("Spaghetti Bolognese", response.getFirst().getName());
        assertEquals("Italian", response.getFirst().getCategory().getName());
        assertEquals("Cook pasta. Prepare sauce. Combine and serve.", response.getFirst().getDirections());
        assertEquals("Spaghetti", response.getFirst().getIngredients().getFirst().getValue());
        assertEquals(1, response.getFirst().getUserId());
    }

    @Test
    @DisplayName("Should return all recipes when there are no recipes in the database")
    void testGetAllRecipes_whenThereAreNoRecipesInDatabase_thenReturnEmptyList() throws Exception {
        //Given
        when(recipeService.getAllRecipes()).thenReturn(List.of());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/recipes");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<RecipeEntity>> jacksonTypeReference = new TypeReference<List<RecipeEntity>>() {};

        List<RecipeEntity> response = objectMapper.readValue(responseBodyAsString, jacksonTypeReference);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(0, response.size());
    }

    @Test
    @DisplayName("Should return error 500 when there is an error fetching recipes")
    void testGetAllRecipes_whenThereIsAnErrorFetchingRecipes_thenReturnError500() throws Exception {
        //Given
        when(recipeService.getAllRecipes()).thenThrow(new DatabaseConnectionException("An error occurred while fetching recipes: Connection refused", new Exception()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/recipes");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);


        //Then
        assertEquals(500, result.getResponse().getStatus());
        assertEquals("An error occurred while fetching recipes: Connection refused", response.getMessage());
    }

    @Test
    @DisplayName("Should return a recipe when the id exists")
    void testGetRecipeById_whenIdExists_thenReturnRecipe() throws Exception {
        //Given
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.ofNullable(recipes.getFirst()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/recipes/1");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        RecipeEntity response = objectMapper.readValue(responseBodyAsString, RecipeEntity.class);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(1L, response.getId());
        assertEquals("Spaghetti Bolognese", response.getName());
        assertEquals("Italian", response.getCategory().getName());
        assertEquals("Cook pasta. Prepare sauce. Combine and serve.", response.getDirections());
        assertEquals("Spaghetti", response.getIngredients().getFirst().getValue());
        assertEquals(1, response.getUserId());
    }

    @Test
    @DisplayName("Should return error 404 when the id does not exist")
    void testGetRecipeById_whenIdDoesNotExist_thenReturnError404() throws Exception {
        //Given
        when(recipeService.getRecipeById(12L)).thenReturn(Optional.empty());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/recipes/12");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(404, result.getResponse().getStatus());
        assertEquals("Recipe with id: 12 not found", response.getMessage());
    }

    @Test
    @DisplayName("Should return error 500 when there is an error fetching recipe by id")
    void testGetRecipeById_whenThereIsAnErrorFetchingRecipeById_thenReturnError500() throws Exception {
        //Given
        when(recipeService.getRecipeById(1L)).thenThrow(new DatabaseConnectionException("An error occurred while fetching recipe with id: 1 Connection refused", new Exception()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/recipes/1");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(500, result.getResponse().getStatus());
        assertEquals("An error occurred while fetching recipe with id: 1 Connection refused", response.getMessage());
    }

    @Test
    @DisplayName("Should return a recipe when a recipe is created successfully")
    void testCreateRecipe_whenRecipeIsCreated_thenReturnRecipe() throws Exception {
        //Given
        RecipeEntity newRecipe = new RecipeEntity();
        newRecipe.setName("Spaghetti Bolognese");
        newRecipe.setCategory(recipes.getFirst().getCategory());
        newRecipe.setDirections("Cook pasta. Prepare sauce. Combine and serve.");
        newRecipe.setIngredients(List.of(new IngredientEntity(1L, "Spaghetti")));
        newRecipe.setUserId(1);
        RecipeEntity recipeCreated = recipeTestUtils.addRecipeEntities(newRecipe);

        when(recipeService.createRecipe(any(RecipeEntity.class))).thenReturn(recipeCreated);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/recipes")
                .contentType("application/json")
                .accept("application/json")
                .content(new ObjectMapper().writeValueAsString(newRecipe));
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        RecipeEntity response = objectMapper.readValue(responseBodyAsString, RecipeEntity.class);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(11L, response.getId());
        assertEquals("Spaghetti Bolognese", response.getName());
        assertEquals("Italian", response.getCategory().getName());
        assertEquals("Cook pasta. Prepare sauce. Combine and serve.", response.getDirections());
        assertEquals("Spaghetti", response.getIngredients().getFirst().getValue());
        assertEquals(1, response.getUserId());
    }

    @Test
    @DisplayName("Should return error 500 when there is an error creating a recipe")
    void testCreateRecipe_whenThereIsAnErrorCreatingRecipe_thenReturnError500() throws Exception {
        //Given
        RecipeEntity newRecipe = new RecipeEntity();
        newRecipe.setName("Spaghetti Bolognese");
        newRecipe.setCategory(recipes.getFirst().getCategory());
        newRecipe.setDirections("Cook pasta. Prepare sauce. Combine and serve.");
        newRecipe.setIngredients(List.of(new IngredientEntity(1L, "Spaghetti")));
        newRecipe.setUserId(1);

        when(recipeService.createRecipe(any(RecipeEntity.class))).thenThrow(new DatabaseConnectionException("An error occurred while creating recipe: Connection refused", new Exception()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/recipes")
                .contentType("application/json")
                .accept("application/json")
                .content(new ObjectMapper().writeValueAsString(newRecipe));
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(500, result.getResponse().getStatus());
        assertEquals("An error occurred while creating recipe: Connection refused", response.getMessage());
    }

    @Test
    @DisplayName("Should return a recipe when a recipe is updated successfully")
    void testUpdateRecipe_whenRecipeIsUpdated_thenReturnRecipe() throws Exception {
        //Given
        RecipeEntity updatedRecipe = recipes.getFirst();
        updatedRecipe.setName("Spaghetti Bolognese2");

        when(recipeService.updateRecipe(any(RecipeEntity.class))).thenReturn(updatedRecipe);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/recipes")
                .contentType("application/json")
                .accept("application/json")
                .content(new ObjectMapper().writeValueAsString(updatedRecipe));
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        RecipeEntity response = objectMapper.readValue(responseBodyAsString, RecipeEntity.class);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(1L, response.getId());
        assertEquals("Spaghetti Bolognese2", response.getName());
        assertEquals("Italian", response.getCategory().getName());
        assertEquals("Cook pasta. Prepare sauce. Combine and serve.", response.getDirections());
        assertEquals("Spaghetti", response.getIngredients().getFirst().getValue());
        assertEquals(1, response.getUserId());
    }

    @Test
    @DisplayName("Should return error 400 when the recipe does not have an id")
    void testUpdateRecipe_whenRecipeDoesNotHaveId_thenReturnError404() throws Exception {
        //Given
        RecipeEntity updatedRecipe = recipes.getFirst();
        updatedRecipe.setId(null);

        when(recipeService.updateRecipe(any(RecipeEntity.class))).thenThrow(new IllegalArgumentException("Recipe must have an id to be updated"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/recipes")
                .contentType("application/json")
                .accept("application/json")
                .content(new ObjectMapper().writeValueAsString(updatedRecipe));
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Recipe must have an id to be updated", response.getMessage());
    }

    // Test the deleteRecipe method

    @Test
    @DisplayName("Should return true when a recipe is deleted successfully")
    void testDeleteRecipe_whenRecipeIsDeleted_thenReturnTrue() throws Exception {
        //Given
        when(recipeService.deleteRecipe(1L)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/recipes/1");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        boolean response = objectMapper.readValue(responseBodyAsString, Boolean.class);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertTrue(response);
    }

    @Test
    @DisplayName("Should return error 404 when the recipe does not exist")
    void testDeleteRecipe_whenRecipeDoesNotExist_thenReturnError404() throws Exception {
        //Given
        when(recipeService.deleteRecipe(12L)).thenThrow(new RecipeNotFoundException("An error occurred while deleting recipe: Recipe with id 12 not found"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/recipes/12");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(404, result.getResponse().getStatus());
        assertEquals("An error occurred while deleting recipe: Recipe with id 12 not found", response.getMessage());
    }

    @Test
    @DisplayName("Should return error 500 when there is an error deleting a recipe")
    void testDeleteRecipe_whenThereIsAnErrorDeletingRecipe_thenReturnError500() throws Exception {
        //Given
        when(recipeService.deleteRecipe(1L)).thenThrow(new DatabaseConnectionException("An error occurred while deleting recipe with id: 1 Connection refused", new Exception()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/recipes/1");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(500, result.getResponse().getStatus());
        assertEquals("An error occurred while deleting recipe with id: 1 Connection refused", response.getMessage());
    }
}