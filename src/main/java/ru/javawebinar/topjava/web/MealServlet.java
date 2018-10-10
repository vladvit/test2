package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger log = getLogger(MealServlet.class);

    private static final String ATTR_MEALS = "meals";
    private static final String ATTR_MEAL = "meal";

    private final String ATTR_DATE = "date";
    private final String ATTR_DESC = "desc";
    private final String ATTR_CALORIES = "calories";
    private final String ATTR_MEAL_ID = "mealId";

    private final String ATTR_ACTION = "action";
    private final String ATTR_VALUE_ACTION_DELETE = "delete";
    private final String ATTR_VALUE_ACTION_EDIT = "edit";
    private final String ATTR_VALUE_ACTION_INSERT = "insert";
    private final String ATTR_VALUE_ACTION_LIST = "list";

    private static String ADDR_INSERT_OR_EDIT = "/add_meal.jsp";
    private static String ADDR_LIST_MEAL = "/meals.jsp";
    private static String ADDR_REDIRECT_MEAL_SERVLET = "meals";

    private static IMealDao dao;

    static {
        dao = DbUtil.getDao();
        dao.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак 1", 500));
        dao.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед 1", 1000));
        dao.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин 1", 500));
        dao.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак 2", 1000));
        dao.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед 2", 500));
        dao.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин 2", 510));
    }

    public MealServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";

        String action = request.getParameter(ATTR_ACTION);
        if (action == null) {
            action = ATTR_VALUE_ACTION_LIST;
        }

        switch (action) {
            case ATTR_VALUE_ACTION_EDIT: {
                request.setAttribute(ATTR_MEAL, dao.getMealById(Integer.parseInt(request.getParameter(ATTR_MEAL_ID))));
                // go through, no break
            }
            case ATTR_VALUE_ACTION_INSERT: {
                log.debug("forward to insert/edit");
                forward = ADDR_INSERT_OR_EDIT;
                break;
            }
            case ATTR_VALUE_ACTION_DELETE: {
                dao.delete(Integer.parseInt(request.getParameter(ATTR_MEAL_ID)));

                log.debug("redirect to meals");
                response.sendRedirect(ADDR_REDIRECT_MEAL_SERVLET);
                return;
            }
            default: { // show list of meals
                request.setAttribute(ATTR_MEALS, MealsUtil.getFilteredWithExceeded(dao.getAll(), 2000));

                log.debug("forward to meals");
                forward = ADDR_LIST_MEAL;
                break;
            }
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        final Meal meal;
        try {
            meal = new Meal(LocalDateTime.parse(request.getParameter(ATTR_DATE)), request.getParameter(ATTR_DESC),
                    Integer.parseInt(request.getParameter(ATTR_CALORIES)));
        } catch (Exception e) {
            log.error("Wrong parameters", e);
            response.sendRedirect("meals");
            return;
        }

        final String mealId = request.getParameter(ATTR_MEAL_ID);
        if (mealId == null || mealId.isEmpty()) {
            dao.add(meal);
        } else {
            try {
                meal.setId(Integer.parseInt(mealId));
                dao.update(meal);
            } catch (Exception e) {
                log.error("Wrong attribute", e);
            }
        }

        log.debug("redirect to meals");
        response.sendRedirect(ADDR_REDIRECT_MEAL_SERVLET);
    }
}
