package com.abn.recipe.dao.impl;

import com.abn.recipe.dao.IngredientDao;
import com.abn.recipe.entity.IngredientEntity;
import com.abn.recipe.entity.RecipeEntity;
import com.abn.recipe.entity.RecipeIngredientMappingEntity;
import com.abn.recipe.repository.IngredientRepository;
import com.abn.recipe.repository.RecipeIngredientRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class IngredientDaoImpl implements IngredientDao {
    IngredientRepository ingredientRepository;
    RecipeIngredientRepository recipeIngredientRepository;

    @Override
    public List<IngredientEntity> getIngredientsByRecipeId(Long recipeId) {
        return ingredientRepository.findAllByRecipeId(recipeId);
    }

    @Override
    public Map<Long, List<IngredientEntity>> getAllIngredients(List<Long> recipeEntities) {
        Map<Long, List<IngredientEntity>> recipeIngredientMap = new HashMap<>();
        if (ObjectUtils.isNotEmpty(recipeEntities)) {
            for (Long recipeId : recipeEntities) {
                recipeIngredientMap.put(recipeId, ingredientRepository.findAllByRecipeId(recipeId));
            }
        }
        return recipeIngredientMap;
    }

    @Override
    @Transactional
    public List<IngredientEntity> save(List<IngredientEntity> ingredientEntities, Long recipeId) {
        List<IngredientEntity> result = null;
        if (ObjectUtils.allNotNull(ingredientEntities)) {
            for (IngredientEntity ingredientEntity : ingredientEntities) {
                ingredientRepository.findByName(ingredientEntity.getName())
                        .orElseGet(() -> ingredientRepository.save(ingredientEntity));
            }
        }
        result = ingredientRepository.findAllByNameIn(ingredientEntities.stream().map(IngredientEntity::getName).collect(Collectors.toList()));

        List<RecipeIngredientMappingEntity> recipeIngredientMappingEntities = result.stream().map(a -> new RecipeIngredientMappingEntity(recipeId, a.getId(), "1")).collect(Collectors.toList());
        recipeIngredientRepository.deleteByRecipeId(recipeId);
        List<RecipeIngredientMappingEntity> recipeIngredientMappingEntityResult = recipeIngredientRepository.saveAll(recipeIngredientMappingEntities);

        return ingredientRepository.findAllById(recipeIngredientMappingEntityResult.stream().map(a -> a.getIngredientId()).collect(Collectors.toList()));
    }

    @Override
    public void deleteIngredients(RecipeEntity recipeEntity) {
        recipeIngredientRepository.deleteByRecipeId(recipeEntity.getId());
    }
}
