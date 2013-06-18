<%@page import="ru.terra.jbrss.web.security.SessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@include file="/WEB-INF/jsp/header.jsp"%>
    <!-- B. MAIN -->
    <div class="main">
 
      <!-- B.1 MAIN NAVIGATION -->
      <div class="main-navigation">
        <!-- Template infos -->                
        <h1>Последние посты</h1>
        <div id="last_posts">        
        </div>  
      </div>
 
      <!-- B.2 MAIN CONTENT -->
	<div class="main-content">

		<!-- Pagetitle -->
		<h1 class="pagetitle">Контакты</h1>
		<div>
			<!-- Content unit - One column -->
			<div class="column1-unit">
				<h1>Информация о контактах</h1>
				<h3>JBRss</h3>
				<p>тут контакты</p>
			</div>
			<hr class="clear-contentunit" />
		</div>
	</div>
	<!-- B.3 SUBCONTENT -->
      <div class="main-subcontent">

        <!-- Subcontent unit -->
        <div class="subcontent-unit-border">
          <div class="round-border-topleft"></div><div class="round-border-topright"></div>
          <h1>Последние обновления</h1>
          <p>Сайт отктыт</p>
        </div>

        <!-- Subcontent unit -->
        <div class="subcontent-unit-border-orange">
          <div class="round-border-topleft"></div><div class="round-border-topright"></div>
          <h1 class="orange">Android client</h1>
          <p>Android клиент можно скачать <a href="resources/apk/jbrss.apk">тут</a></p>
        </div>

<!--         Subcontent unit -->
<!--         <div class="subcontent-unit-border-green"> -->
<!--           <div class="round-border-topleft"></div><div class="round-border-topright"></div> -->
<!--           <h1 class="green">It's free!</h1> -->
<!--           <p>Enjoy the template for free. There are no restrictions in the license. As a sign of appreciation, please keep the author credits "<a href="http://www.1-2-3-4.info">Design by G. Wolfgang</a>" in the template footer. Thanks!</p> -->
<!--         </div> -->
      </div>
    </div>
<%@include file="/WEB-INF/jsp/footer.jsp"%>      
 