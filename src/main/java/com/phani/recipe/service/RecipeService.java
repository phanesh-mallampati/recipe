package com.phani.recipe.service;

import com.phani.recipe.commands.RecipeCommand;
import com.phani.recipe.models.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<RecipeCommand> getRecipes();

    RecipeCommand getRecipeByID(Long id);

    RecipeCommand addNewRecipe(RecipeCommand newRecipeCommand);

    void deleteRecipe(Long id);
}
