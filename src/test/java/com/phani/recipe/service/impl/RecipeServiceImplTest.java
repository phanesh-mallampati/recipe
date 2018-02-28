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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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
        recipe.setId(1l);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);
        when(recipeRepository.findById(recipe.getId())).thenReturn(optionalRecipe);

       // optionalRecipe = recipeRepository.findById(2l);

        fail("not implemented");
    }

    @Test
    public void deleteRecipe(){
        recipeService.deleteRecipe(4L);
        verify(recipeRepository, times(1)).deleteById(4l);
    }
}