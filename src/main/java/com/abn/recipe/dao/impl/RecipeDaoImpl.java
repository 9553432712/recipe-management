package com.abn.recipe.dao.impl;

import com.abn.recipe.dao.RecipeDao;
import com.abn.recipe.entity.RecipeEntity;
import com.abn.recipe.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RecipeDaoImpl implements RecipeDao {

    RecipeRepository recipeRepository;

    @Override
    public List<RecipeEntity> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public RecipeEntity save(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }

    @Override
    public RecipeEntity findByName(String name) {
        return recipeRepository.findByName(name);
    }

    @Override
    public void delete(RecipeEntity recipeEntity) {
        recipeRepository.delete(recipeEntity);
    }
}
