package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import be.kuleuven.foodrestservice.exceptions.NoMealAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

import java.net.URI;
import java.util.stream.Collectors;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/meals/cheapest")
    Meal getCheapestMeal() {
        return mealsRepository.getAllMeal()
                .stream()
                .min(Comparator.comparing(Meal::getPrice))
                .orElseThrow(NoMealAvailableException::new);
    }

    @GetMapping("/restrpc/meals/largest")
    Meal getLargestMeal() {
        return mealsRepository.getAllMeal()
                .stream()
                .max(Comparator.comparing(Meal::getKcal))
                .orElseThrow(NoMealAvailableException::new);
    }

    @PostMapping("/restrpc/meals")
    ResponseEntity<Meal> addMeal(@RequestBody Meal newMeal) {
        Meal meal = mealsRepository.save(newMeal);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(meal.getId()).toUri();
        return ResponseEntity.created(location).body(meal);
    }

    @PutMapping("/restrpc/meals/{id}")
    Meal updateMeal(@RequestBody Meal newMeal, @PathVariable String id) {
        return mealsRepository.update(id, newMeal);
    }

    //ResponseEntity<?> 使用泛型通配符 ? 表示它可以包含任何类型的体（body）
    @DeleteMapping("/restrpc/meals/{id}")
    ResponseEntity<?> deleteMeal(@PathVariable String id) {
        mealsRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/restrpc/orders")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        List<Meal> orderedMeals = order.getMealIds().stream()
                .map(mealId -> mealsRepository.findMeal(mealId)
                        .orElseThrow(() -> new MealNotFoundException(mealId)))
                .collect(Collectors.toList());

        String mealNames = orderedMeals.stream()
                .map(Meal::getName)
                .collect(Collectors.joining(", "));

        String confirmationMessage = "Order for " + orderedMeals.size() + " meal(s) placed successfully: " + mealNames + ".\n";
        return ResponseEntity.ok(confirmationMessage);
    }
}
