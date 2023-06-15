package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.ADMIN_ID;
import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(USER_ID, meal));
        save(ADMIN_ID, new Meal(LocalDateTime.of(2022, Month.JUNE, 15, 13, 0), "Админ ланч", 600));
        save(ADMIN_ID, new Meal(LocalDateTime.of(2022, Month.JUNE, 15, 18, 0), "Админ ужин", 1500));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, key -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(userId, (key, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("Deleting meal with id {} for userId {}", id, userId);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("Getting meal with id {} for userId {}", id, userId);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null ? meals.get(id) : null;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return filterByPredicate(userId, meal -> Util.isBetweenHalfOpen(meal.getDateTime(), startDateTime, endDateTime));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        return CollectionUtils.isEmpty(meals) ? Collections.emptyList() :
                meals.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}

