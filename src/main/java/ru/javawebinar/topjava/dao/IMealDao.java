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
    Meal add(Meal meal);

    /**
     * Deletes meal from repo
     *
     * @param id meal id
     */
    void delete(int id);

    /**
     * Updates stored object
     *
     * @param meal   meal for update
     */
    Meal update(Meal meal);

    /**
     * Retrieves all stored objects
     *
     * @return
     */
    List<Meal> getAll();

    /**
     * Retrieves meal by its id
     *
     * @param id
     * @return
     */
    Meal getMealById(int id);
}
