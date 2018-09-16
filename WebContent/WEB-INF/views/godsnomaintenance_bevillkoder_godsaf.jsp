<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header ===========================-->
<jsp:include page="/WEB-INF/views/headerGodsnoMaintenance.jsp" />
<!-- =====================end header ==========================-->
	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
		specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>
	<SCRIPT type="text/javascript" src="resources/js/godsnomaintenance_bevillkoder_godsaf.js?ver=${user.versionEspedsg}"></SCRIPT>	
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
						<a id="alinkRecordId" onClick="setBlockUI(this);" href="godsnomaintenance_bevillkoder.do">
							<img style="vertical-align: middle;"  src="resources/images/list.gif" border="0" alt="general list">
							<font class="tabDisabledLink">&nbsp;Bevill.koder - Vedlikehold</font>&nbsp;
						</a>
					</td>
					<td width="20%" valign="bottom" class="tab" align="center">
						<img style="vertical-align: middle;"  src="resources/images/bulletGreen.png" border="0" width="10px" height="10px" alt="db table">
						<font class="tabLink">Låse avd/bevill.koder</font>						
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
								<th width="2%" class="tableHeaderField" align="center" ><spring:message code="systema.godsno.maintenance.update"/></th>
								<th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsaf.gflavd"/>&nbsp;</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsaf.gflbko"/>&nbsp;</th>
			                    <th width="2%" class="tableHeaderField" align="left" >&nbsp;<spring:message code="systema.godsno.maintenance.godsaf.gfenh"/>&nbsp;</th>
			                    <th width="2%" class="tableHeaderField" align="center" >&nbsp;<spring:message code="systema.godsno.maintenance.delete"/>&nbsp;</th>
			                </tr>  
			                </thead>
			                 
			                <tbody >  
				            <c:forEach var="record" items="${model.list}" varStatus="counter">   
				               <tr class="tableRow" height="20" >
				              
				               <td width="2%" id="recordUpdate_${record.gflavd}_${record.gflbko}" class="tableCellFirst" onClick="getRecord(this);" style="border-style: solid;border-width: 0px 1px 1px 0px;border-color:#FAEBD7;" align="center">
				               		<img src="resources/images/update.gif" border="0" alt="edit">
				               </td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gflavd}&nbsp;</font></td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gflbko}&nbsp;</font></td>
				               <td width="2%" class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="left" ><font class="text14">&nbsp;${record.gfenh}&nbsp;</font></td>
				               <td width="2%" id="recordDelete_${record.gflavd}_${record.gflbko}" class="tableCell" onClick="doDeleteRecord(this);" style="border-style: solid;border-width: 0px 1px 1px 0px;border-color:#FAEBD7;" align="center">
				               		<img src="resources/images/delete.gif" width="15px" height="15px" border="0" alt="edit">
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
			<c:if test="${not empty model.errorMessage}">
			<tr>
				<td width="3">&nbsp;</td>
				<td >
	            	<table align="left" border="0" cellspacing="0" cellpadding="0">
				 		<tr>
				 			<td >
				 				<ul class="isa_error text14" >
                                    <li>Server return code: ${model.errorMessage}</li>                                    
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
				<form action="godsnomaintenance_bevillkoder_godsaf_edit.do" name="formRecord" id="formRecord" method="POST" >
					<input type="hidden" name="applicationUser" id="applicationUser" value="${user.user}">
					<input type="hidden" name="language" id="language" value='${user.usrLang}'>
	 	    		<input type="hidden" name="updateId" id=updateId value="${model.updateId}"> 
					<input type="hidden" name="action" id=action value="doUpdate">
					
					<table cellspacing="1" border="0" align="left">
			    	    <tr>
							<td class="text14" title="gflavd">&nbsp;<font class="text14RedBold" >*</font><spring:message code="systema.godsno.maintenance.godsaf.gflavd"/></td>
							<td class="text14" title="gflbko">&nbsp;<font class="text14RedBold" >*</font><spring:message code="systema.godsno.maintenance.godsaf.gflbko"/></td>
							<td class="text14" title="gfenh">&nbsp;<spring:message code="systema.godsno.maintenance.godsaf.gfenh"/></td>							
						</tr>
						<tr>
							<td ><input type="text" class="inputTextMediumBlueMandatoryField" name="gflavd" id="gflavd" size="5" maxlength="4" value='${record.gflavd}'></td>
							<td >
								<select class="inputTextMediumBlueMandatoryField" name="gflbko" id="gflbko" >
			 						<option value="">-velg-</option>
			 						<c:forEach var="item" items="${model.bkoderList}" >
				 				  		<option value="${item.gflbko}" <c:if test="${item.gflbko == record.gflbko}"> selected </c:if> >${item.gflbko}</option>
									</c:forEach>  
								</select>
							</td>
							<td ><input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gfenh" id="gfenh" size="2" maxlength="1" value='${record.gfenh}'></td>
							<td>
								<input onClick="setBlockUI(this);" class="inputFormSubmit" type="submit" name="submit" id="submit" value='<spring:message code="systema.godsno.maintenance.save"/>'/>
							</td>
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

