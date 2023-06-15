package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(userId, key -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
            return meal;
        } else {
            Map<Integer, Meal> meals = repository.get(userId);
            if (meals != null) {
                synchronized (meals) {
                    Meal existing = meals.get(meal.getId());
                    if (existing != null) {
                        meals.put(meal.getId(), meal);
                        return meal;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("Deleting meal with id {} for userId {}", id, userId);
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals != null) {
            if (meals.containsKey(id)) {
                meals.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("Getting meal with id {} for userId {}", id, userId);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null ? meals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Getting all meals for userId {}", userId);
        Map<Integer, Meal> meals = repository.getOrDefault(userId, Collections.emptyMap());
        if (!meals.isEmpty()) {
            return meals.values().stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<Meal> getFiltered(int userId, LocalDate fromDate, LocalDate toDate, LocalTime startTime, LocalTime endTime) {
        log.info("Getting all meals for userId {} from Date {} to Date {}", userId, fromDate, toDate);
        Map<Integer, Meal> meals = repository.getOrDefault(userId, Collections.emptyMap());
        if (!meals.isEmpty()) {
            return meals.values().stream()
                    .filter(meal -> !meal.getDate().isBefore(fromDate) && !meal.getDate().isAfter(toDate)
                                    && !meal.getTime().isBefore(startTime) && !meal.getTime().isAfter(endTime))
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        InMemoryMealRepository mealRepository = new InMemoryMealRepository();
        MealService mealService = new MealService(mealRepository);
        mealRepository.save(1, new Meal(LocalDateTime.now(), "Meal 1u1", 500));
        mealRepository.save(1, new Meal(LocalDateTime.now().plusHours(1), "Meal 2u1", 1000));
        mealRepository.save(1, new Meal(LocalDateTime.now().plusHours(2), "Meal 3u1", 700));
        mealRepository.save(2, new Meal(LocalDateTime.now(), "Meal 1u2", 500));
        mealRepository.save(2, new Meal(LocalDateTime.now().plusHours(1), "Meal 2u2", 700));
        mealRepository.save(2, new Meal(LocalDateTime.now().plusHours(2), "Meal 3u2", 300));

//        System.out.println(mealRepository.get(0, 4));
//        System.out.println(mealRepository.delete(0, 2));
//        System.out.println(mealRepository.getAll(1));
//        mealService.delete(1, 5);
        mealService.update(0, new Meal(1, LocalDateTime.now().plusHours(2), "Test", 300));
        System.out.println(mealService.getAll(1));


    }
}

