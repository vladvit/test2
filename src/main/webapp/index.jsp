<%@ page import="ru.javawebinar.topjava.web.SecurityUtil" %>
<%@ page import="java.util.Optional" %>
<%@ page import="java.util.function.Consumer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Topjava)</title>
    <style>
        .text {
            text-align:  right;
        }
    </style>
</head>
<body>
<h3>Проект <a href="https://github.com/JavaWebinar/topjava" target="_blank">Java Enterprise (Topjava)</a></h3>
<hr>
<form action="users" method="post">
    <select type="text" size="1" name="userid" required>
        <option <%= SecurityUtil.authUserId() == 0 ? "selected" : "" %> disabled>Select user</option>
        <option <%= SecurityUtil.authUserId() == 1 ? "selected" : "" %> value="1">User 1</option>
        <option <%= SecurityUtil.authUserId() == 2 ? "selected" : "" %> value="2">User 2</option>
        <option <%= SecurityUtil.authUserId() == 3 ? "selected" : "" %> value="3">User 3</option>
    </select>
    <input type="hidden" name="action" value="Login">
    <button type="submit">Login</button>
</form>
<ul>
    <li><a href="users">Users</a></li>
    <li><a href="meals">Meals</a></li>
</ul>
</body>
<footer>
    <hr>
    <p>
        Current user:
        <%= SecurityUtil.authUserIdAsString() %>
    </p>
</footer>
</html>
