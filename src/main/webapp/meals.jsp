<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="https://github.com/wecobol/functions" prefix="f" %>

<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<table class="greenTable">
    <thead>
    <tr>
        <th>Data</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Exceed</th>
    </tr>
    </thead>
    <tfoot>
    <tr>
        <td colspan="4">
            <div class="links"><a href="#">&laquo;</a> <a class="active" href="#">1</a> <a href="#">2</a> <a
                    href="#">3</a> <a href="#">4</a> <a href="#">&raquo;</a></div>
        </td>
    </tr>
    </tfoot>
    <tbody>

    <% // start scriptlet
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
    %>

    <c:forEach items="${requestScope.meals}" var="meal">
        <tr class="${meal.isExceed() ? 'exceed' : 'normal'}">
            <td><c:out value="${f:formatLocalDateTime(meal.getDateTime(), 'dd.MM.yyyy HH:mm')}"/></td>
            <td><c:out value="${meal.getDescription()}"/></td>
            <td><c:out value="${meal.getCalories()}"/></td>
            <td><c:out value="${meal.isExceed()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>