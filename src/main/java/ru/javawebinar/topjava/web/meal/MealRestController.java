package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return null;
    }

    public Meal get(int id) {
        return service.get(authUserId(), id);
    }

    public void delete(int id) {
        service.delete(authUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        ValidationUtil.checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public void update(int id, Meal meal) {
        log.info("update meal {} for id {}", meal, id);
        ValidationUtil.assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }
}