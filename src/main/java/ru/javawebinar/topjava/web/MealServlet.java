package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MapStorage;
import ru.javawebinar.topjava.storage.Storage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final Storage storage = new MapStorage();
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private static final String INSERT_OR_EDIT = "/editMeal.jsp";
    private static final String LIST_USER = "/meals.jsp";
    public static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        String dateTime = req.getParameter("dateTime");
        meal.setDateTime(LocalDateTime.parse(dateTime));
        meal.setDescription(req.getParameter("description"));
        meal.setCalories(Integer.parseInt(req.getParameter("calories")));
        String id = req.getParameter("id");
        if(id == null || id.isEmpty()) {
            storage.addMeal(meal);
        } else {
            meal.setId(COUNTER.incrementAndGet());
            storage.updateMeal(meal);
        }
        RequestDispatcher view = req.getRequestDispatcher(LIST_USER);
        req.setAttribute("meals", storage.getAllMeals());
        view.forward(req, resp);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            int id = Integer.parseInt(request.getParameter("id"));
            storage.deleteMeal(id);
            forward = LIST_USER;
            request.setAttribute("meals", storage.getAllMeals());
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = storage.getMeal(id);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("meals")){
            forward = LIST_USER;
            log.debug("redirect forward to meals");
            request.setAttribute("meals", storage.getAllMeals());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }
}

