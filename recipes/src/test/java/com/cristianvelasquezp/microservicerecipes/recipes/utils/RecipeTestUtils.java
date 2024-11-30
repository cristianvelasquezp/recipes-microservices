package com.cristianvelasquezp.microservicerecipes.recipes.utils;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.CategoryEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.Entities.IngredientEntity;
import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecipeTestUtils {
    private final List<RecipeEntity> recipeEntities = new ArrayList<>();

    public List<RecipeEntity> createRecipeEntities() {

        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setId(1L);
        recipe1.setName("Spaghetti Bolognese");
        recipe1.setDescription("Classic Italian pasta dish with a rich meat sauce.");
        recipe1.setCategory(new CategoryEntity(1L, "Italian"));
        recipe1.setDate(new Timestamp(System.currentTimeMillis()));
        recipe1.setDirections("Cook pasta. Prepare sauce. Combine and serve.");
        recipe1.setIngredients(List.of(new IngredientEntity(1L, "Spaghetti"), new IngredientEntity(2L, "Ground Beef"), new IngredientEntity(3L, "Tomato Sauce")));
        recipe1.setUserId(1);
        recipeEntities.add(recipe1);

        RecipeEntity recipe2 = new RecipeEntity();
        recipe2.setId(2L);
        recipe2.setName("Chicken Curry");
        recipe2.setDescription("Spicy and flavorful chicken curry.");
        recipe2.setCategory(new CategoryEntity(2L, "Indian"));
        recipe2.setDate(new Timestamp(System.currentTimeMillis()));
        recipe2.setDirections("Cook chicken. Prepare curry sauce. Combine and serve.");
        recipe2.setIngredients(List.of(new IngredientEntity(4L, "Chicken"), new IngredientEntity(5L, "Curry Powder"), new IngredientEntity(6L, "Coconut Milk")));
        recipe2.setUserId(2);
        recipeEntities.add(recipe2);

        RecipeEntity recipe3 = new RecipeEntity();
        recipe3.setId(3L);
        recipe3.setName("Caesar Salad");
        recipe3.setDescription("Fresh salad with romaine lettuce, croutons, and Caesar dressing.");
        recipe3.setCategory(new CategoryEntity(3L, "Salad"));
        recipe3.setDate(new Timestamp(System.currentTimeMillis()));
        recipe3.setDirections("Chop lettuce. Prepare dressing. Toss and serve.");
        recipe3.setIngredients(List.of(new IngredientEntity(7L, "Romaine Lettuce"), new IngredientEntity(8L, "Croutons"), new IngredientEntity(9L, "Caesar Dressing")));
        recipe3.setUserId(3);
        recipeEntities.add(recipe3);

        RecipeEntity recipe4 = new RecipeEntity();
        recipe4.setId(4L);
        recipe4.setName("Beef Stroganoff");
        recipe4.setDescription("Russian dish with sautéed pieces of beef served in a sauce with smetana.");
        recipe4.setCategory(new CategoryEntity(4L, "Russian"));
        recipe4.setDate(new Timestamp(System.currentTimeMillis()));
        recipe4.setDirections("Cook beef. Prepare sauce. Combine and serve.");
        recipe4.setIngredients(List.of(new IngredientEntity(10L, "Beef"), new IngredientEntity(11L, "Sour Cream"), new IngredientEntity(12L, "Mushrooms")));
        recipe4.setUserId(4);
        recipeEntities.add(recipe4);

        RecipeEntity recipe5 = new RecipeEntity();
        recipe5.setId(5L);
        recipe5.setName("Tacos");
        recipe5.setDescription("Traditional Mexican dish consisting of a small hand-sized corn or wheat tortilla topped with a filling.");
        recipe5.setCategory(new CategoryEntity(5L, "Mexican"));
        recipe5.setDate(new Timestamp(System.currentTimeMillis()));
        recipe5.setDirections("Prepare filling. Warm tortillas. Assemble and serve.");
        recipe5.setIngredients(List.of(new IngredientEntity(13L, "Tortillas"), new IngredientEntity(14L, "Beef"), new IngredientEntity(15L, "Cheese")));
        recipe5.setUserId(5);
        recipeEntities.add(recipe5);

        RecipeEntity recipe6 = new RecipeEntity();
        recipe6.setId(6L);
        recipe6.setName("Sushi");
        recipe6.setDescription("Japanese dish consisting of prepared vinegared rice, usually with some sugar and salt, accompanying a variety of ingredients.");
        recipe6.setCategory(new CategoryEntity(6L, "Japanese"));
        recipe6.setDate(new Timestamp(System.currentTimeMillis()));
        recipe6.setDirections("Prepare rice. Prepare fillings. Roll and serve.");
        recipe6.setIngredients(List.of(new IngredientEntity(16L, "Rice"), new IngredientEntity(17L, "Fish"), new IngredientEntity(18L, "Seaweed")));
        recipe6.setUserId(6);
        recipeEntities.add(recipe6);

        RecipeEntity recipe7 = new RecipeEntity();
        recipe7.setId(7L);
        recipe7.setName("Pad Thai");
        recipe7.setDescription("Stir-fried rice noodle dish commonly served as street food in Thailand.");
        recipe7.setCategory(new CategoryEntity(7L, "Thai"));
        recipe7.setDate(new Timestamp(System.currentTimeMillis()));
        recipe7.setDirections("Cook noodles. Prepare sauce. Stir-fry and serve.");
        recipe7.setIngredients(List.of(new IngredientEntity(19L, "Rice Noodles"), new IngredientEntity(20L, "Shrimp"), new IngredientEntity(21L, "Peanuts")));
        recipe7.setUserId(7);
        recipeEntities.add(recipe7);

        RecipeEntity recipe8 = new RecipeEntity();
        recipe8.setId(8L);
        recipe8.setName("Pancakes");
        recipe8.setDescription("Flat cake, often thin and round, prepared from a starch-based batter that may contain eggs, milk and butter.");
        recipe8.setCategory(new CategoryEntity(8L, "Breakfast"));
        recipe8.setDate(new Timestamp(System.currentTimeMillis()));
        recipe8.setDirections("Prepare batter. Cook on griddle. Serve with syrup.");
        recipe8.setIngredients(List.of(new IngredientEntity(22L, "Flour"), new IngredientEntity(23L, "Eggs"), new IngredientEntity(24L, "Milk")));
        recipe8.setUserId(8);
        recipeEntities.add(recipe8);

        RecipeEntity recipe9 = new RecipeEntity();
        recipe9.setId(9L);
        recipe9.setName("Hamburger");
        recipe9.setDescription("Sandwich consisting of one or more cooked patties of ground meat, usually beef, placed inside a sliced bread roll or bun.");
        recipe9.setCategory(new CategoryEntity(9L, "American"));
        recipe9.setDate(new Timestamp(System.currentTimeMillis()));
        recipe9.setDirections("Prepare patties. Cook patties. Assemble and serve.");
        recipe9.setIngredients(List.of(new IngredientEntity(25L, "Beef Patties"), new IngredientEntity(26L, "Buns"), new IngredientEntity(27L, "Lettuce")));
        recipe9.setUserId(9);
        recipeEntities.add(recipe9);

        RecipeEntity recipe10 = new RecipeEntity();
        recipe10.setId(10L);
        recipe10.setName("Chocolate Cake");
        recipe10.setDescription("Cake flavored with melted chocolate, cocoa powder, or both.");
        recipe10.setCategory(new CategoryEntity(10L, "Dessert"));
        recipe10.setDate(new Timestamp(System.currentTimeMillis()));
        recipe10.setDirections("Prepare batter. Bake. Frost and serve.");
        recipe10.setIngredients(List.of(new IngredientEntity(28L, "Flour"), new IngredientEntity(29L, "Cocoa Powder"), new IngredientEntity(30L, "Sugar")));
        recipe10.setUserId(10);
        recipeEntities.add(recipe10);

        return recipeEntities;
    }

    public RecipeEntity addRecipeEntities(RecipeEntity recipe) {
        Long id = recipeEntities.size() + 1L;
        recipe.setId(id);
        recipeEntities.add(recipe);
        return recipeEntities.getLast();
    }
}
