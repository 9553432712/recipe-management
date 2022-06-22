package com.abn.recipe.util;

import com.abn.recipe.dto.IngredientDto;
import com.abn.recipe.dto.RecipeDto;
import com.abn.recipe.entity.IngredientEntity;
import com.abn.recipe.entity.RecipeEntity;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonUtil {

    public static RecipeDto getRecipeDto(RecipeEntity recipeEntityResult, List<IngredientEntity> ingredientEntities) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setIsVeg(recipeEntityResult.getIsVeg());
        recipeDto.setName(recipeEntityResult.getName());
        recipeDto.setServingCount(recipeEntityResult.getServingCount());
        recipeDto.setSufficientFor(recipeEntityResult.getSufficientFor());
        recipeDto.setIngredientDtos(ingredientEntities.stream().map(a -> new IngredientDto(a.getName())).collect(Collectors.toList()));
        return recipeDto;
    }

    public static IngredientDto getIngredientDto(IngredientEntity ingredientEntity) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName(ingredientEntity.getName());
        return ingredientDto;
    }

    public static RecipeEntity getRecipeEntity(RecipeDto recipeDto) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setIsVeg(recipeDto.getIsVeg());
        recipeEntity.setName(recipeDto.getName());
        recipeEntity.setServingCount(recipeDto.getServingCount());
        recipeEntity.setSufficientFor(recipeDto.getSufficientFor());
        return recipeEntity;
    }

    public static List<IngredientEntity> getIngredientEntities(List<IngredientDto> ingredientDtos) {
        List<IngredientEntity> result = new ArrayList<>();
        if (ObjectUtils.allNotNull(ingredientDtos)) {
            for (IngredientDto ingredientDto : ingredientDtos) {
                IngredientEntity ingredientEntity = new IngredientEntity();
                ingredientEntity.setName(ingredientDto.getName());
                result.add(ingredientEntity);
            }
        }
        return result;
    }

    public static List<RecipeDto> getRecipies(List<RecipeEntity> recipeEntities, Map<Long, List<IngredientEntity>> ingredientEntities) {
        List<RecipeDto> result = new ArrayList<>();
        if (ObjectUtils.allNotNull(ingredientEntities, recipeEntities)) {
            return recipeEntities.stream().map(a -> getRecipeDto(a, ingredientEntities.get(a.getId()))).collect(Collectors.toList());
        }
        return result;
    }

    public static List<IngredientDto> getIngredientDto(List<IngredientEntity> ingredientEntities) {
        if (ObjectUtils.isNotEmpty(ingredientEntities)) {
            return ingredientEntities.stream().map(CommonUtil::getIngredientDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
