<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header ===========================-->
<jsp:include page="/WEB-INF/views/headerGodsno.jsp" />
<!-- =====================end header ==========================-->
	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
		specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>	
	<SCRIPT type="text/javascript" src="resources/js/godsno_mainlist.js?ver=${user.versionEspedsg}"></SCRIPT>
	
	<style type = "text/css">
	.ui-datepicker { font-size:9pt;}
	</style>

<table width="100%"  class="text14" cellspacing="0" border="0" cellpadding="0">
	<tr>
	<td>
	<%-- tab container component --%>
	<table width="100%"  class="text14" cellspacing="0" border="0" cellpadding="0">
		<tr height="2"><td></td></tr>
		<tr height="25"> 
			<td width="15%" valign="bottom" class="tab" align="center" nowrap>
				<a style="display:block;" href="godsno_mainlist.do?action=doFind" >
					<img style="vertical-align:middle;" src="resources/images/bulletGreen.png" width="8px" height="8px" border="0" alt="efaktura log">
					<font class="tabLink" ><span id="activeTabList" onMouseOver="showPop('list_info');" onMouseOut="hidePop('list_info');"><spring:message code="systema.godsno.mainlist.tab"/></span></font>
				</a>
				<div class="text14" style="position: relative;" align="left">
                <span style="position:absolute;top:2px;left:50px;" id="list_info" class="popupWithInputText text14"  >
                	Standard antall dager tilbake: <b>${user.dftdg}</b><br/>
					Hvis du ønsker å søke annat, bruk filtret<br/>
				</span>	
				</div>
			</td>
			<%--
			<td width="1px" class="tabFantomSpace" align="center" nowrap><font class="tabDisabledLink">&nbsp;</font></td>
			<td width="20%" valign="bottom" class="tabDisabled" align="center" nowrap>
				<a style="display:block;" href="transportdisp_workflow_getTrip.do?user=${user.user}&tuavd=${searchFilter.avd}&tupro=${searchFilter.tur}">
					<img style="vertical-align:bottom;" src="resources/images/list.gif" border="0" alt="general list">
					<font class="tabDisabledLink"><spring:message code="systema.transportdisp.workflow.trip.tab"/></font>
				</a>
			</td>
			 --%>
			<td width="85%" class="tabFantomSpace" align="center" nowrap><font class="tabDisabledLink">&nbsp;</font></td>
		</tr>
	</table>
	</td>
	</tr>
	
		<tr>
		<td>
			<input type="hidden" name="applicationUser" id="applicationUser" value='${user.user}'>
	
			<%-- this table wrapper is necessary to apply the css class with the thin border --%>
			<table width="100%" class="tabThinBorderWhite" border="0" cellspacing="0" cellpadding="0">
			<tr height="3"><td></td></tr> 
			<tr>
				<td>
				<form name="searchForm" id="searchForm" action="godsno_mainlist.do?action=doFind" method="post" >
				<input type="hidden" name="todo" id="todo" value=''>
				<table id="containerdatatableTable" width="100%" cellspacing="2" align="left">
				<tr>
					<td class="text14" title="avd">&nbsp;Avd</td>
					<td class="text14" title="sign">&nbsp;Signatur</td>
					<td class="text14" title="gogn">&nbsp;Godsnr</td>
					<td class="text14" title="gotrnr">&nbsp;Transittnr.</td>
					<td class="text14" title="goturn">&nbsp;Turnr.</td>
					<td class="text14" title="gobiln">&nbsp;Bilnr.</td>
				</tr>
				<tr>	
					<td class="text14"><input type="text" class="inputText" name="avd" id="avd" size="8" maxlength="6" value='${searchFilter.avd}'></td>
		        	<td class="text14"><input type="text" class="inputText" name="sign" id="sign" size="5" maxlength="4" value='${searchFilter.sign}'></td>
		        	<td class="text14"><input type="text" class="inputText" name="gogn" id="gogn" size="16" maxlength="15" value='${searchFilter.gogn}'></td>
		        	<td class="text14"><input type="text" class="inputText" name="gotrnr" id="gotrnr" size="21" maxlength="20" value='${searchFilter.gotrnr}'></td>
		        	<td class="text14"><input type="text" class="inputText" name="goturn" id="goturn" size="18" maxlength="17" value='${searchFilter.goturn}'></td>
		        	<td class="text14"><input type="text" class="inputText" name="gobiln" id="gobiln" size="14" maxlength="13" value='${searchFilter.gobiln}'></td>
		        	
			        <td class="text14">
			        	<input class="inputFormSubmit" type="submit" name="submit" id="submit" value='Søk'>
			        </td>			
				</tr>
				</form>
				<tr height="2"><td></td></tr>
				</table>
				</td>
			</tr>
			</table>
		</td>
		</tr>	
		
			<tr height="3"><td></td></tr>
			<%-- Validation errors --%>
			<spring:hasBindErrors name="record"> <%-- name must equal the command object name in the Controller --%>
			<tr>
				<td>
		           	<table width="100%" align="left" border="0" cellspacing="0" cellpadding="0">
		           	<tr>
					<td class="textError">					
			            <ul>
			            <c:forEach var="error" items="${errors.allErrors}">
			                <li >
			                	<spring:message code="${error.code}" text="${error.defaultMessage}"/>
			                </li>
			            </c:forEach>
			            </ul>
					</td>
					</tr>
					</table>
				</td>
			</tr>
			</spring:hasBindErrors>	
			
			<tr>
			<td>		
			<table width="100%" cellspacing="0" border="0" cellpadding="0">
		    	<%-- separator --%>
		        <tr height="1"><td></td></tr> 
				<tr>
					<td>
					<table id="containerdatatableTable" width="100%" cellspacing="2" align="left">	
					<tr>
					<td >
					<table id="mainList" class="display compact cell-border" >
						<thead>
						<tr class="tableHeaderField" height="20" >
							<th width="2%" class="tableHeaderFieldFirst">Endre</th>
							<th class="tableHeaderField"><spring:message code="systema.godsno.mainlist.label.godsnr"/></th>   
		                    <th class="tableHeaderField"><spring:message code="systema.godsno.mainlist.label.transittnr"/></th>
		                    <th class="tableHeaderField"><spring:message code="systema.godsno.mainlist.label.turnr"/></th>
		                    <th class="tableHeaderField"><spring:message code="systema.godsno.mainlist.label.bilnr"/></th>
		                    <th width="2%" class="tableHeaderField"><spring:message code="systema.godsno.mainlist.label.avgtollsted.dato"/></th>
		                </tr> 
		                </thead>
		                
		                <tbody>
			            <c:forEach items="${model.list}" var="record" varStatus="counter">    
			             <tr class="tableRow" height="20">  
			               <td align="center" width="2%" class="tableCellFirst" >
			               		<a id="alinkEdit_${counter.count}" style="display:block;" href="godsno_edit.do?gogn=${record.gogn}" onClick="setBlockUI()" >
			               			<img title="Endre post" style="vertical-align:bottom;" src="resources/images/update.gif" border="0" alt="edit">
			               		</a>	
			               </td>
			               <td class="tableCell" >${record.gogn}</td>
			               <td class="tableCell" >${record.gotrnr}</td>
			               <td class="tableCell" >${record.goturn}</td>
			               <td class="tableCell" >${record.gobiln}</td>
			               <td width="2%" class="tableCell" >${record.goavg}</td>
			            </tr> 
			            </c:forEach>
			            </tbody>
		            </table>
					</td>
					</tr>
					
					</table>
					</td>	
				</tr>
			</table>
			</td>
			</tr>
			</table>
		</td>
		</tr>
		<tr height="10"><td></td></tr>
	</table>
		
<!-- ======================= footer ===========================-->
<jsp:include page="/WEB-INF/views/footer.jsp" />
<!-- =====================end footer ==========================-->

