package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, List<Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);


    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(userId, key -> new ArrayList<>()).add(meal);
            return meal;
        } else {
            List<Meal> meals = getMealList(userId);
            meals.stream()
                    .filter(existing -> existing.getId().equals(meal.getId()))
                    .findFirst()
                    .ifPresent(existing -> {
                        existing.setDateTime(meal.getDateTime());
                        existing.setDescription(meal.getDescription());
                        existing.setCalories(meal.getCalories());
                    });
        }
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("Deleting meal with id {} for userId {}", id, userId);
        List<Meal> meals = getMealList(userId);
        Optional<Meal> mealToDelete = meals.stream()
                .filter(meal -> meal.getId() == id)
                .findFirst();
        if (mealToDelete.isPresent()) {
            meals.remove(mealToDelete.get());
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("Getting meal with id {} for userId {}", id, userId);
        List<Meal> meals = getMealList(userId);
        for (Meal meal : meals) {
            if (meal.getId() == id) {
                return meal;
            }
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("Getting all meals for userId {}", userId);
        List<Meal> meals = getMealList(userId);
        meals.sort(Comparator.comparing(Meal::getDateTime).reversed());
        return meals;
    }

    private List<Meal> getMealList(int userId) {
       return repository.getOrDefault(userId, Collections.emptyList());
    }
}

