package com.abn.recipe.service;

import com.abn.recipe.dto.IngredientDto;
import com.abn.recipe.dto.RecipeDto;

import java.util.List;

public interface RecipeService {
    List<RecipeDto> getAllRecipes();

    RecipeDto createRecipe(RecipeDto recipeDto);

    RecipeDto updateRecipe(RecipeDto recipeDto);

    void deleteRecipe(String recipeName);

    String checkVegetarian(String recipeName);

    Long checkServings(String recipeName);

    List<IngredientDto> checkIngredients(String recipeName);

    List<RecipeDto> searchRecipes(Boolean isVeg, int serveFor, String ingredient, String noIngredient);
}
