<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Meals</title>
    <style>
        table, th, td {
            border: 1px solid black;
        }
        .excess {
            color: red;
        }
        .normal {
            color: green;
        }
    </style>
</head>
<body>
<h1>Meals List</h1>
<hr>
<p><a href="meals?action=edit">Add Meal</a></p>

<table>
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan=2>Action</th>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${meals}" var="meal">
        <tr class="${meal.excess ? 'excess' : 'normal'}">
            <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"))}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?id=${meal.id}&action=delete">Delete</a></td>
            <td><a href="meals?id=${meal.id}&action=edit">Edit</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

