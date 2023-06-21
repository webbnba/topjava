package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService service;

    @Test
    public void get() {
        Meal meal = service.get(ID_1, USER_ID);
        MealTestData.assertMatch(meal, MealTestData.meal1);

    }

    @Test
    public void delete() {
        service.delete(ID_1, USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> service.get(ID_1, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2023, 6, 18);
        LocalDate endDateTime = LocalDate.of(2023, 6, 19);
        List<Meal> meals = service.getBetweenInclusive(startDate, endDateTime, USER_ID);
        Assertions.assertThat(meals).isNotNull();
        Assertions.assertThat(meals).hasSize(3);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        MealTestData.assertMatch(all, meal1, meal2, meal3, meal4);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        MealTestData.assertMatch(service.get(updated.getId(), USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void deleteNotYourOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(meal1.getId(), ADMIN_ID));
    }

    @Test
    public void updateNotYourOwn() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
    }

    @Test
    public void getNotYourOwn() {
        assertThrows(NotFoundException.class, () -> service.get(meal1.getId(), ADMIN_ID));
    }

}