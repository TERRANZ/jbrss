<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="resources/css/layout4_setup.css" />
  <link rel="stylesheet" type="text/css" media="screen,projection,print" href="resources/css/layout4_text.css" />
  <title></title>
  <link rel="stylesheet" href="https://d10ajoocuyu32n.cloudfront.net/mobile/1.3.1/jquery.mobile-1.3.1.min.css">

  <!-- Extra Codiqa features -->
  <link rel="stylesheet" href="resources/css/codiqa.ext.css">
</head>
<%@include file="/WEB-INF/jsp/jsinclude.jsp"%>
<script src="https://d10ajoocuyu32n.cloudfront.net/mobile/1.3.1/jquery.mobile-1.3.1.min.js"></script>
  <!-- Extra Codiqa features -->
  <script src="https://d10ajoocuyu32n.cloudfront.net/codiqa.ext.js"></script>
<body>
<!-- Home -->
<div data-role="page" id="page1">
    <div data-role="content">
        <div data-role="collapsible-set">
            <div data-role="collapsible" data-collapsed="false" id="feeds_collapsable">
                <h3>
                    Feeds
                </h3>
                <ul data-role="listview" data-divider-theme="b" data-inset="true" id="feeds">             
              
                </ul>
            </div>
            <div data-role="collapsible" id="messages_collapsable">
                <h3>
                    Messages
                </h3>
                <div id="messages"></div>
            </div>
        </div>
    </div>
</div>
<script src="resources/js/mobile.js"></script>  
</body>
</html>