<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Eating Poll</title>
</head>
<body>
<h1>Eating Poll</h1>
<h2><a href="${pageContext.request.contextPath}/user/create">Sigh up</a></h2>
<h3>Log in:</h3>
<form:form action="user/login" modelAttribute="user">
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
<br/>
<h4>Choose mock profile:</h4>
<c:forEach items="${users}" var="user">
    <li><a href="${pageContext.request.contextPath}/voting?userId=${user.id}">${user.name} - Role: ${user.role}</a></li>
</c:forEach>
<br/>
<h4>Sigh up as admin:</h4>
<form:form action="user/create" modelAttribute="user">
    <table border="0" cellpadding="5">
        <tr>
            <td>Password:</td>
            <td><form:password path="password"/></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="Sigh up"></td>
        </tr>
    </table>
</form:form>
</body>
</html>
