<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo" scope="request"/>
    <title>Edit meal</title>
</head>
<body>
<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>DateTime</dt>
            <dd><input type="datetime-local" name="dateTime" size=30 value="${meal.dateTime}"></dd>
        </dl>
        <dl>
            <dt>Description</dt>
            <dd><input type="text" name="description" size="100" value="${meal.description}"></dd>
        </dl>
        <dl>
            <dt>Calories</dt>
            <dd><input type="text" name="calories" size="10" value="${meal.calories}"></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()">Cancel</button>
    </form>
</section>
</body>
</html>
