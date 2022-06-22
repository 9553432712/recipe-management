package com.abn.recipe.dao;

import com.abn.recipe.entity.IngredientEntity;
import com.abn.recipe.entity.RecipeEntity;

import java.util.List;
import java.util.Map;

public interface IngredientDao {
    List<IngredientEntity> getIngredientsByRecipeId(Long aLong);

    Map<Long, List<IngredientEntity>> getAllIngredients(List<Long> recipeEntities);

    List<IngredientEntity> save(List<IngredientEntity> ingredientEntities, Long recipeId);

    void deleteIngredients(RecipeEntity recipeEntity);
}
