package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import be.kuleuven.foodrestservice.exceptions.NoMealAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.stream.Collectors;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{id}")
    EntityModel<Meal> getMealById(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));

        return mealToEntityModel(id, meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    @GetMapping("/rest/meals/cheapest")
    EntityModel<Meal> getCheapestMeal() {
        Meal cheapestMeal = mealsRepository.getAllMeal()
                .stream()
                .min(Comparator.comparing(Meal::getPrice))
                .orElseThrow(NoMealAvailableException::new);

        return mealToEntityModel(cheapestMeal.getId(), cheapestMeal);
    }

    @GetMapping("/rest/meals/largest")
    EntityModel<Meal> getLargestMeal() {
        Meal largestMeal = mealsRepository.getAllMeal()
                .stream()
                .max(Comparator.comparing(Meal::getKcal))
                .orElseThrow(NoMealAvailableException::new);

        return mealToEntityModel(largestMeal.getId(), largestMeal);
    }

    @PostMapping("/rest/meals")
    public ResponseEntity<EntityModel<Meal>> addMeal(@RequestBody Meal newMeal) {
        Meal meal = mealsRepository.save(newMeal);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(meal.getId())
                .toUri();
        EntityModel<Meal> entityModel = mealToEntityModel(meal.getId(), meal);
        return ResponseEntity.created(location).body(entityModel);
    }

    @PutMapping("/rest/meals/{id}")
    public ResponseEntity<EntityModel<Meal>> updateMeal(@RequestBody Meal newMeal, @PathVariable String id) {
        Meal updatedMeal = mealsRepository.update(id, newMeal);
        EntityModel<Meal> entityModel = mealToEntityModel(updatedMeal.getId(), updatedMeal);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/rest/meals/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable String id) {
        mealsRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rest/orders")
    public CollectionModel<EntityModel<Meal>> placeOrder(@RequestBody Order order) {
        List<Meal> orderedMeals = order.getMealIds().stream()
                .map(mealId -> mealsRepository.findMeal(mealId)
                        .orElseThrow(() -> new MealNotFoundException(mealId)))
                .collect(Collectors.toList());

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : orderedMeals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }


    //将一个 Meal 对象转换成一个 EntityModel<Meal> 对象
    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"));
    }
    //linkTo(...): 这个方法用于构建链接
    //methodOn(MealsRestController.class): 这个方法用于指定链接目标应该在哪个控制器上
    //withSelfRel(): 这个方法用来指定创建的链接是一个“自链接”
    //.withRel("rest/meals"): 这个方法用来为链接设置一个关系名
    /*在 HATEOAS（Hypermedia as the Engine of Application State）原则中，关系名（relation name 或 rel）是一个重要概念，
    它为超媒体链接提供了语义上下文。这意味着关系名不仅告诉客户端有一个链接存在，而且还告诉客户端这个链接与当前资源的关系 */

}
