package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("description", meal.getDescription())
                    .addValue("dateTime", meal.getDateTime())
                    .addValue("calories", meal.getCalories())
                    .addValue("userId", userId);
            KeyHolder holder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update("INSERT INTO meals(description, date_time, calories, user_id) "
                    + "VALUES(:description, :dateTime, :calories, :userId)", params, holder);
            meal.setId(holder.getKey().intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET description=:description, date_time=:dateTime, calories=:calories " +
                        "WHERE id=:id", getParams(meal)) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> users = jdbcTemplate.query("SELECT * FROM meals WHERE  id=? AND user_id=?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<Meal> getAll(int userId) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId);
        return namedParameterJdbcTemplate.query("SELECT * FROM meals WHERE user_id=:user_id ORDER BY date_time DESC",
                params, ROW_MAPPER);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("start_date", startDate)
                .addValue("end_date", endDate);
        return namedParameterJdbcTemplate.query("SELECT * FROM meals WHERE user_id=:user_id AND date_time " +
                        "BETWEEN :start_date AND :end_date ORDER BY date_time DESC",
                params, ROW_MAPPER);
    }

    private SqlParameterSource getParams(Object bean) {
        return new BeanPropertySqlParameterSource(bean);
    }
}
