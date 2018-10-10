package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealCrudInMemoryDaoImpl implements IMealDao {
    private static AtomicInteger mealCounter = new AtomicInteger();

    private Map<Integer, Meal> mealMap;

    public MealCrudInMemoryDaoImpl() {
        mealMap = new ConcurrentHashMap<>();
    }

    @Override
    public Meal add(Meal meal) {
        meal.setId(mealCounter.incrementAndGet());
        return mealMap.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        return mealMap.replace(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getMealById(int id) {
        return mealMap.get(id);
    }
}
