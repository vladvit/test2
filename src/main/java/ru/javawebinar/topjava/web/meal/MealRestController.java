package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private MealService service;

    private final LocalDate START_DATE = LocalDate.of(1970, 1, 1);
    private final LocalDate END_DATE = LocalDate.now();
    private final LocalTime START_TIME = LocalTime.MIN;
    private final LocalTime END_TIME = LocalTime.MAX;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;

        resetFilter();
    }

    public void save(Meal meal) {
        if (meal.isNew()) {
            service.create(meal, authUserId());
        } else {
            service.update(meal, authUserId());
        }
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }

    public Collection<MealWithExceed> getWithExceeded() {
        return getWithExceeded(this.startDate, this.endDate, this.startTime, this.endTime);
    }

    public Collection<MealWithExceed> getWithExceeded(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredWithExceeded(service.getAll(authUserId(), startDate, endDate), SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal get(int id) {
        return service.get(id, authUserId());
    }

    public void setFilterPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setFilterTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void resetFilter() {
        startDate = START_DATE;
        endDate = END_DATE;
        startTime = START_TIME;
        endTime = END_TIME;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}