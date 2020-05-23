<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Eating Poll</title>
</head>
<body>
<h2>Eating Poll</h2>
<p><a href="${pageContext.request.contextPath}/">Back to profile options</a></p>

<c:forEach items="${restaurants}" var="restaurant">
    <br/>
    <h4>${restaurant.name}</h4>
    <p>Number of Votes: ${restaurant.votesCount} <a
            href="${pageContext.request.contextPath}/vote?restaurantId=${restaurant.id}&userId=${user.id}">Choose</a>
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
</body>
</html>
