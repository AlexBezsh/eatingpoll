<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Eating Poll</title>
</head>
<body>
<div align="center">
    <h1>Eating Poll</h1>
    <p><a href="${pageContext.request.contextPath}/">Back to profile options</a></p>
    <h4>User: ${user.name}. Role: ${user.role}
        <a href="${pageContext.request.contextPath}/user/update?userEmail=${user.email}&userPassword=${user.password}">Update profile</a>
        <a ${(user.id == 1 || user.id == 2) ? "hidden" : ""} href="${pageContext.request.contextPath}/user/delete?userId=${user.id}&userEmail=${user.email}&userPassword=${user.password}">Delete this profile</a>
    </h4>
    <h3>Restaurants:</h3>
    <c:forEach items="${restaurants}" var="restaurant">
        <h4>${restaurant.name}</h4>
        <p>Number of Votes: ${restaurant.votesCount} <a
                href="${pageContext.request.contextPath}/user/vote?restaurantId=${restaurant.id}&userEmail=${user.email}&userPassword=${user.password}">Choose</a>
        </p>
        <table border="1" cellpadding="5">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
            </tr>
            <c:forEach items="${restaurant.dishes}" var="dish">
                <tr>
                    <td>${dish.id}</td>
                    <td>${dish.name}</td>
                    <td>${dish.price}</td>
                </tr>
            </c:forEach>
        </table>
    </c:forEach>
</div>
</body>
</html>
