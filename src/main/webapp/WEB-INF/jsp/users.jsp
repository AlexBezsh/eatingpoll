<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>Eating Poll</title>
    <style><%@include file="/WEB-INF/css/styles.css" %></style>
</head>
<body>
<div align="center">
    <h1>Eating Poll</h1>
    <p><a href="${pageContext.request.contextPath}/admin/home">Back to main page</a></p>
    <br/>
    <div class="form-style-2-heading">
        <h3>All registered users:</h3>
    </div>
    <table border="1" cellpadding="5" class="zui-table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.role}</td>
                <td>
                    <a ${(user.email.equals("user1@gmail.com") || user.email.equals("user2@gmail.com")|| user.email.equals("admin1@gmail.com")) ? "hidden" : ""}
                            href="${pageContext.request.contextPath}/admin/users/delete/${user.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <p>Note: You can't delete mock users. If you want to test "delete" action create a new account</p>
</div>
</body>
</html>