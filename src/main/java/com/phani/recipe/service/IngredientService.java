package com.phani.recipe.service;

import com.phani.recipe.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByReciepeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredient(IngredientCommand ingredientCommand);

    void deleteById(Long recipeId, Long idToDelete);
}
