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
    <h4>User: ${user.name}. Role: ${user.role} <a href="${pageContext.request.contextPath}/admin/update">Update profile</a></h4>
    <p><a href="${pageContext.request.contextPath}/admin/users">All users</a></p>
    <h4><a href="${pageContext.request.contextPath}/admin/discard">Discard Results of Voting</a></h4>
    <div class="form-style-2">
        <form:form action="${pageContext.request.contextPath}/admin/restaurant/save" method="post" modelAttribute="restaurant">
            <table border="0" cellpadding="5">
                <tr>
                    <td>Add New Restaurant:</td>
                    <td><form:input path="name"/></td>
                    <td colspan="2"><input type="submit" value="Save"></td>
                </tr>
            </table>
        </form:form>
    </div>
    <div class="form-style-2-heading"><h2>Restaurants:</h2></div>
    <c:forEach items="${restaurants}" var="restaurant">
        <h4>${restaurant.name}
            <a href="${pageContext.request.contextPath}/admin/restaurant/delete?restaurantId=${restaurant.id}">(delete)</a>
            <a href="${pageContext.request.contextPath}/admin/dish/create?restaurantId=${restaurant.id}">(add dish)</a>
        </h4>
        <div class="form-style-2">
            <form:form action="${pageContext.request.contextPath}/admin/restaurant/update" method="post" modelAttribute="restaurant">
                <table border="0" cellpadding="5">
                    <tr>
                        <td>Set new name:</td>
                        <td><form:hidden path="id" value="${restaurant.id}"/><form:input path="name"/></td>
                        <td colspan="2"><input type="submit" value="Save"></td>
                    </tr>
                </table>
            </form:form>
        </div>
        <p>Number of Votes: ${restaurant.votesCount} <a href="${pageContext.request.contextPath}/admin/vote?restaurantId=${restaurant.id}">Choose</a></p>
        <table border="1" cellpadding="5" class="zui-table">
            <thead>
            <tr>
                <th>Name</th>
                <th>Price</th>
                <th>Action</th>
            </tr>
            </thead>
            <c:forEach items="${restaurant.dishes}" var="dish">
                <tbody>
                <tr>
                    <td>${dish.name}</td>
                    <td>${dish.price.toString().matches("\\d+\\.\\d{1}") ? dish.price.toString().concat("0") : dish.price.toString()}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/dish/update?dishId=${dish.id}">Update</a>
                        <a href="${pageContext.request.contextPath}/admin/dish/delete?dishId=${dish.id}">Delete</a>
                    </td>
                </tr>
                </tbody>
            </c:forEach>
        </table>
        <br/>
        <br/>
    </c:forEach>
</div>
</body>
</html>
