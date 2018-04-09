package com.phani.recipe.service.impl;

import com.phani.recipe.commands.RecipeCommand;
import com.phani.recipe.convertors.RecipeCommandToRecipe;
import com.phani.recipe.convertors.RecipeToRecipeCommand;
import com.phani.recipe.models.Recipe;
import com.phani.recipe.repositories.RecipeRepository;
import com.phani.recipe.service.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand,
                             RecipeCommandToRecipe recipeCommandToRecipe) {
        this.recipeRepository = recipeRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    @Override
    @Transactional
    public Set<RecipeCommand> getRecipes() {
        Set<RecipeCommand> recipes = new HashSet();
        Iterator<Recipe> itr = recipeRepository.findAll().iterator();
        while(itr.hasNext()){
            Recipe r = itr.next();
            recipes.add(recipeToRecipeCommand.convert(r));
        }
        return recipes;
    }

    @Override
    public RecipeCommand getRecipeByID(Long id) {
        Optional<Recipe> opt = recipeRepository.findById(id);
        if(!opt.isPresent())
            throw new RuntimeException("Recipe not found!!!");
        else{
            return recipeToRecipeCommand.convert(opt.get());
        }
    }

    @Override
    @Transactional
    public RecipeCommand addNewRecipe(RecipeCommand newRecipeCommand) {
        Recipe newRecipe = recipeCommandToRecipe.convert(newRecipeCommand);
        Recipe insertedRecipe = recipeRepository.save(newRecipe);
        return recipeToRecipeCommand.convert(insertedRecipe);
    }

    @Override
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
        return;
    }
}
