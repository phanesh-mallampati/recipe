package com.phani.recipe.service.impl;

import com.phani.recipe.commands.IngredientCommand;
import com.phani.recipe.convertors.IngredientCommandToIngredient;
import com.phani.recipe.convertors.IngredientToIngredientCommand;
import com.phani.recipe.models.Ingredient;
import com.phani.recipe.models.Recipe;
import com.phani.recipe.repositories.RecipeRepository;
import com.phani.recipe.repositories.UnitOfMeasureRepository;
import com.phani.recipe.service.IngredientService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

        private final IngredientToIngredientCommand ingredientToIngredientCommand;

        private final IngredientCommandToIngredient ingredientCommandToIngredient;

        private final RecipeRepository recipeRepository;

        private final UnitOfMeasureRepository unitOfMeasureRepository;

        public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                     IngredientCommandToIngredient ingredientCommandToIngredient,
                                     RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {

            this.ingredientToIngredientCommand = ingredientToIngredientCommand;
            this.ingredientCommandToIngredient = ingredientCommandToIngredient;
            this.recipeRepository = recipeRepository;
            this.unitOfMeasureRepository = unitOfMeasureRepository;
        }
        @Override
        public IngredientCommand findByReciepeIdAndIngredientId(Long recipeId, Long ingredientId) {
            Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
            if (!recipeOptional.isPresent()){
              throw new RuntimeException("recipe id not found. Id: " + recipeId);
            }
            Recipe recipe = recipeOptional.get();
            Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .map( ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

            if(!ingredientCommandOptional.isPresent()){
                throw new RuntimeException("Ingredient id not found: " + ingredientId);
            }
            return ingredientCommandOptional.get();
        }

        @Override
        @Transactional
        public IngredientCommand saveIngredient (IngredientCommand command) {
            Optional<Recipe> recipeOptional = recipeRepository.findById(command.getId());
            if(!recipeOptional.isPresent()){
                return new IngredientCommand();
            } else {
                Recipe recipe = recipeOptional.get();
                Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getId().equals(command.getId()))
                        .findFirst();
                if(ingredientOptional.isPresent()){
                    Ingredient ingredientFound = ingredientOptional.get();
                    ingredientFound.setDescription(command.getDescription());
                    ingredientFound.setAmount(command.getAmount());
                    ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                            .findById(command.getUnitOfMeasure().getId())
                            .orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
                } else {
                    Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                    ingredient.setRecipe(recipe);
                    recipe.addIngredients(ingredient);
                }
                Recipe savedRecipe = recipeRepository.save(recipe);
                Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                        .findFirst();
                if(!savedIngredientOptional.isPresent()){
                    savedIngredientOptional = savedRecipe.getIngredients().stream()
                            .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                            .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                            .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(
                                    command.getUnitOfMeasure().getId()))
                            .findFirst();
                }
                return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            }
        }

        @Override
        public void deleteById(Long recipeId, Long idToDelete) {
            Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
            if(recipeOptional.isPresent()){
                Recipe recipe = recipeOptional.get();
                Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getId().equals(idToDelete))
                        .findFirst();

                if(ingredientOptional.isPresent()){
                    Ingredient ingredientToDelete = ingredientOptional.get();
                    ingredientToDelete.setRecipe(null);
                    recipe.getIngredients().remove(ingredientOptional.get());
                    recipeRepository.save(recipe);
                }
            } else {
                //TODO: have to handle not found scenario
            }
        }
}