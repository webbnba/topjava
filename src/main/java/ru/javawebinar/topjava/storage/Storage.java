package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface Storage {

    Meal add(Meal meal);

    Meal update(Meal meal);

    void delete(int id);

    Meal get(int id);

    List<Meal> getAllMeals();
}
