package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
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
import java.util.Optional;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();

        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = Optional.ofNullable(request.getParameter("action")).orElse("addUpdate");
        String id = request.getParameter("id");

        switch (action) {
            case "Filter": {
                mealRestController.setFilterPeriod(LocalDate.parse(request.getParameter("startDate")), LocalDate.parse(request.getParameter("endDate")));
                mealRestController.setFilterTime(LocalTime.parse(request.getParameter("startTime")), LocalTime.parse(request.getParameter("endTime")));
                forwardAndShowList(request, response);
                break;
            }
            case "Reset": {
                mealRestController.resetFilter();
                forwardAndShowList(request, response);
                break;
            }
            default: {
                Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")), SecurityUtil.authUserId());

                log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
                mealRestController.save(meal);
                response.sendRedirect("meals");
                break;
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = Optional.ofNullable(request.getParameter("action")).orElse("all");

        switch (action) {
            case "delete": {
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            }
            case "create":
            case "update": {
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId()) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            }
            case "all":
            default: {
                forwardAndShowList(request, response);
                break;
            }
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private void forwardAndShowList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("listMeals");
        request.setAttribute("controller", mealRestController);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
