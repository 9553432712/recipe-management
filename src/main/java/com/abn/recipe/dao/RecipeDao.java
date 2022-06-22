package com.abn.recipe.dao;

import com.abn.recipe.entity.RecipeEntity;

import java.util.List;

public interface RecipeDao {
    List<RecipeEntity> getAllRecipes();

    RecipeEntity save(RecipeEntity recipeEntity);

    RecipeEntity findByName(String name);

    void delete(RecipeEntity recipeEntity);
}
