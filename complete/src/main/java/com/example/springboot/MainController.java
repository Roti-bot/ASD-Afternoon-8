package com.example.springboot;
import com.example.springboot.Recipe;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Controller // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private RecipeRepository recipeRepository;

    @GetMapping("/recipes")
    public List<Recipe> getRecipes() {
        System.out.println("komme da rein List");
        List<Recipe> recipe_list = (List<Recipe>) recipeRepository.findAll();
       if(recipe_list.isEmpty())
           System.out.println("Ist leer");

       return recipe_list;

    }

    @GetMapping("/recipes/edit/{id}")
    public Recipe getRecipe(@PathVariable int id) {
        System.out.println("komme da rein List huhu");
        return recipeRepository.findById(id).orElse(null);

    }

    @PutMapping("/recipes/edit/{id}")
    void updateRecipe(@PathVariable int id, @RequestBody Recipe recipe ) {
        System.out.println("komme da rein update");
        Recipe recipe_ = recipeRepository.findById(id).get();
        recipe_.setTitle(recipe.getTitle());
        recipe_.setDescription(recipe.getDescription());
        recipe_.setType(recipe.getType());
        recipe_.setPreparationtime(recipe.getPreparationtime());
        recipe_.setCookingtimeime(recipe.getCookingtime());
        recipe_.setContent(recipe.getContent());
        recipe_.setDifficulty(recipe.getDifficulty());

        System.out.println("komme da rein update");
        System.out.println("komme da rein update " + recipe.getTitle());
        System.out.println("komme da rein update" + recipe.getType());
        System.out.println("komme da rein update" + recipe.getDescription());


        System.out.println("komme da rein Add");
        recipeRepository.save(recipe_);
    }
    @PutMapping("/recipes/updateTitle/{id}")
    void updateTitle(@PathVariable int id, @RequestBody String huhu ) {
        System.out.println("komme da rein update");
        Recipe recipe_ = recipeRepository.findById(id).get();
        recipe_.setTitle(huhu);
        System.out.println("komme da rein updateTitle");
        recipeRepository.save(recipe_);
    }

    @PutMapping("/recipes/favorize/{id}")
        void favorize(@PathVariable int id, @RequestBody String title) {
        Recipe recipe_ = recipeRepository.findById(id).get();
        System.out.println("komme da rein update" + id + recipe_.getFavorite());
        if(recipe_.getFavorite() == true)
            recipe_.setFavorite(false);
        else if(recipe_.getFavorite() == false)
            recipe_.setFavorite(true);
        recipe_.setTitle(title);
        System.out.println("komme da rein updateFavorite" + recipe_.getFavorite());
        recipeRepository.save(recipe_);
    }

    @GetMapping("/recipes/filter/{term}/{kind}")
    public List<Recipe>  filter(@PathVariable String term, @PathVariable String kind){
        System.out.println("komme da rein filter");
        List<Recipe> recipe_list_filter =  new ArrayList<Recipe>();;
        List<Recipe> recipe_list =  new ArrayList<Recipe>();;
        recipe_list = (List<Recipe>) recipeRepository.findAll();
        recipe_list.sort((object1, object2) -> object1.getTitle().compareTo(object2.getTitle()));

        if (term.equals("A-Z") && kind.equals("title")) {
            recipe_list.sort((object1, object2) -> object1.getTitle().compareTo(object2.getTitle()));
            return recipe_list;
        }
        else if (term.equals("Z-A") && kind.equals("title")) {
            recipe_list.sort((object1, object2) -> object2.getTitle().compareTo(object1.getTitle()));
            return recipe_list;
        }
        else if (term.equals("A-Z") && kind.equals("type")) {
            recipe_list.sort((object1, object2) -> object1.getType().compareTo(object2.getType()));
            return recipe_list;
        }
        else if (term.equals("Z-A") && kind.equals("type")) {
            recipe_list.sort((object1, object2) -> object2.getType().compareTo(object1.getType()));
            return recipe_list;
        }
        else if (term.equals("ascending order") && kind.equals("preptime")) {
            recipe_list.sort((object1, object2) -> object1.getPreparationtime().compareTo(object2.getPreparationtime()));
            return recipe_list;
        }
        else if (term.equals("descending order") && kind.equals("preptime")) {
            recipe_list.sort((object1, object2) -> object2.getPreparationtime().compareTo(object1.getPreparationtime()));
            return recipe_list;
        }
        else if (term.equals("ascending order") && kind.equals("cooktime")) {
            recipe_list.sort((object1, object2) -> object1.getCookingtime().compareTo(object2.getCookingtime()));
            return recipe_list;
        }
        else if (term.equals("descending order") && kind.equals("cooktime")) {
            recipe_list.sort((object1, object2) -> object2.getCookingtime().compareTo(object1.getCookingtime()));
            return recipe_list;
        }

        if (!term.isEmpty() && kind.equals("type")) {
            recipe_list_filter.addAll(recipeRepository.findByType(term));
            recipe_list_filter.sort((object1, object2) -> object1.getTitle().compareTo(object2.getTitle()));
            return recipe_list_filter;
        }
        if (NumberUtils.isCreatable(term) && kind.equals("preptime")) {
            int prep_time = Integer.parseInt(term);
            recipe_list_filter.addAll(recipeRepository.findByPreparationtime(prep_time));
            recipe_list_filter.sort((object1, object2) -> object1.getTitle().compareTo(object2.getTitle()));
            return recipe_list_filter;
        }
        if (NumberUtils.isCreatable(term) && kind.equals("cooktime")) {
            int cook_time = Integer.parseInt(term);
            recipe_list_filter.addAll(recipeRepository.findByCookingtime(cook_time));
            recipe_list_filter.sort((object1, object2) -> object1.getTitle().compareTo(object2.getTitle()));
            return recipe_list_filter;
        }

        return recipe_list;
    }

    @GetMapping("/recipes/favorites")
    List<Recipe> favorites() {

        List<Recipe> favorites = recipeRepository.findAllByFavorite(true);

        return favorites;
    }

   @GetMapping("/recipes/search/{title}")
    public List<Recipe>  Search_2(@PathVariable String title ) {
        System.out.println("komme da rein search");
        List<Recipe> recipe_list =  new ArrayList<Recipe>();;
        //int i = Integer.parseInt(title);
           if(recipeRepository.existsRecipeByTitle(title))
           {
           System.out.println("gefunden" + title);

           recipe_list.addAll(recipeRepository.findByTitle(title)) ;
       }
       if(recipeRepository.existsRecipeByType(title))
       {
           System.out.println("gefunden 2" + title);

           recipe_list.addAll(recipeRepository.findByType(title)) ;
       }

       if(NumberUtils.isCreatable(title)) {
           int prep_cooktime = Integer.parseInt(title);

           if (recipeRepository.existsRecipeByPreparationtime(prep_cooktime)) {
               System.out.println("gefunden 2" + title);

               recipe_list.addAll(recipeRepository.findByPreparationtime(prep_cooktime));
           }
           if (recipeRepository.existsRecipeByCookingtime(prep_cooktime)) {
               System.out.println("gefunden 2" + prep_cooktime);

               recipe_list.addAll(recipeRepository.findByCookingtime(prep_cooktime));
           }
       }


        if(recipe_list.isEmpty())
            System.out.println("Ist leer");

        return recipe_list;

    }
    @GetMapping("/recipes/favorites")
    public List<Recipe>  listFavorites() {
        System.out.println("komme da rein List");
        List<Recipe> recipe_list = (List<Recipe>) recipeRepository.findByFavorite(true);
        if(recipe_list.isEmpty())
            System.out.println("Ist leer");

        return recipe_list;

    }

    @PostMapping("/recipes")
    void addUser(@RequestBody Recipe recipe) {
        System.out.println("komme da rein Add");
        recipeRepository.save(recipe);
    }

    @DeleteMapping("/recipes/delete/{id}")
    public void deleteRecipe(@PathVariable int id){
        System.out.println("komme da rein delete");
        recipeRepository.deleteById(id);
    }
}