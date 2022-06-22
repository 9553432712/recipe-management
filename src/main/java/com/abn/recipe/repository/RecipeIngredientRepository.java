package com.abn.recipe.repository;

import com.abn.recipe.entity.RecipeIngredientMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredientMappingEntity, Long> {
    @Modifying
    @Query(value = "delete from recipe_ingredient where recipe_Id= :recipeId", nativeQuery = true)
    int deleteByRecipeId(@Param("recipeId") Long recipeId);
}
