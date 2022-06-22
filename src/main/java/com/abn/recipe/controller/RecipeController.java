package com.abn.recipe.controller;

import com.abn.recipe.dto.IngredientDto;
import com.abn.recipe.dto.RecipeDto;
import com.abn.recipe.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
@io.swagger.annotations.Api(value = "Recipe Management APIs")
public class RecipeController {

    RecipeService recipeService;

    @GetMapping("/get")
    public List<RecipeDto> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/checkVeg/{recipeName}")
    public String checkDishVegetarian(@PathVariable("recipeName") String recipeName) {
        return recipeService.checkVegetarian(recipeName);
    }

    @GetMapping("/checkServing/{recipeName}")
    public Long checkServings(@PathVariable("recipeName") String recipeName) {
        return recipeService.checkServings(recipeName);
    }

    @GetMapping("/search")
    public List<RecipeDto> searchRecipe(@RequestParam(value = "veg", required = false, defaultValue = "false") Boolean isVeg,
                                        @RequestParam(value = "serve", required = false, defaultValue = "0") int serveFor,
                                        @RequestParam(value = "ingredient", required = false) String ingredient,
                                        @RequestParam(value = "notIngredient", required = false) String noIngredient) {
        return recipeService.searchRecipes(isVeg, serveFor, ingredient, noIngredient);
    }

    @GetMapping("/checkIngredients/{recipeName}")
    public List<IngredientDto> checkIngredients(@PathVariable("recipeName") String recipeName) {
        return recipeService.checkIngredients(recipeName);
    }

    @PostMapping("/create")
    public RecipeDto createRecipe(@RequestBody RecipeDto recipeDto) {
        return recipeService.createRecipe(recipeDto);
    }

    @PutMapping("/update")
    public RecipeDto updateRecipe(@RequestBody RecipeDto recipeDto) {
        return recipeService.updateRecipe(recipeDto);
    }

    @DeleteMapping("/delete/{recipeName}")
    public void deleteRecipe(@PathVariable("recipeName") String recipeName) {
        recipeService.deleteRecipe(recipeName);
    }
}
