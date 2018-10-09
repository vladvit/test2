package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealCrudServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger log = getLogger(MealCrudServlet.class);

    private static final String ATTR_MEALS = "meals";
    private static final String ATTR_MEAL = "meal";

    private final String ATTR_DATE = "date";
    private final String ATTR_DESC = "desc";
    private final String ATTR_CALORIES = "calories";
    private final String ATTR_MEAL_ID = "mealId";

    private final String ATTR_ACTION = "action";
    private final String ATTR_VALUE_ACTION_DELETE = "delete";
    private static final String ATTR_VALUE_ACTION_EDIT = "edit";
    private static final String ATTR_VALUE_ACTION_LISTMEALS = "listMeals";

    private static String ADDR_INSERT_OR_EDIT = "/add_meal.jsp";
    private static String ADDR_LIST_MEAL = "/meals.jsp";

    private IMealDao dao;

    public MealCrudServlet() {
        super();
        dao = DbUtil.getDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter(ATTR_ACTION);

        if (action.equalsIgnoreCase(ATTR_VALUE_ACTION_DELETE)) {
            int userId = Integer.parseInt(request.getParameter(ATTR_MEAL_ID));
            dao.deleteMeal(userId);
            response.sendRedirect("meals");
            return;
        } else if (action.equalsIgnoreCase(ATTR_VALUE_ACTION_EDIT)) {
            forward = ADDR_INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter(ATTR_MEAL_ID));
            Meal meal = dao.getMealById(mealId);
            request.setAttribute(ATTR_MEAL, meal);
        } else if (action.equalsIgnoreCase(ATTR_VALUE_ACTION_LISTMEALS)) {
            forward = ADDR_LIST_MEAL;
            request.setAttribute(ATTR_MEALS, MealsUtil.getFilteredWithExceeded(dao.getAllMeals(), 2000));
        } else {
            forward = ADDR_INSERT_OR_EDIT;
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Meal meal = null;
        try {
            meal = new Meal(LocalDateTime.parse(request.getParameter(ATTR_DATE)), request.getParameter(ATTR_DESC),
                    Integer.parseInt(request.getParameter(ATTR_CALORIES)));
        } catch (Exception e) {
            log.error("Wrong parameters", e);
        }

        if (meal != null) {
            Integer mealId = null;
            try {
                mealId = Integer.parseInt(request.getParameter(ATTR_MEAL_ID));
            } catch (Exception e) {
                log.error("Wrong attribute", e);
            }

            if (mealId == null) {
                dao.addMeal(meal);
            } else {
                dao.updateMeal(mealId, meal);
            }
        }

        RequestDispatcher view = request.getRequestDispatcher(ADDR_LIST_MEAL);
        request.setAttribute(ATTR_MEALS, MealsUtil.getFilteredWithExceeded(dao.getAllMeals(), 2000));
        view.forward(request, response);
    }
}
