<%@ page import="com.javawebinar.eatingpoll.exceptions.BadRequestException" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Eating Poll</title>
</head>
<body>
<div align="center">
    <h2>Eating Poll</h2>
    <h2>New Dish</h2>
    <form:form action="save?userId=${userId}" method="post" modelAttribute="dish">
        <table border="0" cellpadding="5">
            <tr>
                <td><form:hidden path="id" value="${dish.id}"/></td>
                <td>Name:</td>
                <td><form:input path="name" value="${dish.name}"/></td>
            </tr>
            <tr>
                <td><form:hidden path="restaurantId" value="${dish.restaurantId}"/></td>
                <td>Price:</td>
                <td><form:input type="number" step="0.01" path="price" value="${dish.price}"/></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="Save"></td>
            </tr>
        </table>
    </form:form>
</div>
</body>
</html>