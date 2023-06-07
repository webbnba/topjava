package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface Storage {

    void addMeal(Meal meal);

    void updateMeal(Meal meal);

    void deleteMeal(Integer id);

    Meal getMeal(Integer id);

    List<MealTo> getAllMeals();
}
