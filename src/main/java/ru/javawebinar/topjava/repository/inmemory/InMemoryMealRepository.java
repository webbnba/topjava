package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, ConcurrentHashMap<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        ConcurrentHashMap<Integer, Meal> meals1 = new ConcurrentHashMap<>();
        meals1.put(1, new Meal(1, LocalDateTime.now(), "Meal 1u1", 500));
        meals1.put(2, new Meal(2, LocalDateTime.now().plusHours(1), "Meal 2u1", 700));
        meals1.put(3, new Meal(3, LocalDateTime.now().plusHours(2), "Meal 3u1", 1000));

        ConcurrentHashMap<Integer, Meal> meals2 = new ConcurrentHashMap<>();
        meals2.put(1, new Meal(1, LocalDateTime.now(), "Meal 1u2", 600));
        meals2.put(2, new Meal(2, LocalDateTime.now().plusHours(1), "Meal 2u2", 800));
        meals2.put(3, new Meal(3, LocalDateTime.now().plusHours(2), "Meal 3u2", 1000));

        repository.put(1, meals1);
        repository.put(2, meals2);
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(userId, key -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
            return meal;
        } else {
            ConcurrentHashMap<Integer, Meal> meals = repository.get(userId);
            meals.replace(meal.getId(), meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("Deleting meal with id {} for userId {}", id, userId);
        Map<Integer, Meal> meals = repository.get(userId);
        Meal mealToDelete = meals.get(id);
        if (mealToDelete != null) {
            return meals.remove(id, mealToDelete);
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("Getting meal with id {} for userId {}", id, userId);
        ConcurrentHashMap<Integer, Meal> meals = repository.get(userId);
        return meals.getOrDefault(id, null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Getting all meals for userId {}", userId);
        ConcurrentHashMap<Integer, Meal> meals = repository.get(userId);
        return meals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

