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
    <br/>
    <h4>Add New Restaurant:<h4/>
        <form:form action="restaurant/save?userId=${user.id}" method="post" modelAttribute="restaurant">
            <table border="0" cellpadding="5">
                <tr>
                    <td>Name:</td>
                    <td><form:input path="name"/></td>
                    <td colspan="2"><input type="submit" value="Save"></td>
                </tr>
            </table>
        </form:form>
        <br/>
        <a href="${pageContext.request.contextPath}/discard?userId=${user.id}">Discard Results of Voting</a></h4>

    <c:forEach items="${restaurants}" var="restaurant">
        <br/>
        <h4>${restaurant.name}
            <a href="${pageContext.request.contextPath}/restaurant/delete?restaurantId=${restaurant.id}&userId=${user.id}">(delete)</a>
            <a href="${pageContext.request.contextPath}/dish/create?restaurantId=${restaurant.id}&userId=${user.id}">(add
                dish)</a>
        </h4>
        <form:form action="restaurant/update?userId=${user.id}" method="post" modelAttribute="restaurant">
            <table border="0" cellpadding="5">
                <tr>
                    <td>Set new name:</td>
                    <td><form:hidden path="id" value="${restaurant.id}"/><form:input path="name"/></td>
                    <td colspan="2"><input type="submit" value="Save"></td>
                </tr>
            </table>
        </form:form>
        <p>Number of Votes: ${restaurant.votesCount} <a
                href="${pageContext.request.contextPath}/vote?restaurantId=${restaurant.id}&userId=${user.id}">Choose</a>
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
                    <td>${dish.price}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/dish/update?dishId=${dish.id}&userId=${user.id}">Update</a>

                        <a href="${pageContext.request.contextPath}/dish/delete?dishId=${dish.id}&userId=${user.id}">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p></p>
        <p></p>
    </c:forEach>
</div>
</body>
</html>
