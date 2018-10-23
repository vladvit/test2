package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/initDB.sql", "classpath:db/populateUsersDB.sql", "classpath:db/populateMealsDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final int USER_ID = START_SEQ;
    private static final int ADMIN_ID = START_SEQ + 1;

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() throws Exception {
        Meal created = service.create(new Meal(LocalDateTime.of(2014, Month.MAY, 1, 10, 0), "Завтрак", 500), USER_ID);
        assertMatch(service.get(created.getId(), USER_ID), created);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeAndUserCreate() throws Exception {
        service.create(new Meal(LocalDateTime.of(2017, Month.MAY, 2, 10, 0), "Завтрак", 500), USER_ID);
        service.create(new Meal(LocalDateTime.of(2017, Month.MAY, 2, 10, 0), "Завтрак", 500), USER_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = service.create(new Meal(LocalDateTime.of(2015, Month.MAY, 3, 10, 0), "Завтрак", 500), USER_ID);
        updated.setDescription("Updated Description");
        updated.setCalories(330);
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotOwned() throws Exception {
        Meal updated = service.create(new Meal(LocalDateTime.of(2016, Month.MAY, 4, 10, 0), "Завтрак", 500), USER_ID);
        updated.setDescription("Updated Description");
        updated.setCalories(330);
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void delete() throws Exception {
        Meal deleted = service.create(new Meal(LocalDateTime.of(2016, Month.MAY, 5, 10, 0), "Завтрак", 500), USER_ID);
        service.delete(deleted.getId(), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1, 1);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotOwned() throws Exception {
        Meal deleted = service.create(new Meal(LocalDateTime.of(2016, Month.MAY, 6, 10, 0), "Завтрак", 500), USER_ID);
        service.delete(deleted.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, 1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotOwned() throws Exception {
        Meal userMeal = service.create(new Meal(LocalDateTime.of(2016, Month.MAY, 7, 10, 0), "Завтрак", 500), USER_ID);
        service.delete(userMeal.getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenDates() throws Exception {
        final Meal meal1 = service.create(new Meal(LocalDateTime.of(2018, Month.APRIL, 6, 10, 0), "Завтрак", 500), USER_ID);
        final Meal meal2 = service.create(new Meal(LocalDateTime.of(2018, Month.MAY, 6, 10, 0), "Завтрак", 500), ADMIN_ID);
        final Meal meal3 = service.create(new Meal(LocalDateTime.of(2018, Month.JUNE, 6, 10, 0), "Завтрак", 500), USER_ID);
        assertMatch(service.getBetweenDates(LocalDate.of(2018, Month.MARCH, 1),
                LocalDate.of(2018, Month.JULY, 1), USER_ID), meal3, meal1);
    }

}