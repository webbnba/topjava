package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.MealServlet.CALORIES_PER_DAY;

public class MapStorage implements Storage {
    private final static Map<Integer, Meal> mapOfMeal = new ConcurrentHashMap();

    @Override
    public void addMeal(Meal meal) {
        mapOfMeal.put(meal.getId(), meal);
    }

    @Override
    public void updateMeal(Meal meal) {
        mapOfMeal.put(meal.getId(), meal);
    }

    @Override
    public void deleteMeal(Integer id) {
        mapOfMeal.remove(id);
    }

    @Override
    public Meal getMeal(Integer id) {
        return mapOfMeal.get(id);
    }

    @Override
    public List<MealTo> getAllMeals() {
//        return mapOfMeal.values().stream().map(meal -> new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories())).collect(Collectors.toList());
        Map<LocalDate, Integer> caloriesSumByDate = mapOfMeal.values().stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
       return mapOfMeal.values().stream()
                .map(meal -> new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                .collect(Collectors.toList());
    }
}
