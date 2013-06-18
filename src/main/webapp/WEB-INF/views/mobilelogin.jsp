<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="/jbrss/resources/css/layout4_setup.css" />
  <link rel="stylesheet" type="text/css" media="screen,projection,print" href="/jbrss/resources/css/layout4_text.css" />
  <title>Мобильный вход</title>
  <link rel="stylesheet" href="https://d10ajoocuyu32n.cloudfront.net/mobile/1.3.1/jquery.mobile-1.3.1.min.css">

  <!-- Extra Codiqa features -->
  <link rel="stylesheet" href="/jbrss/resources/css/codiqa.ext.css">
</head>
<%@include file="/WEB-INF/jsp/jsinclude.jsp"%>
<script src="https://d10ajoocuyu32n.cloudfront.net/mobile/1.3.1/jquery.mobile-1.3.1.min.js"></script>
  <!-- Extra Codiqa features -->
  <script src="https://d10ajoocuyu32n.cloudfront.net/codiqa.ext.js"></script>
<body>
<!-- Home -->

<div data-role="page" id="page1">
    <div data-role="content">
        <form action="/jbrss/do.login" method="POST" data-ajax="false">
            <div data-role="fieldcontain">
                <label for="textinput1">
                    Логин
                </label>
                <input name="j_username" id="j_username" placeholder="" value="" type="text">
            </div>
            <div data-role="fieldcontain">
                <label for="textinput2">
                    Пароль
                </label>
                <input name="j_password" id="j_password" placeholder="" value="" type="password">
            </div>
            <input type="button" data-icon="check" data-iconpos="left" value="Вход" onclick="userLogin();">
        </form>
    </div>
</div>

<script src="/jbrss/resources/js/mobile.js"></script>  
</body>
</html>l>