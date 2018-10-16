package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;

public interface MealService {
    Meal create(Meal meal, int authUserId);

    void delete(int id, int authUserId) throws NotFoundException;

    Meal get(int id, int authUserId) throws NotFoundException;

    void update(Meal meal, int authUserId);

    Collection<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate);
}