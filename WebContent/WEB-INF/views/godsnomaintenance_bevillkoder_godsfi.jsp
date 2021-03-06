<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header ===========================-->
<jsp:include page="/WEB-INF/views/headerGodsnoMaintenance.jsp" />
<!-- =====================end header ==========================-->
	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
		specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>
	<SCRIPT type="text/javascript" src="resources/js/godsnomaintenance_bevillkoder_godsfi.js?ver=${user.versionEspedsg}"></SCRIPT>	
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
						<font class="tabLink">Lage bevill.koder</font>						
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
				<table id="containerdatatableTable" cellspacing="1" border="0" align="left">
			    	    <tr>
						<td class="text14">
						<table id="mainList" class="display compact cell-border" >
							<thead>
							<tr>
								<th width="2%" class="tableHeaderField" align="center" ><spring:message code="systema.godsno.maintenance.update"/></th>
								<th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gflbko"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="center" >&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gfenh"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gflbs1"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gflbs2"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gflbs3"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gflbs4"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gfprt"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gffax"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.delete"/>&nbsp;</th>
			                </tr>  
			                </thead>
			                 
			                <tbody >  
				            <c:forEach var="record" items="${model.list}" varStatus="counter">   
				               <tr class="tableRow" height="20" >
				              
				               <td align="center" width="2%" id="recordUpdate_${record.gflbko}_${record.gfenh}" class="tableCellFirst" onClick="getRecord(this);" style="cursor:pointer; border-style: solid;border-width: 0px 1px 1px 0px;border-color:#FAEBD7;" >
				               		<img style="display:block;cursor:pointer;" src="resources/images/update.gif" border="0" alt="edit">
				               </td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gflbko}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="center" ><font class="text14">&nbsp;${record.gfenh}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gflbs1}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gflbs2}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gflbs3}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gflbs4}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gfprt}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gffax}&nbsp;</font></td>
				               <td width="2%" id="recordDelete_${record.gflbko}_${record.gfenh}" class="tableCell" onClick="doDeleteRecord(this);" style="cursor:pointer; border-style: solid;border-width: 0px 1px 1px 0px;border-color:#FAEBD7;" align="center">
				               		<img style="display:block;cursor:pointer;" src="resources/images/delete.gif" width="15px" height="15px" border="0" alt="edit">
				               </td>
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
	 	    <%-- Validation errors --%>
			<spring:hasBindErrors name="record"> <%-- name must equal the command object name in the Controller --%>
			<tr>
				<td width="3%">&nbsp;</td>
				<td width="100%">
	            	<table align="left" border="0" cellspacing="0" cellpadding="0">
	            	<tr >
					<td >					
			            <ul class="isa_error text14" >
			            <c:forEach var="error" items="${errors.allErrors}">
			                <li >
			                	<spring:message code="${error.code}" text="${error.defaultMessage}"/>&nbsp;&nbsp;
			                </li>
			            </c:forEach>
			            </ul>
					</td>
					</tr>
					</table>
				</td>
			</tr>
			</spring:hasBindErrors>
			
			<%-- Other errors (none validation errors) --%>
			<c:if test="${not empty errorMessage}">
			<tr>
				<td width="3">&nbsp;</td>
				<td >
	            	<table align="left" border="0" cellspacing="0" cellpadding="0">
				 		<tr>
				 			<td >
				 				<ul class="isa_error text14" >
                                    <li>${errorMessage}</li>                                    
                                </ul>
				 			</td>
						</tr>
					</table>
				</td>
			</tr>
			</c:if>
			</tr>
	 	    
	 	    <tr >
				<td width="3%">&nbsp;</td>
				<td><button name="newRecordButton" id="newRecordButton" class="inputFormSubmitStd" type="button" ><spring:message code="systema.godsno.maintenance.new"/></button></td>
			</tr>
			<tr height="2"><td>&nbsp;</td></tr>
	 	    <tr >
	 	    	<td width="3%">&nbsp;</td>
				<td >
				<form action="godsnomaintenance_bevillkoder_godsfi_edit.do" name="formRecord" id="formRecord" method="POST" >
					<input type="hidden" name="applicationUser" id="applicationUser" value="${user.user}">
					<input type="hidden" name="language" id="language" value='${user.usrLang}'>
	 	    		<input type="hidden" name="updateId" id=updateId value="${model.updateId}"> 
					<input type="hidden" name="action" id=action value="doUpdate">
					
					<table cellspacing="1" border="0" align="left">
			    	    <tr>
							<td class="text14" title="gflbko">&nbsp;<font class="text14RedBold" >*</font><spring:message code="systema.godsno.maintenance.godsfi.gflbko"/></td>
							<td class="text14" title="gfenh">&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gfenh"/></td>
							<td class="text14" title="gflbs1">&nbsp;<font class="text14RedBold" >*</font><spring:message code="systema.godsno.maintenance.godsfi.gflbs1"/></td>
							<td class="text14" title="gflbs2">&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gflbs2"/></td>
							<td class="text14" title="gflbs3">&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gflbs3"/></td>
							<td class="text14" title="gflbs4">&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gflbs4"/></td>
							<td class="text14" title="gfprt">&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gfprt"/></td>
							<td class="text14" title="gffax">&nbsp;<spring:message code="systema.godsno.maintenance.godsfi.gffax"/></td>
						</tr>
						<tr>
							<td ><input required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" type="text" class="inputTextMediumBlueMandatoryField" name="gflbko" id="gflbko" size="6" maxlength="5" value='${record.gflbko}'></td>
							<td ><input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gfenh" id="gfenh" size="2" maxlength="1" value='${record.gfenh}'></td>
							<td ><input required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" type="text" class="inputTextMediumBlueMandatoryField" name="gflbs1" id="gflbs1" size="25" maxlength="30" value='${record.gflbs1}'></td>
							<td ><input type="text" class="inputTextMediumBlue" name="gflbs2" id="gflbs2" size="25" maxlength="30" value='${record.gflbs2}'></td>
							<td ><input type="text" class="inputTextMediumBlue" name="gflbs3" id="gflbs3" size="25" maxlength="30" value='${record.gflbs3}'></td>
							<td ><input type="text" class="inputTextMediumBlue" name="gflbs4" id="gflbs4" size="25" maxlength="30" value='${record.gflbs4}'></td>
							<td ><input type="text" class="inputTextMediumBlue" name="gfprt" id="gfprt" size="11" maxlength="10" value='${record.gfprt}'></td>
							<td ><input type="text" class="inputTextMediumBlue" name="gffax" id="gffax" size="16" maxlength="15" value='${record.gffax}'></td>
							<td>
								<input class="inputFormSubmit" type="submit" name="submit" id="submit" value='<spring:message code="systema.godsno.maintenance.save"/>'/>
							</td>
						</tr>
						<tr height="5"><td>&nbsp;</td>
						<tr>	
							
						</tr>
						<tr>	
							
						</tr>
						<tr height="3"><td></td>
					</table>
	 	    	</form>
	 	    </tr>
	 	    <tr height="20"><td>&nbsp;</td></tr>
	 		</table>
		</td>
	</tr>
</table>	

<!-- ======================= footer ===========================-->
<jsp:include page="/WEB-INF/views/footer.jsp" />
<!-- =====================end footer ==========================-->

