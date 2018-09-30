<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header ===========================-->
<jsp:include page="/WEB-INF/views/headerGodsnoMaintenance.jsp" />
<!-- =====================end header ==========================-->
	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
		specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>
	<SCRIPT type="text/javascript" src="resources/js/godsnomaintenance_godshf.js?ver=${user.versionEspedsg}"></SCRIPT>	
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
	
	<style type = "text/css">
		.ui-dialog{font-size:11pt;}
		.ui-datepicker { font-size:9pt;}
	</style>


<table width="100%" class="text11" cellspacing="0" border="0" cellpadding="0">
	<tr height="15"><td>&nbsp;</td></tr>
	<tr>
		<td>
		<%-- tab container component --%>
		<table style="width:100%" class="text11" cellspacing="0" border="0" cellpadding="0">
			<tr height="2"><td></td></tr>
				<tr height="25"> 
					<td width="20%" valign="bottom" class="tabDisabled" align="center" nowrap>
						<a style="display:block;" id="alinkRecordId" onClick="setBlockUI(this);" href="godsnomaintenance.do">
							<img style="vertical-align: middle;"  src="resources/images/list.gif" border="0" alt="general list">
							<font class="tabDisabledLink">&nbsp;Vedlikehold</font>&nbsp;
						</a>
					</td>
					<td width="20%" valign="bottom" class="tab" align="center">
						<img style="vertical-align: middle;"  src="resources/images/bulletGreen.png" border="0" width="10px" height="10px" alt="db table">
						<font class="tabLink">Hendelseslogg</font>						
					</td>
					<td width="80%" class="tabFantomSpace" align="center" nowrap><font class="tabDisabledLink">&nbsp;</font></td>	
				</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<%-- space separator --%>
	 		<table style="width:100%" class="tabThinBorderWhite" border="0" cellspacing="0" cellpadding="0">
	 		
	 	    <tr height="20"><td>&nbsp;</td></tr>
	 	    
			<%-- list component --%>
			<tr>
				<td width="3%">&nbsp;</td>
					
				<td >
				<table id="containerdatatableTable" style="width:60%" cellspacing="1" border="0" align="left">
			    	    <tr>
						<td class="text14">
						<table id="mainList" class="display compact cell-border" >
							<thead>
							<tr>
								<th class="tableHeaderFieldFirst" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gogn"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gotrnr"/>&nbsp;</th>
			                    <th width="2%" class="tableHeaderField" align="center"  >&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohpgm"/>&nbsp;</th>
			                    <th width="2%" class="tableHeaderField" align="center" >&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohdat"/>&nbsp;</th>
			                    <th width="2%" class="tableHeaderField" align="center" >&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohtim"/>&nbsp;</th>
			                    <th width="2%" class="tableHeaderField" align="center" >&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohusr"/>&nbsp;</th>
			                    <th width="2%" class="tableHeaderField" align="center" >&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohkod"/>&nbsp;</th>
			                </tr>  
			                </thead>
			                 
			                <tbody >  
				            <c:forEach var="record" items="${list}" varStatus="counter">   
				               <tr class="tableRow" height="20" >
				              
				               <td class="tableCellFirst" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gogn}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gotrnr}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gohpgm}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gohdat}&nbsp;</font></td>
				               <td width="2%" class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="center" ><font class="text14">&nbsp;${record.gohtim}&nbsp;</font></td>
				               <td width="2%" class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="center" ><font class="text14">&nbsp;${record.gohusr}&nbsp;</font></td>
				               <td width="2%" class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="center" ><font class="text14">&nbsp;${record.gohkod}&nbsp;</font></td>
				               
				            </tr> 
				            </c:forEach>
				            </tbody>
			            </table>
					</td>	
					</tr>
				</table>
				</td>
			</tr>
	 	    
	 	    <tr height="20"><td>&nbsp;</td></tr>
	 		</table>
		</td>
	</tr>
</table>	

<!-- ======================= footer ===========================-->
<jsp:include page="/WEB-INF/views/footer.jsp" />
<!-- =====================end footer ==========================-->

