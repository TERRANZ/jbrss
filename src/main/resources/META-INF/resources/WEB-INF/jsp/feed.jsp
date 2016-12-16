<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/jsp/inc/header.jsp"/>
<div id="feedpage" data-role="page" is="page">
    <div role="main" class="ui-content" is="content">
        <a class="ui-btn" href="#mainform">Назад</a>
        <div id="messages">
            <c:forEach items="${data}" var="fp" varStatus="loopStatus">
                <div class="column1-unit">
                    <a href="#" onclick="window.open('<c:out value="${fp.postlink}"/>');">
                        <h1><c:out value="${fp.posttitle}"/></h1>
                    </a>
                    <h3><c:out value="${fp.posttitle}"/></h3>
                    <p><c:out value="${fp.posttext}"/></p>
                    <ul class="social-buttons cf">
                        <li><a href="https://plus.google.com/share?url=<c:out value="${fp.postlink}"/>" class="socialite googleplus-one" data-size="tall" data-href="<c:out value="${fp.postlink}"/>" rel="nofollow" target="_blank"><span class="vhidden">Share on Google+</span></a></li>
                    </ul>
                </div>
                <hr class="clear-contentunit">
            </c:forEach>
        </div>
        <a class="ui-btn" href='/feed?id=<c:out value="${id}"/>&?page=<c:out value="${page}"/>'>Дальше</a>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/inc/footer.jsp"/>