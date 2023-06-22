package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;

import java.time.Month;
import java.util.Arrays;

import static java.time.LocalDateTime.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int ID_1 = START_SEQ + 3;
    public static final int ID_2 = START_SEQ + 4;
    public static final int ID_3 = START_SEQ + 5;
    public static final int ID_4 = START_SEQ + 6;

    public static final Meal meal1 = new Meal(ID_1, of(2023, 6, 19, 9, 40, 0), "Завтрак", 500);
    public static final Meal meal2 = new Meal(ID_2, of(2023, 6, 19, 13, 40, 0), "Обед", 800);
    public static final Meal meal3 = new Meal(ID_3, of(2023, 6, 19, 19, 40, 0), "Ужин", 1000);
    public static final Meal meal4 = new Meal(ID_4, of(2023, 6, 20, 7, 30, 0), "Завтрак", 400);

    public static Meal getNew() {
        return new Meal(null, of(2023, Month.JUNE, 22, 22, 00), "New", 800);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDescription("Updated");
        updated.setCalories(700);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
