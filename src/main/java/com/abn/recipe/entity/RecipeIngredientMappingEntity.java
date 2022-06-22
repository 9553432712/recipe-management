package com.abn.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe_ingredient")
@IdClass(RecipeIngredientId.class)
public class RecipeIngredientMappingEntity implements Serializable {
    @Id
    @Column(name = "recipe_id")
    private Long recipeId;
    @Id
    @Column(name = "ingredient_id")
    private Long ingredientId;
    private String quantity;
}
