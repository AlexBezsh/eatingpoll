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
    <h1>Eating Poll</h1>
    <h4>${pageContext.request.getAttribute("message") != null ? pageContext.request.getAttribute("message") : ""}</h4>
    <h2><a href="${pageContext.request.contextPath}/register">Sigh up</a></h2>
    <div class=form-style-2>
        <div class="form-style-2-heading"><h3>Log in:</h3></div>
        <form:form action="${pageContext.request.contextPath}/login" modelAttribute="userToLogin">
            <table border="0" cellpadding="5">
                <tr>
                    <td>Email:</td>
                    <td><form:input path="email"/></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><form:password path="password"/></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Log in"></td>
                </tr>
            </table>
        </form:form>
    </div>
    <br/>
    <div class="form-style-2-heading">
        <h4>Choose mock profile:</h4>
        <c:forEach items="${users}" var="user">
            <li><a href="${pageContext.request.contextPath}/mock/login?email=${user.email}">${user.name} - Role: ${user.role}</a></li>
        </c:forEach>
    </div>
</div>
</body>
</html>
