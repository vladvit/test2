package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class MealCounterUtil {
    private static AtomicInteger mealCounter = new AtomicInteger();

    private MealCounterUtil() {
    }

    public static int incrementCounter() {
        return mealCounter.incrementAndGet();
    }
}
