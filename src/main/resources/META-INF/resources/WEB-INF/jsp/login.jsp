<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/jsp/inc/header.jsp"/>

<!-- Page: login -->
<div id="login" data-role="page">
    <div role="main" class="ui-content">
        <form:form method="post" action="/login">
            <a class="ui-btn" href="#reg">Регистрация</a>

            <div class="ui-field-contain">
                <label for="UserName">Логин</label>
                <input type="text" name="UserName" id="UserName">
            </div>
            <div class="ui-field-contain">
                <label for="Password">Пароль</label>
                <input type="password" name="Password" id="Password">
            </div>
            <input type="submit" class="ui-btn" value="Вход"/>
        </form:form>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/inc/footer.jsp"/>