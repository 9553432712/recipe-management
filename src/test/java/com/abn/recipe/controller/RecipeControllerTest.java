package com.abn.recipe.controller;

import com.abn.recipe.dto.IngredientDto;
import com.abn.recipe.dto.RecipeDto;
import com.abn.recipe.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
class RecipeControllerTest {

    MockMvc mockMvc;

    @Mock
    RecipeServiceImpl recipeService;

    @InjectMocks
    RecipeController recipeController;

    RecipeDto recipeDto;
    IngredientDto ingredientDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        recipeDto = new RecipeDto();
        recipeDto.setSufficientFor(4);
        recipeDto.setName("Potato Fry");
        recipeDto.setIsVeg(true);
        recipeDto.setServingCount(10L);
        ingredientDto = new IngredientDto();
        ingredientDto.setName("potato");
        recipeDto.setIngredientDtos(Arrays.asList(ingredientDto));
    }

    @Test
    void getAllRecipes() throws Exception {
        Mockito.when(recipeService.getAllRecipes()).thenReturn(Arrays.asList(recipeDto));
        this.mockMvc.perform(get("/recipe/get"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].name").value("Potato Fry"))
                .andExpect(jsonPath("$[0].isVeg").value(true))
                .andExpect(jsonPath("$[0].sufficientFor").value(4));
    }

    @Test
    void checkDishVegetarian() throws Exception {
        Mockito.when(recipeService.checkVegetarian(anyString())).thenReturn("Yes, Potato Fry is veg");
        MvcResult mvcResult = this.mockMvc.perform(get("/recipe/checkVeg/recipeName", "Potato Fry"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Assertions.assertEquals("Yes, Potato Fry is veg", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void checkServings() throws Exception {
        Mockito.when(recipeService.checkServings(anyString())).thenReturn(4L);
        MvcResult mvcResult = this.mockMvc.perform(get("/recipe/checkServing/recipeName", "Potato Fry"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Assertions.assertEquals("4", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void searchRecipe() throws Exception {
        Mockito.when(recipeService.searchRecipes(anyBoolean(), anyInt(), anyString(), anyString())).thenReturn(Arrays.asList(recipeDto));
        this.mockMvc.perform(get("/recipe/search")
                        .queryParam("veg", "false")
                        .queryParam("serve", "1")
                        .queryParam("ingredient", "potato")
                        .queryParam("notIngredient", "chicken"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].name").value("Potato Fry"))
                .andExpect(jsonPath("$[0].isVeg").value(true))
                .andExpect(jsonPath("$[0].sufficientFor").value(4))
                .andReturn();
    }

    @Test
    void checkIngredients() throws Exception {
        Mockito.when(recipeService.checkIngredients(anyString())).thenReturn(Arrays.asList(ingredientDto));
        this.mockMvc.perform(get("/recipe/checkIngredients/recipeName", "Potato Fry"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].name").value("potato"))
                .andReturn();
    }

    @Test
    void createRecipe() throws Exception {
        Mockito.when(recipeService.createRecipe(any(RecipeDto.class))).thenReturn(recipeDto);
        this.mockMvc.perform(post("/recipe/create").contentType(MediaType.APPLICATION_JSON).content(fromFile("src/test/resources/recipe.json")))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name").value("Potato Fry"))
                .andExpect(jsonPath("$.isVeg").value(true))
                .andExpect(jsonPath("$.sufficientFor").value(4))
                .andReturn();
    }

    public byte[] fromFile(final String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    @Test
    void updateRecipe() throws Exception {
        recipeDto.setSufficientFor(5);
        Mockito.when(recipeService.updateRecipe(any(RecipeDto.class))).thenReturn(recipeDto);
        this.mockMvc.perform(put("/recipe/update").contentType(MediaType.APPLICATION_JSON).content(fromFile("src/test/resources/recipe.json")))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name").value("Potato Fry"))
                .andExpect(jsonPath("$.isVeg").value(true))
                .andExpect(jsonPath("$.sufficientFor").value(5))
                .andReturn();
    }

    @Test
    void deleteRecipe() throws Exception {
        doNothing().when(recipeService).deleteRecipe(anyString());
        this.mockMvc.perform(delete("/recipe/delete/recipeName", "Potato Fry"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}