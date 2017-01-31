<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/jsp/inc/header.jsp"/>
<div id="mainform" data-role="page" is="page">
    <div role="main" class="ui-content" is="content">
        <a class="ui-btn" href="#addpage">Добавить</a>
        <a class="ui-btn" onclick="update();">Update</a>

        <div class="ui-field-contain">
            <label for="search">Поиск</label>
            <input type="text" name id="search">
        </div>
        <a class="ui-btn" onclick="doSearch();">Поиск</a>
        <ul data-role="listview" data-inset="true" data-divider-theme="b" id="feeds" class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
            <c:forEach items="${data}" var="feed" varStatus="loopStatus">
                <li>
                    <a id='1' href='/feed?id=<c:out value="${feed.id}"/>' data-transition='slide' class='ui-btn ui-btn-icon-right ui-icon-carat-r'><c:out value="${feed.feedname}"/></a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/inc/footer.jsp"/>