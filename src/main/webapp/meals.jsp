<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.Year" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.jsp">Home</a></h3>
    <h2>Meals</h2>
    <form method="post" action="meals">
        <jsp:useBean id="controller" scope="request" type="ru.javawebinar.topjava.web.meal.MealRestController"/>
        <table border="0" cellpadding="8" cellspacing="0">
            <tr>
                <td>
                    <dl>
                        <dt>From date:</dt>
                        <dd><input type="date" name="startDate" value=${controller.startDate} required></dd>
                    </dl>
                    <dl>
                        <dt>From time:</dt>
                        <dd><input type="time" name="startTime" value=${controller.startTime} required></dd>
                    </dl>
                </td>
                <td>
                    <dl>
                        <dt>To date:</dt>
                        <dd><input type="date" name="endDate" value=${controller.endDate} required></dd>
                    </dl>
                    <dl>
                        <dt>To time:</dt>
                        <dd><input type="time" name="endTime" value=${controller.endTime} required></dd>
                    </dl>
                </td>
            </tr>
        </table>
        <input type="submit" name="action" value="Filter"/>
        <input type="submit" name="action" value="Reset"/>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${controller.withExceeded}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
