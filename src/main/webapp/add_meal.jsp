<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <title>Add new meal</title>
    <style type="text/css">
        input[type=text], select {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type=datetime-local], select {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type=submit] {
            width: 100%;
            background-color: #4CAF50;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type=submit]:hover {
            background-color: #45a049;
        }

        div {
            border-radius: 5px;
            background-color: #f2f2f2;
            padding: 20px;
        }
    </style>
</head>

<body>
<form class="greenTable" method="POST" action='crud' name="frmAddMeal">
    <input type="hidden" name="mealId" value="${meal.getId()}">
    Date : <input
        type="datetime-local" name="date"
        value="${meal.getDateTime()}"/> <br/>
    Description : <input
        type="text" name="desc"
        value="<c:out value="${meal.getDescription()}" />"/> <br/>
    Calories : <input
        type="text" name="calories"
        value="<c:out value="${meal.getCalories()}" />"/> <br/>
    <input
            type="submit" value="Submit"/>
</form>
</body>
</html>