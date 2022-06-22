package com.abn.recipe.repository;

import com.abn.recipe.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
    <T> Optional<T> findByName(String name);

    List<IngredientEntity> findAllByNameIn(List<String> collect);

    @Query(value = "select i.* from ingredient i, recipe_ingredient ri where ri.ingredient_id=i.id and ri.recipe_id=:recipeId", nativeQuery = true)
    List<IngredientEntity> findAllByRecipeId(@Param("recipeId") Long recipeId);
}
