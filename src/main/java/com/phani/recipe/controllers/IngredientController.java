package com.phani.recipe.controllers;

import com.phani.recipe.commands.IngredientCommand;
import com.phani.recipe.commands.RecipeCommand;
import com.phani.recipe.commands.UnitOfMeasureCommand;
import com.phani.recipe.service.IngredientService;
import com.phani.recipe.service.RecipeService;
import com.phani.recipe.service.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IngredientController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService,
                                UnitOfMeasureService unitOfMeasureService) {

        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String showIngredients(@PathVariable String recipeId, Model model){
        model.addAttribute("recipe", recipeService.getRecipeByID(new Long(recipeId)));
        return "/recipe/ingredients/list";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,@PathVariable String id, Model model){
        model.addAttribute("ingredient", ingredientService.findByReciepeIdAndIngredientId(Long.valueOf(recipeId),
                Long.valueOf(id)));
        return "recipe/ingredients/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String newRecipe(@PathVariable String recipeId, Model model){
        RecipeCommand recipeCommand = recipeService.getRecipeByID(Long.valueOf(recipeId));
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        model.addAttribute("ingredient", ingredientCommand);
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("uomList",  unitOfMeasureService.listAllUoms());
        return "recipe/ingredients/ingredientform";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,@PathVariable String id, Model model){
        model.addAttribute("ingredient", ingredientService.findByReciepeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredients/ingredientform";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredient(command);
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredients/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId,@PathVariable String id){
        ingredientService.deleteById(Long.valueOf(recipeId), Long.valueOf(id));
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}