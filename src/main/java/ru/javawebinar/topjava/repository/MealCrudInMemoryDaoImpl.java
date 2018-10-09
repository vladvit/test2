package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealCounterUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MealCrudInMemoryDaoImpl implements IMealDao {
    private Map<Integer, Meal> mealMap;

    public MealCrudInMemoryDaoImpl() {
        mealMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(MealCounterUtil.incrementCounter());
        mealMap.put(meal.getId(), meal);
    }

    @Override
    public void deleteMeal(int id) {
        mealMap.remove(id);
    }

    @Override
    public void updateMeal(int id, Meal meal) {
        meal.setId(id);
        mealMap.replace(id, meal);
    }

    @Override
    public List<Meal> getAllMeals() {
        return mealMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Meal getMealById(int id) {
        return mealMap.get(id);
    }
}
