package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * DAO for Meals
 */
public interface IMealDao {
    /**
     * Adds a new meal
     *
     * @param meal new meal
     */
    void addMeal(Meal meal);

    /**
     * Deletes meal from repo
     *
     * @param mealId meal id
     */
    void deleteMeal(int mealId);

    /**
     * Updates stored object
     *
     * @param mealId meal id
     * @param meal   meal for update
     */
    void updateMeal(int mealId, Meal meal);

    /**
     * Retrieves all stored objects
     *
     * @return
     */
    List<Meal> getAllMeals();

    /**
     * Retrieves meal by its id
     *
     * @param mealId
     * @return
     */
    Meal getMealById(int mealId);
}
