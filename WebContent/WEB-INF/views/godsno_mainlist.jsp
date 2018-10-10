<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header ===========================-->
<jsp:include page="/WEB-INF/views/headerGodsno.jsp" />
<!-- =====================end header ==========================-->
	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
		specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>	
	<SCRIPT type="text/javascript" src="resources/js/godsno_mainlist.js?ver=${user.versionEspedsg}"></SCRIPT>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
	
	<style type = "text/css">
		.ui-dialog{font-size:11pt;}
		.ui-datepicker { font-size:9pt;}
		/* this line will align the datatable search field in the left */
		.dataTables_wrapper .mainListFilter .dataTables_filter{float:left}
		.dataTables_wrapper .mainListFilter .dataTables_info{float:right}
		
	</style>

<table width="100%"  class="text14" cellspacing="0" border="0" cellpadding="0">
	<tr>
	<td>
	<%-- tab container component --%>
	<table width="100%"  class="text14" cellspacing="0" border="0" cellpadding="0">
		<tr height="2"><td></td></tr>
		<tr height="25"> 
			<td width="15%" valign="bottom" class="tab" align="center" nowrap>
				<img style="vertical-align:middle;" src="resources/images/bulletGreen.png" width="10px" height="10px" border="0" alt="list">
				<font class="tabLink" ><span id="activeTabList" onMouseOver="showPop('list_info');" onMouseOut="hidePop('list_info');"><spring:message code="systema.godsno.mainlist.tab"/></span></font>
				<div class="text14" style="position: relative;" align="left">
                <span style="position:absolute;top:2px;left:50px;" id="list_info" class="popupWithInputText text14"  >
                	Standard antall dager tilbake: <b>${user.dftdg}</b><br/>
					Hvis du ønsker å søke annat, bruk filtret<br/>
				</span>	
				</div>
			</td>
			<td width="15%" valign="bottom" class="tabDisabled" align="center" nowrap>
				<a id="createNewOrderTabIdLink" style="display:block;" runat="server" href="#">
					<img style="vertical-align:middle;" src="resources/images/add.png" width="10px" height="10px" border="0" alt="add">
					<font class="tabDisabledLink" ><span id="activeTabList" ><spring:message code="systema.godsno.createnew.tab"/></span></font>
				</a>
			</td>
			<td width="85%" class="tabFantomSpace" align="center" nowrap><font class="tabDisabledLink">&nbsp;</font></td>
		</tr>
	</table>
	</td>
	</tr>
	
		<tr>
		<td>
			<input type="hidden" name="applicationUser" id="applicationUser" value='${user.user}'>
			<input type="hidden" name="language" id="language" value='${user.usrLang}'>
			<%-- this table wrapper is necessary to apply the css class with the thin border --%>
			<table width="100%" class="tabThinBorderWhite" border="0" cellspacing="0" cellpadding="0">
			<tr height="3"><td></td></tr> 
			<tr>
				<td>
				<form name="searchForm" id="searchForm" action="godsno_mainlist.do?action=doFind" method="post" >
				<input type="hidden" name="todo" id="todo" value=''>
				<table id="containerdatatableTable" width="100%" cellspacing="2" align="left">
				<tr>
					<td class="text14" title="gogn">&nbsp;Godsnr</td>
					<td class="text14" title="gotrnr">&nbsp;Transittnr.</td>
					<td class="text14" title="gomott">&nbsp;Varemottaker</td>
					<td class="text14" title="goturn">&nbsp;Turnr.</td>
					<td class="text14" title="gobiln">&nbsp;Bilnr.</td>
				</tr>
				<tr>	
					<td class="text14"><input type="text" class="inputText" name="gogn" id="gogn" size="16" maxlength="15" value='${searchFilter.gogn}'></td>
		        	<td class="text14"><input type="text" class="inputText" name="gotrnr" id="gotrnr" size="21" maxlength="20" value='${searchFilter.gotrnr}'></td>
		        	<td class="text14"><input type="text" class="inputText" name="gomott" id="gomott" size="16" maxlength="15" value='${searchFilter.gomott}'></td>
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
							<th width="2%" align="left" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.godsnr"/></th> 
							<th width="2%" align="left" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.transittnr"/></th>
		                    <th width="2%" align="center" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.type"/></th>
		                    <th width="2%" align="center" class="tableHeaderField" title="Merknadsjournal" >&nbsp;<spring:message code="systema.godsno.mjournallist.label.merknadsjournal"/></th> 
							<th align="left" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.godsmottaker"/></th> 
							<th align="left" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.turnr"/></th>
		                    <th width="2%" align="left" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.bilnr"/></th>
		                    <th width="2%" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.avgtollsted"/></th>
		                    <th width="2%" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.transittdato"/></th>
		                    <th width="2%" class="tableHeaderField" title="Tillegg av transitt på godsnummer" >&nbsp;<spring:message code="systema.godsno.mainlist.label.copy"/>
		                    </th>
		                    <th width="2%" class="tableHeaderField">&nbsp;<spring:message code="systema.godsno.mainlist.label.delete"/></th>
		                </tr> 
		                </thead>
		                
		                <tbody>
			            <c:forEach items="${model.list}" var="record" varStatus="counter">    
			             <tr class="tableRow" height="20">  
			               <td align="center" width="2%" class="tableCellFirst" >
			               		<a id="alinkEdit_${counter.count}" style="display:block;" href="godsno_edit.do?updateFlag=1&gogn=${record.gogn}&gotrnr=${record.gotrnr}" onClick="setBlockUI()" >
			               			<img title="Endre post" style="vertical-align:bottom;" src="resources/images/update.gif" border="0" alt="edit">
			               		</a>	
			               </td>
			               <td width="2%" class="tableCell" style="color:navy;">
			               		${record.gogn}
			               	</td>
			               	<td width="2%" class="tableCell" >${record.gotrnr}</td>
			               	<td width="2%" align="center" class="tableCell" >
				               	<c:choose>		
					               	<c:when test="${not empty record.goavg && fn:length(record.goavg)>12}">
					               			${fn:substring(record.goavg, 12, fn:length(record.goavg))}
				               		</c:when>
				               		<c:otherwise>
				               				${record.goavg}
				               		</c:otherwise>
			               		</c:choose>
		               	   	</td>
			               	<td width="2%" align="center" class="tableCell" >
			               		<c:set var = "key" value = "${record.gogn}${record.gotrnr}"/>
			               		<c:if test="${not empty model[key]}">
			               			<a title="print" class="printMerknaderLink" style="display:block;" id="printMerknaderLink${counter.count}" OnClick="executePrintMerknadGognPgm(this);" runat="server" href="#">
			               				<img title="Merknadsjournal utskrift" src="resources/images/veiledning.png" width="20px" height="20" border="0" alt="Merknadsjournal utskrift">
			               			</a>
			               			<div style="display: none;" class="clazz_dialog" id="dialogPrintMerknader${counter.count}" title="Dialog">
										<form id="printMerknaderForm">
										<input type="hidden" name="printMerknaderGogn${counter.count}" id="printMerknaderGogn${counter.count}" value='${record.gogn}'/>
									 	<table>
					   						<tr height="3"><td></td></tr>
											<tr>
												<td class="text14" align="left" ><b>Godsnr.</b>&nbsp;
													<input readonly type="text" class="inputTextReadOnly" id="gognForPrint${counter.count}" name="gognForPrint${counter.count}" size="20" value='${record.gogn}'>
												</td>
					   						</tr>
					   						
											<tr height="15"><td></td></tr>
											<tr>
												<td colspan="3" class="text14MediumBlue" align="left">
													
								               		
														<label id="printMerknaderStatus${counter.count}"></label>
												</td>
											</tr>
											
										</table>
										</form>
									</div>
			               		</c:if>	
			               	</td>
			               <td class="tableCell" >${record.gomott}</td>
			               
			               <td class="tableCell" >${record.goturn}</td>
			               <td width="2%" class="tableCell" >${record.gobiln}</td>
			               <td width="2%" class="tableCell" >
				               	<c:choose>		
					               	<c:when test="${not empty record.goavg && fn:length(record.goavg)>12}">
					               		${fn:substring(record.goavg, 0, 12)}
				               		</c:when>
				               		<c:otherwise>
				               			<c:if test="${not empty record.goavg && fn:length(record.goavg)>3}">
					               			${fn:substring(record.goavg, 0, fn:length(record.goavg))}
				               			</c:if>
				               		</c:otherwise>
			               		</c:choose>
			               	</td>
			               <td width="2%" class="tableCell" >${record.gotrdt}</td>
			               <td align="center" width="2%" class="tableCell" >
			               		<a style="cursor:pointer;display:block;" id="alinkClone_${counter.count}" style="display:block;" href="godsno_clone.do?action=doFetch&gogn=${record.gogn}&gotrnr=${record.gotrnr}" onClick="setBlockUI()" >
			               			<img title="Tillegg av transitt på godsnr." src="resources/images/copy.png" border="0" alt="copy">
			               		</a>
			               	</td>	
			               <td align="center" width="2%" class="tableCell" >
			               		<a style="cursor:pointer;display:block;" id="id_${record.gogn}@id2_${record.gotrnr}" onClick="doDeleteOrder(this)" >
			               			<img title="Slett post" style="vertical-align:bottom;" src="resources/images/delete.gif" border="0" alt="edit">
			               		</a>	
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
			</table>
			</td>
		</tr>
					
		<%-- Pop-up window --%>
		<tr>
		<td>
			<div id="dialogCreateNewOrder" title="Dialog">
				<form  action="godsno_edit.do" name="createNewOrderForm" id="createNewOrderForm" method="post">
				 	<input type="hidden" name="action" id="action" value="doCreate">
				 	<p class="text14" >&nbsp;Velg inng.parametre</p>
					 				
					<table>
						<tr>
							<td class="text14MediumBlue">Avd&nbsp;
								<select class="selectMediumBlueE2" name="avd" id="avd" autofocus>
			 						<option value="">-velg-</option>
			 						<c:forEach var="record" items="${model.avdList}" >
				 				  		<option value="${record.koaavd}"<c:if test="${record.koaavd == user.asavd}"> selected </c:if> >${record.koaavd}</option>
									</c:forEach>  
								</select>
							</td>
							<td width="5"></td>
							<td class="text14MediumBlue">Sign&nbsp;
								<select class="selectMediumBlueE2" name="sign" id="sign" >
			 						<option value="">-velg-</option>
			 						<c:forEach var="record" items="${model.signatureList}" >
				 				  		<option value="${record.kosfsi}"<c:if test="${user.signatur == record.kosfsi}"> selected </c:if> >${record.kosfsi}</option>
									</c:forEach>  
								</select>
							</td>
						</tr>
						
					</table>
						
				</form>
			</div>
		</td>
		</tr>
		
		
</table>
		
		
<!-- ======================= footer ===========================-->
<jsp:include page="/WEB-INF/views/footer.jsp" />
<!-- =====================end footer ==========================-->

