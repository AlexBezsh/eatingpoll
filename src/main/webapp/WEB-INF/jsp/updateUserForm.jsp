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
        <h1>Eating Poll</h1>
        <div class="form-style-2-heading">
            <h2>Update Profile</h2>
        </div>
        <form:form action="${pageContext.request.contextPath}/update" method="post" modelAttribute="user">
            <table border="0" cellpadding="5">
                <tr>
                    <td>New name:</td>
                    <td><form:input path="name" value="${user.name}"/></td>
                </tr>
                <tr hidden>
                    <td><form:input path="email" value="${user.email}"/></td>
                </tr>
                <tr>
                    <td>New password:</td>
                    <td><form:password path="password" value="${user.password}"/></td>
                </tr>
                <tr>
                    Enter previous name or password if you don't want to change it
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