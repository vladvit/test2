package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static int loggedUserId = 0;

    public static int authUserId() {
        return loggedUserId;
    }

    public static String authUserIdAsString() {
        return loggedUserId > 0 ? "" + loggedUserId : "not logged";
    }

    public static void setAuthUserId(Integer id) {
        loggedUserId = id;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}