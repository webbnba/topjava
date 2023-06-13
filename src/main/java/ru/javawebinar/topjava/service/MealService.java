package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), userId);
    }

    public Meal get(int userId, int id) {
        Meal meal = repository.get(userId, id);
        if (meal.isNew()) {
            throw new NotFoundException("Meal not found or it belongs to another user");
        } else {
            return checkNotFoundWithId(meal, userId);
        }
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }
}