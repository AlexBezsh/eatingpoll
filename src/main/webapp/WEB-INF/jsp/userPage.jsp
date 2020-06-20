<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Eating Poll</title>
    <style><%@include file="/WEB-INF/css/styles.css" %></style>
</head>
<body>
<div align="center">
    <h1>Eating Poll</h1>
    <p><a href="${pageContext.request.contextPath}/">Back to profile options</a></p>
    <h4>User: ${user.name}. Role: ${user.role}
        <a href="${pageContext.request.contextPath}/user/update">Update profile</a>
        <a ${(user.email.equals("user1@gmail.com") || user.email.equals("user2@gmail.com")) ? "hidden" : ""}
                href="${pageContext.request.contextPath}/user/delete">Delete this profile</a>
    </h4>
    <br/>
    <div class="form-style-2-heading">
        <h3>Restaurants:</h3>
    </div>
    <c:forEach items="${restaurants}" var="restaurant">
        <h4>${restaurant.name}</h4>
        <p>Number of Votes: ${restaurant.votesCount} <a href="${pageContext.request.contextPath}/user/vote?restaurantId=${restaurant.id}">Choose</a>
        </p>
        <table border="1" cellpadding="5" class="zui-table">
            <thead>
            <tr>
                <th>Name</th>
                <th>Price</th>
            </tr>
            </thead>
            <c:forEach items="${restaurant.dishes}" var="dish">
                <tbody>
                <tr>
                    <td>${dish.name}</td>
                    <td>${dish.price.toString().matches("\\d+\\.\\d{1}") ? dish.price.toString().concat("0") : dish.price.toString()}</td>
                </tr>
                </tbody>
            </c:forEach>
        </table>
        <br/>
    </c:forEach>
</div>
</body>
</html>
