package com.abn.recipe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class RecipeDto {
    private String name;

    private Boolean isVeg;

    private int sufficientFor;

    private Long servingCount;

    private List<IngredientDto> ingredientDtos;
}
