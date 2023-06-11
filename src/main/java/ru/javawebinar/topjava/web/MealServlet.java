package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.Storage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final Storage storage = new MealStorage();
    private static final String INSERT_OR_EDIT = "/editMeal.jsp";
    private static final String LIST_USER = "/meals.jsp";
    public static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        String dateTime = req.getParameter("dateTime");
        meal.setDateTime(LocalDateTime.parse(dateTime));
        meal.setDescription(req.getParameter("description"));
        meal.setCalories(Integer.parseInt(req.getParameter("calories")));
        String id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            storage.add(meal);
        } else {
            meal.setId(MealStorage.generatedId());
            storage.update(meal);
        }
        RequestDispatcher view = req.getRequestDispatcher(LIST_USER);
        req.setAttribute("meals", storage.getAllMeals());
        view.forward(req, resp);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            storage.delete(id);
            forward = LIST_USER;
            request.setAttribute("meals", storage.getAllMeals());
        } else if (action.equalsIgnoreCase("edit")) {
            if (storage.getAllMeals().isEmpty()) {
                Meal empty = storage.add(new Meal(MealStorage.generatedId(), LocalDateTime.now(), "", 0));
                forward = INSERT_OR_EDIT;
                request.setAttribute("meal", empty);
            } else {
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    int id = Integer.parseInt(idParam);
                    Meal meal = storage.get(id);
                    if (meal != null) {
                        forward = INSERT_OR_EDIT;
                        request.setAttribute("meal", meal);
                    }
                }
            }
        } else if (action.equalsIgnoreCase("meals")) {
            Map<LocalDate, Integer> caloriesSumByDate = storage.getAllMeals().stream()
                    .collect(
                            Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
            List<MealTo> mealTo = storage.getAllMeals().stream().map(meal ->
                    new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                    .collect(Collectors.toList());
            log.debug("redirect forward to meals");
            forward = LIST_USER;
            request.setAttribute("meals", mealTo);
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }
}

