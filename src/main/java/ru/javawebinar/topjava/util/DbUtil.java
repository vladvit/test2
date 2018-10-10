package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.repository.MealCrudInMemoryDaoImpl;

public class DbUtil {
    private static IMealDao dao;

    static {
        dao = new MealCrudInMemoryDaoImpl();
    }

    // Suppresses default constructor, ensuring non-instantiability.
    private DbUtil() {
    }

    public static IMealDao getDao() {
        return dao;
    }
}
