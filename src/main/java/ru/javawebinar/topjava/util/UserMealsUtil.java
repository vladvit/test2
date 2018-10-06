package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    /**
     * Implementation of list filtering via {@link java.util.stream.Stream}
     *
     * @param mealList       original list
     * @param startTime      start time
     * @param endTime        end time
     * @param caloriesPerDay amount of calories
     * @return a new list with {@link UserMealWithExceed} objects formed from the original list
     */
    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime,
                                                                   LocalTime endTime, int caloriesPerDay) {
        // Map<Date, Predefined amount of calories per day>
        final Map<LocalDate, Integer> caloriesPerDateMap = mealList.stream()
                .collect(
                        Collectors.groupingBy(meal ->
                                        meal.getDateTime().toLocalDate(), // grouping by date
                                Collectors.summingInt(UserMeal::getCalories))); // summing of calories

        // Time filter
        final Predicate<UserMeal> mealFilter = new Predicate<UserMeal>() {
            @Override
            public boolean test(UserMeal userMeal) {
                final LocalTime mealTime = userMeal.getDateTime().toLocalTime();
                return mealTime.isAfter(startTime) && mealTime.isBefore(endTime);
            }
        };

        // create an extended list
        // filling with @UserMealWithExceed
        final List<UserMealWithExceed> filteredUmList = mealList.stream()
                .filter(mealFilter)
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                caloriesPerDateMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

//        // Second variant (Optional 2) // but I think this variant is not so clear as the first and requires more
//        // efforts for further support
//        mealList.stream().collect(
//                Collectors.groupingBy(meal ->
//                        meal.getDateTime().toLocalDate())).forEach((date, uml) -> {
//            uml.stream().caloriesPerDateMap(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
//                    uml.stream().collect(Collectors.summingInt(UserMeal::getCalories)) > caloriesPerDay)).forEach(filteredUmlList::add);
//        });

 //       filteredUmList.forEach(System.out::println);
        return filteredUmList;
    }

    /**
     * Implementation of list filtering via loops
     *
     * @param mealList       original list
     * @param startTime      start time
     * @param endTime        end time
     * @param caloriesPerDay amount of calories
     * @return a new list with {@link UserMealWithExceed} objects formed from the original list
     */
    public static List<UserMealWithExceed> getFilteredWithExceededViaLoops(List<UserMeal> mealList, LocalTime startTime,
                                                                           LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, List<UserMeal>> umlPerDateMap = new HashMap<>();

        // grouping UserMeal by date
        for (UserMeal meal : mealList) {
            final List<UserMeal> umByDateList = umlPerDateMap.getOrDefault(meal.getDateTime().toLocalDate(), new ArrayList<>());
            umByDateList.add(meal);
            umlPerDateMap.put(meal.getDateTime().toLocalDate(), umByDateList);
        }

        // create an extended list
        final List<UserMealWithExceed> filteredUmList = new ArrayList<>();

        for (List<UserMeal> uml : umlPerDateMap.values()) {
            int iTotalCalories = 0;

            // summing of calories
            for (UserMeal meal : uml) {
                iTotalCalories += meal.getCalories();
            }

            // filling with @UserMealWithExceed
            for (UserMeal meal : uml) {
                final LocalTime mealTime = meal.getDateTime().toLocalTime();
                if (mealTime.isAfter(startTime) && mealTime.isBefore(endTime)) {
                    filteredUmList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                            iTotalCalories > caloriesPerDay));
                }
            }
        }

//        filteredUmList.forEach(System.out::println);

        return filteredUmList;
    }
}
