<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Eating Poll</title>
</head>
<body>
<div align="center">
    <h2>Eating Poll</h2>
    <h2>New User</h2>
    <form:form action="save" method="post" modelAttribute="user">
        <table border="0" cellpadding="5">
            <tr>
                <td>Name: </td>
                <td><form:input path="name" value="${user.name}" /></td>
            </tr>
            <tr>
                <td>Email: </td>
                <td><form:input path="email" value="${user.email}" /></td>
            </tr>
            <tr>
                <td>Password: </td>
                <td><form:password path="password" value="${user.password}" /></td>
                <td><form:hidden path="role" value="${user.role}"/></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="Save"></td>
            </tr>
        </table>
    </form:form>
</div>

</body>
</html>