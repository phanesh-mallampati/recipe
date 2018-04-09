package com.phani.recipe.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.phani.recipe.commands.RecipeCommand;
import com.phani.recipe.service.IngredientService;
import com.phani.recipe.service.RecipeService;
import com.phani.recipe.service.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

public class IngredientControllerTest {

    IngredientController ingredientController;

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    Model model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientController = new IngredientController(ingredientService, recipeService, unitOfMeasureService );
    }

    @Test
    public void showIngredients() throws Exception{
        RecipeCommand recipeCommand = new RecipeCommand();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
        mockMvc.perform(get("/recipe/2/ingredients")).andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredients/list"));
        assertEquals("/recipe/ingredients/list", ingredientController.showIngredients("2", model));
        ArgumentCaptor<RecipeCommand> captor = ArgumentCaptor.forClass(RecipeCommand.class);
        Mockito.when(recipeService.getRecipeByID(2l)).thenReturn(recipeCommand);
        verify(recipeService, times(2)).getRecipeByID(2l);
        verify(model, times(1)).addAttribute(ArgumentMatchers.eq("recipe"), captor.capture());
    }
}