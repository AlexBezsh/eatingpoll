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
        <a href="${pageContext.request.contextPath}/admin/update?userEmail=${user.email}&userPassword=${user.password}">Update profile</a>
        <a ${user.id == 3 ? "hidden" : ""} href="${pageContext.request.contextPath}/admin/delete?userId=${user.id}">Delete this profile</a>
    </h4>
    <p><a href="${pageContext.request.contextPath}/admin/users?userEmail=${user.email}&userPassword=${user.password}">All users</a></p>
    <form:form
            action="${pageContext.request.contextPath}/admin/restaurant/save?userEmail=${user.email}&userPassword=${user.password}"
            method="post" modelAttribute="restaurant">
        <table border="0" cellpadding="5">
            <tr>
                <td>Add New Restaurant:</td>
                <td><form:input path="name"/></td>
                <td colspan="2"><input type="submit" value="Save"></td>
            </tr>
        </table>
    </form:form>
    <h4>
        <a href="${pageContext.request.contextPath}/admin/discard?userEmail=${user.email}&userPassword=${user.password}">Discard Results of Voting</a>
    </h4>
    <h3>Restaurants:</h3>
    <c:forEach items="${restaurants}" var="restaurant">
        <h4>${restaurant.name}
            <a href="${pageContext.request.contextPath}/admin/restaurant/delete?restaurantId=${restaurant.id}&userEmail=${user.email}&userPassword=${user.password}">(delete)</a>
            <a href="${pageContext.request.contextPath}/admin/dish/create?restaurantId=${restaurant.id}&userEmail=${user.email}&userPassword=${user.password}">(add dish)</a>
        </h4>
        <form:form
                action="${pageContext.request.contextPath}/admin/restaurant/update?userEmail=${user.email}&userPassword=${user.password}"
                method="post" modelAttribute="restaurant">
            <table border="0" cellpadding="5">
                <tr>
                    <td>Set new name:</td>
                    <td><form:hidden path="id" value="${restaurant.id}"/><form:input path="name"/></td>
                    <td colspan="2"><input type="submit" value="Save"></td>
                </tr>
            </table>
        </form:form>
        <p>Number of Votes: ${restaurant.votesCount}
            <a href="${pageContext.request.contextPath}/admin/vote?restaurantId=${restaurant.id}&userEmail=${user.email}&userPassword=${user.password}">Choose</a>
        </p>
        <table border="1" cellpadding="5">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Action</th>
            </tr>
            <c:forEach items="${restaurant.dishes}" var="dish">
                <tr>
                    <td>${dish.id}</td>
                    <td>${dish.name}</td>
                    <td>${dish.price.toString().matches("\\d+\\.\\d{1}") ? dish.price.toString().concat("0") : dish.price.toString()}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/dish/update?dishId=${dish.id}&userEmail=${user.email}&userPassword=${user.password}">Update</a>
                        <a href="${pageContext.request.contextPath}/admin/dish/delete?dishId=${dish.id}&userEmail=${user.email}&userPassword=${user.password}">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <br/>
    </c:forEach>
</div>
</body>
</html>
