<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>Eating Poll</title>
    <style>
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<div align="center">
    <h1>Eating Poll</h1>
    <p><a href="${pageContext.request.contextPath}/admin/home?userEmail=${userEmail}&userPassword=${userPassword}">Back
        to main page</a></p>
    <br/>
    <div class="form-style-2-heading">
        <h3>All registered users:</h3>
    </div>
    <table border="1" cellpadding="5" class="zui-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.role}</td>
                <td>
                    <a ${(user.id == 1 || user.id == 2 || user.id == 3) ? "hidden" : ""}
                            href="${pageContext.request.contextPath}/admin/users/delete?userId=${user.id}&userEmail=${userEmail}&userPassword=${userPassword}">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <p>Note: You can't delete mock users. If you want to test "delete" action create a new account</p>
</div>
</body>
</html>