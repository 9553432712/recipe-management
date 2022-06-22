package com.abn.recipe.service.impl;

import com.abn.recipe.dao.IngredientDao;
import com.abn.recipe.dao.RecipeDao;
import com.abn.recipe.dto.IngredientDto;
import com.abn.recipe.dto.RecipeDto;
import com.abn.recipe.entity.IngredientEntity;
import com.abn.recipe.entity.RecipeEntity;
import com.abn.recipe.exception.DataNotFoundException;
import com.abn.recipe.service.RecipeService;
import com.abn.recipe.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    RecipeDao recipeDao;
    IngredientDao ingredientDao;

    @Override
    public List<RecipeDto> getAllRecipes() {
        List<RecipeEntity> recipeEntities = recipeDao.getAllRecipes();
        Map<Long, List<IngredientEntity>> ingredientEntities = ingredientDao.getAllIngredients(recipeEntities.stream().map(RecipeEntity::getId).collect(Collectors.toList()));
        return CommonUtil.getRecipies(recipeEntities, ingredientEntities);
    }

    @Override
    @Transactional
    public RecipeDto createRecipe(RecipeDto recipeDto) {
        RecipeEntity recipeEntityDB = recipeDao.findByName(recipeDto.getName());
        if (!ObjectUtils.allNull(recipeEntityDB)) {
            throw new DataNotFoundException(recipeDto.getName() + " already exist! use update function");
        }
        RecipeEntity recipeEntity = CommonUtil.getRecipeEntity(recipeDto);
        RecipeEntity recipeEntityResult = recipeDao.save(recipeEntity);
        List<IngredientEntity> ingredientEntities = CommonUtil.getIngredientEntities(recipeDto.getIngredientDtos());
        List<IngredientEntity> ingredientEntityResult = ingredientDao.save(ingredientEntities, recipeEntityResult.getId());
        return CommonUtil.getRecipeDto(recipeEntityResult, ingredientEntityResult);
    }

    @Override
    @Transactional
    public RecipeDto updateRecipe(RecipeDto recipeDto) {
        RecipeEntity recipeEntity = recipeDao.findByName(recipeDto.getName());
        if (!ObjectUtils.allNull(recipeEntity)) {
            RecipeEntity recipeEntityToBeSaved = CommonUtil.getRecipeEntity(recipeDto);
            recipeEntityToBeSaved.setId(recipeEntity.getId());
            RecipeEntity recipeEntityResult = recipeDao.save(recipeEntityToBeSaved);
            List<IngredientEntity> ingredientEntities = CommonUtil.getIngredientEntities(recipeDto.getIngredientDtos());
            List<IngredientEntity> ingredientEntityResult = ingredientDao.save(ingredientEntities, recipeEntityToBeSaved.getId());
            return CommonUtil.getRecipeDto(recipeEntityToBeSaved, ingredientEntityResult);
        }
        throw new DataNotFoundException(recipeDto.getName() + " not found");
    }

    @Override
    @Transactional
    public void deleteRecipe(String recipeName) {
        RecipeEntity recipeEntity = recipeDao.findByName(recipeName);
        if (!ObjectUtils.allNotNull(recipeEntity)) {
            throw new DataNotFoundException("Not found");
        }
        ingredientDao.deleteIngredients(recipeEntity);
        recipeDao.delete(recipeEntity);
    }

    @Override
    public String checkVegetarian(String recipeName) {
        RecipeEntity recipeEntity = recipeDao.findByName(recipeName);
        if (!ObjectUtils.allNotNull(recipeEntity)) {
            throw new DataNotFoundException(recipeName + " Not found");
        }
        if (recipeEntity.getIsVeg()) {
            return "Yes, " + recipeName + " is Veg";
        }
        return "No, " + recipeName + " is Non-Veg";
    }

    @Override
    public Long checkServings(String recipeName) {
        RecipeEntity recipeEntity = recipeDao.findByName(recipeName);
        if (!ObjectUtils.allNotNull(recipeEntity)) {
            throw new DataNotFoundException(recipeName + " Not found");
        }
        return recipeEntity.getServingCount();
    }

    @Override
    public List<IngredientDto> checkIngredients(String recipeName) {
        RecipeEntity recipeEntity = recipeDao.findByName(recipeName);
        if (!ObjectUtils.allNotNull(recipeEntity)) {
            throw new DataNotFoundException(recipeName + " Not found");
        }
        return CommonUtil.getIngredientDto(ingredientDao.getIngredientsByRecipeId(recipeEntity.getId()));
    }

    @Override
    public List<RecipeDto> searchRecipes(Boolean isVeg, int serveFor, String ingredient, String noIngredient) {
        List<RecipeDto> result = new ArrayList<>();
        List<RecipeEntity> recipeEntities = recipeDao.getAllRecipes();
        Map<Long, List<IngredientEntity>> ingredientEntities = ingredientDao.getAllIngredients(recipeEntities.stream().map(RecipeEntity::getId).collect(Collectors.toList()));
        List<RecipeDto> recipeDtoListBeforeSearch = CommonUtil.getRecipies(recipeEntities, ingredientEntities);
        if (serveFor > 0) {
            result = recipeDtoListBeforeSearch.stream().filter(a -> a.getSufficientFor() >= serveFor).collect(Collectors.toList());
        } else {
            result = recipeDtoListBeforeSearch;
        }
        if (isVeg) {
            result = recipeDtoListBeforeSearch.stream().filter(a -> a.getIsVeg()).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(ingredient)) {
            result = result.stream().filter(a -> a.getIngredientDtos().stream().map(IngredientDto::getName).collect(Collectors.toList()).contains(ingredient)).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(noIngredient)) {
            result = result.stream().filter(a -> !a.getIngredientDtos().stream().map(IngredientDto::getName).collect(Collectors.toList()).contains(noIngredient)).collect(Collectors.toList());
        }
        return result;
    }
}
