<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Eating Poll</title>
    <style><%@include file="/WEB-INF/css/styles.css" %></style>
</head>
<body>
<div align="center">
    <h2>Eating Poll</h2>
    <div class="form-style-2">
        <div class="form-style-2-heading"><h2>${dish.id == null ? "New Dish" : "Update Dish"}</h2></div>
        <form:form action="${pageContext.request.contextPath}/admin/dish/save" method="post" modelAttribute="dish">
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
</div>
</body>
</html>