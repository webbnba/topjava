package ru.javawebinar.topjava.web;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.util.MealsUtil.*;

public class MealServlet extends HttpServlet {
    private ConfigurableApplicationContext springContext;
    private JdbcMealRepository jdbcMealRepository;

    @Override
    public void init() {
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        jdbcMealRepository = springContext.getBean(JdbcMealRepository.class);
    }

    @Override
    public void destroy() {
        springContext.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String dateTimeParam = request.getParameter("dateTime");
        String descriptionParam = request.getParameter("description");
        String caloriesParam = request.getParameter("calories");
        String idParam = request.getParameter("id");

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeParam);
        String description = StringUtils.hasLength(descriptionParam) ? descriptionParam : "";
        int calories = StringUtils.hasLength(caloriesParam) ? Integer.parseInt(caloriesParam) : 0;
        Integer id = StringUtils.hasLength(idParam) ? Integer.parseInt(idParam) : null;

        Meal meal = new Meal(id, dateTime, description, calories);
        jdbcMealRepository.save(meal, SecurityUtil.authUserId());
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                jdbcMealRepository.delete(id, SecurityUtil.authUserId());
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ? new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) : jdbcMealRepository.get(getId(request), SecurityUtil.authUserId());
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                LocalDateTime startDate = atStartOfDayOrMin(LocalDate.parse(request.getParameter("startDate")));
                LocalDateTime endDate = atStartOfNextDayOrMax(LocalDate.parse(request.getParameter("endDate")));
                LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
                LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
                request.setAttribute("meals", getFilteredTos(jdbcMealRepository.getBetweenHalfOpen(startDate, endDate, SecurityUtil.authUserId()), DEFAULT_CALORIES_PER_DAY, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                request.setAttribute("meals", getTos(jdbcMealRepository.getAll(SecurityUtil.authUserId()), DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
