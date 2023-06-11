package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealStorage implements Storage {
    private static final Map<Integer, Meal> mapOfMeal = new ConcurrentHashMap();
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    public Meal add(Meal meal) {
        return mapOfMeal.put(meal.getId(), meal);
    }

    @Override
    public Meal update(Meal meal) {
        if (mapOfMeal.get(meal.getId()) == null) {
            return null;
        } else return mapOfMeal.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mapOfMeal.remove(id);
    }

    @Override
    public Meal get(int id) {
        return mapOfMeal.get(id);
    }

    @Override
    public List<Meal> getAllMeals() {
        return mapOfMeal.values().stream().map(meal -> new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories())).collect(Collectors.toList());
    }
    public static int generatedId() {
        return COUNTER.incrementAndGet();
    }
}
