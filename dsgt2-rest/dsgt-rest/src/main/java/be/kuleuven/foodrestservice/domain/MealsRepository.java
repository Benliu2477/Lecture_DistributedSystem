package be.kuleuven.foodrestservice.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.*;

import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;

@Component
public class MealsRepository {
    // map: id -> meal
    private static final Map<String, Meal> meals = new HashMap<>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setId("5268203c-de76-4921-a3e3-439db69c462a");
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealType(MealType.MEAT);
        a.setKcal(1100);
        a.setPrice((10.00));

        meals.put(a.getId(), a);

        Meal b = new Meal();
        b.setId("4237681a-441f-47fc-a747-8e0169bacea1");
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealType(MealType.VEGAN);
        b.setKcal(637);
        b.setPrice((7.00));

        meals.put(b.getId(), b);

        Meal c = new Meal();
        c.setId("cfd1601f-29a0-485d-8d21-7607ec0340c8");
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealType(MealType.FISH);
        c.setKcal(950);
        c.setPrice(5.00);

        meals.put(c.getId(), c);
    }

    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
        return Optional.ofNullable(meal);
    }

    //values() 是用于从 HashMap 中提取所有值（在这个案例中就是 Meal 对象）
    public Collection<Meal> getAllMeal() {
        return meals.values();
    }

    public Meal save(Meal meal) {
        // Generate a new ID for new meals or use existing ID
        if (meal.getId() == null || meal.getId().isEmpty()) {
            meal.setId(UUID.randomUUID().toString());
        }
        meals.put(meal.getId(), meal);
        return meal;
    }

    public Meal update(String id, Meal meal) {
        Assert.notNull(id, "The meal id must not be null");
        if (!meals.containsKey(id)) {
            throw new MealNotFoundException(id);
        }
        meal.setId(id); // Ensure the updated meal has the correct ID
        meals.put(id, meal);
        return meal;
    }

    public void delete(String id) {
        if (!meals.containsKey(id)) {
            throw new MealNotFoundException(id);
        }
        meals.remove(id);
    }

}
