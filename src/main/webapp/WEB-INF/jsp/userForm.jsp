<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Eating Poll</title>
    <style>
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<div align="center">
    <div class="form-style-2">
        <h2>Eating Poll</h2>
        <div class="form-style-2-heading">
            <h2>${user.id == null ? "New Profile" : "Update Profile"}</h2>
        </div>
        <form:form action="${pageContext.request.contextPath}/save" method="post" modelAttribute="user">
            <table border="0" cellpadding="5">
                <tr>
                    <td>${user.id == null ? "Name" : "Set new name:"}</td>
                    <td><form:input path="name" value="${user.name}"/></td>
                    <td><form:hidden path="id" value="${user.id}"/></td>
                    <td><form:hidden path="role" value="${user.role}"/></td>
                </tr>
                <tr ${user.id != null ? "hidden" : ""}>
                    <td>Email:</td>
                    <td><form:input path="email" value="${user.email}"/></td>
                </tr>
                <tr>
                    <td>${user.id == null ? "Password:" : "Set new password:"}</td>
                    <td><form:password path="password" value="${user.password}"/></td>
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