package com.abn.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientId implements Serializable {
    private Long recipeId;
    private Long ingredientId;
}