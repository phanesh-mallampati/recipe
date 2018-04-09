package com.phani.recipe.service.impl;

import com.phani.recipe.commands.RecipeCommand;
import com.phani.recipe.convertors.RecipeCommandToRecipe;
import com.phani.recipe.convertors.RecipeToRecipeCommand;
import com.phani.recipe.models.Recipe;
import com.phani.recipe.repositories.RecipeRepository;
import com.phani.recipe.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeToRecipeCommand, recipeCommandToRecipe);
    }

    @Test
    public void getRecipes() {
        Set<RecipeCommand> recipes = recipeService.getRecipes();
        assertEquals(0, recipes.size());
        verify(recipeRepository, times(1)).findById(2l);
    }

    @Test
    public void getRecipeByID() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);

        RecipeCommand recipeCmd = recipeService.getRecipeByID(1L);

        assertNotNull("Null recipe returned", recipeCmd);

        verify(recipeRepository, times(1)).findById(anyLong());

        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void deleteRecipe(){
        recipeService.deleteRecipe(4L);
        verify(recipeRepository, times(1)).deleteById(4l);
    }
}