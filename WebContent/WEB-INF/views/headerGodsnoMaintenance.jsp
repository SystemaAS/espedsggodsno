<!DOCTYPE html>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %> <!-- generally you will include this in a header.jsp -->

<html>
	<head>
		<link href="/espedsg2/resources/${user.cssEspedsgMaintenance}?ver=${user.versionEspedsg}" rel="stylesheet" type="text/css"/>
		<link href="resources/jquery.calculator.css" rel="stylesheet" type="text/css"/>
		<link type="text/css" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/overcast/jquery-ui.css" rel="stylesheet">
		<%--<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.0/themes/smoothness/jquery-ui.css" rel="stylesheet"> --%>
		
		<%-- datatables grid CSS --%>
		<link type="text/css" href="//cdn.datatables.net/1.10.11/css/jquery.dataTables.css" rel="stylesheet">
		
		<c:choose>
			<c:when test="${ fn:contains(user.cssEspedsg, 'Toten') }"> 
				<link rel="SHORTCUT ICON" type="image/ico" href="resources/images/toten_ico.ico"></link>
			</c:when>
			<c:otherwise>
				<link rel="SHORTCUT ICON" type="image/png" href="resources/images/systema_logo.png"></link>
			</c:otherwise>
		</c:choose>
		<%-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> --%>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE" />
		<title>eSpedsg - TVINN</title>
	</head>
	<body>
	<%-- include som javascript functions --%>
	<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
	<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js""></script>
	<script type="text/javascript" src="resources/js/jquery.blockUI.js"></script>
	<script type="text/javascript" src="resources/js/systemaWebGlobal.js?ver=${user.versionEspedsg}"></script>

	<%--datatables grid --%>
	<script type="text/javascript" src="//cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
	
    <table class="noBg" width="100%" border="0" cellspacing="0" cellpadding="0">
		<%--Banner --%>
	 	<tr>
	 		 <%-- class="grayTitanBg" --%>
    		<td height="60" class="headerTdsBannerAreaBg" width="100%" align="left" colspan="3"> 
    			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
    			 	<tr>
			        	<td>&nbsp;</td>
			        	<td>&nbsp;</td>
				 		<td>&nbsp;</td>
			        </tr>
				 	<tr>
				 		<td class="text12white" width="10%" align=left valign="bottom" >&nbsp;</td>
				 		<td class="text32Bold" width="100%" align="center" valign="middle" style="color:#778899;" >
				 			eSped<font style="color:#003300;">sg</font> - Godsreg. - VEDLIKEHOLD
				 			
				 		</td>
				 		 
			    		<td class="text12" width="10%" align="center" valign="middle" ><img src="resources/images/systema_logo.png" border="0" width=80px height=50px ></td>
			      		<%-- <td class="text12white" width="10%" align=right valign="bottom" >&nbsp;</td>--%>
			        </tr>
			        <tr>
			        	<td>&nbsp;</td>
			        	<td>&nbsp;</td>
				 		<td class="text14" width="10%" align=right valign="bottom" >&nbsp;</td>
			        </tr>
			        <tr class="text" height="1"><td></td></tr>
			     </table> 
			</td>
		</tr>
		<%-- Header menu --%>
		<tr >
			<td height="22" class="tabThinBorderLightGray" width="100%" align="left" colspan="3"> 
    			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
				 	<tr >
			    		<td class="text14" width="70%" align="left" >&nbsp;&nbsp;
			    			<%-- --------------------- --%>
			    			<%-- TOPIC ONE MENU        --%>
			    			<%-- --------------------- --%>
			    			<a id="alinkTopicOne" tabindex=-1 href="todo.do?">
			    				&nbsp;<font 
			    				<c:choose>           
		                   			<c:when test="${user.activeMenu=='GODSREGNO_MAINTENANCE_ONE'}">
		                       			class="headerMenuOrange"
		                   			</c:when>
		                   			<c:otherwise>   
		                       			class="headerMenuWhite"
		                   			</c:otherwise>
		               			</c:choose>
			    				
			    				>&nbsp;Lagerbevilln.koder&nbsp;</font>
			    			</a>
			    			
			    			&nbsp;<font color="#FF6600"; style="font-weight: bold;">|</font>
			    			
			    			<%-- --------------------- --%>
			    			<%-- TOPIC TWO  MENU       --%>
			    			<%-- --------------------- --%>
			    			<a id="alinkTopicTwo" tabindex=-1 href="todo.do?">
			    				&nbsp;<font 
			    				<c:choose>           
	                   			<c:when test="${user.activeMenu=='GODSREGNO_MAINTENANCE_TWO'}">
	                       			class="headerMenuOrange"
	                   			</c:when>
	                   			<c:otherwise>   
	                       			class="headerMenuWhite"
	                   			</c:otherwise>
	               			</c:choose>
			    				
		    				>&nbsp;Låse avd/bev.kode&nbsp;</font>
			    			</a>
			    			&nbsp;<font color="#FF6600"; style="font-weight: bold;">|</font>
			    			
		    			 	<%-- ------------------- --%>
			    			<%-- Maintenance  MENU    --%>
			    			<%-- -------------------- --%>
			    			<a id="alinkGodsno" tabindex=-1 href="godsno_mainlist.do?action=doFind">
			    				&nbsp;<font class="headerMenuMaintenance">
		                   		&nbsp;&nbsp;Hovedmeny for Godsregistrering&nbsp;&nbsp;</font>
			    			</a>
	      				</td>		      				
	      				<td class="text14" width="50%" align="right" valign="middle">
						    <img valign="bottom" src="resources/images/countryFlags/Flag_NO.gif" height="12" border="0" alt="country">
		      				&nbsp;
		      				<font class="headerMenuWhite">
			    				<img src="resources/images/appUser.gif" border="0" onClick="showPop('specialInformationAdmin');" > 
						        <font style="color:#000000" >${user.user}&nbsp;</font><font style="color:#FF6600" >${user.usrLang}</font>
			    			</font>
			    				
		    				<font color="#FF6600"; style="font-weight: bold;">&nbsp;&nbsp;|&nbsp;&nbsp;</font>
			    			<div class="text11" style="position: relative;display: inline;" align="left">
								<span style="position:absolute; left:-150px; top:3px; width:100;" id="specialInformationAdmin" class="popupWithInputText"  >
					           		<div class="text11" align="left">
					           			${activeUrlRPG_TvinnSad}
					           			<br/><br/>
					           			<button name="specialInformationButtonClose" class="buttonGrayInsideDivPopup" type="button" onClick="hidePop('specialInformationAdmin');">Close</button> 
					           		</div>
						        </span>   	

			    			<a tabindex=-1 href="logout.do">
			    				<font class="headerMenuWhite"><img src="resources/images/home.gif" border="0">&nbsp;
			    					<font style="color:#000000;" ><spring:message code="dashboard.menu.button"/>&nbsp;</font>
			    				</font>
			    			</a>
			    			<font color="#FF6600"; style="font-weight: bold;">&nbsp;&nbsp;|&nbsp;</font>
			    			<font class="text12" style="cursor:pointer;" onClick="showPop('versionInfo');">${user.versionSpring}&nbsp;</font>
		    				    <div class="text11" style="position: relative;display: inline;" align="left">
								<span style="position:absolute; left:-150px; top:3px; width:100;" id="versionInfo" class="popupWithInputText"  >
					           		<div class="text11" align="left">
					           			&nbsp;<b>${user.versionEspedsg}</b>
					           			<br/><br/>
					           			&nbsp;<a href="renderLocalLog4j.do" target="_blank"><font class="text12Orange">log4j</font></a>
					           			<br/><br/><br/>
					           			<button name="versionInformationButtonClose" class="buttonGrayInsideDivPopup" type="button" onClick="hidePop('versionInfo');">Close</button> 
					           		</div>
					        	</span>
					        	</div> 
			    		</td>
			        </tr>
			     </table> 
			</td>
	    </tr>
	    <tr>
		    <td height="4" class="tabThinBorderOrange" width="100%" align="left" colspan="3"> 
	   			 <table width="1200" border="0" cellspacing="0" cellpadding="0">
	   			 </table>
			</td>
	    </tr>
	   
	    <tr class="text" height="2"><td></td></tr>
		
		
		<%-- ------------------------------------
		Content after banner och header menu
		------------------------------------- --%>
		<tr>
    		<td width="100%" align="left" colspan="3"> 
    		     
     