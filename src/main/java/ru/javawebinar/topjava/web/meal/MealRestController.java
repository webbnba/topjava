package ru.javawebinar.topjava.web.meal;

import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@RestController
@RequestMapping("/meals")
public class MealRestController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    @GetMapping
    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    @GetMapping("/filter")
    public List<MealTo> getFiltered(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalTime startTime,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) LocalTime endTime) {
        return MealsUtil.getFilteredTos(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime)
                .stream().filter(mealTo -> !mealTo.getDateTime().toLocalDate().isBefore(startDate) && !mealTo.getDateTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return service.get(authUserId(), id);
    }

    @DeleteMapping("/id")
    public void delete(@PathVariable int id) {
        service.delete(authUserId(), id);
    }

    @PostMapping
    public Meal create(@RequestParam(required = false) Meal meal) {
        return service.create(authUserId(), meal);
    }

}