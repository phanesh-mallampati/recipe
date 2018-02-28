package com.phani.recipe.controllers;

import com.phani.recipe.commands.RecipeCommand;
import com.phani.recipe.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RecipeController {

    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe/{id}/show")
    public String showRecipeById(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.getRecipeByID(new Long(id)));
        return "/recipe/show";
    }

    @RequestMapping("/recipe/new")
    public String addRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return "/recipe/recipeForm";
    }

    @PostMapping
    @RequestMapping("recipe")
    public String saveOrUpdateRecipe(@ModelAttribute RecipeCommand newRecipeCommand, Model model){
        RecipeCommand savedRecipeCommand = recipeService.addNewRecipe(newRecipeCommand);
        model.addAttribute("recipe", savedRecipeCommand);
        return "redirect:/recipe/"+savedRecipeCommand.getId()+"/show";
    }

    @RequestMapping("/recipe/{id}/update")
    public String editRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.getRecipeByID(new Long(id)));
        return "recipe/recipeForm";
    }

    @RequestMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id, Model model){
        recipeService.deleteRecipe(new Long(id));
        return "redirect:/";
    }
}
