<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header ===========================-->
<jsp:include page="/WEB-INF/views/headerGodsno.jsp" />
<!-- =====================end header ==========================-->

	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
		specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>
	<SCRIPT type="text/javascript" src="resources/js/godsno_edit.js?ver=${user.versionEspedsg}"></SCRIPT>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
	
	<style type = "text/css">
		.ui-dialog{font-size:11pt;}*/
		.ui-datepicker { font-size:9pt;}
	</style>
	
	
<table width="100%" cellspacing="0" border="0" cellpadding="0">

 <tr>
 <td>	
	<%-- tab container component --%>
	<table width="100%"  class="text14" cellspacing="0" border="0" cellpadding="0">
		<tr height="2"><td></td></tr>
		<tr height="25"> 
			<td width="15%" valign="bottom" class="tabDisabled" align="center" nowrap>
				<a style="display:block;" href="godsno_mainlist.do?action=doFind&rd=1" onClick="setBlockUI();">
					<img style="vertical-align:middle;" src="resources/images/bulletGreen.png" width="10px" height="10px" border="0" alt="efaktura log">
					<font class="tabDisabledLink" ><span id="activeTabList" ><spring:message code="systema.godsno.mainlist.tab"/></span></font>
				</a>
			</td>
			<td width="15%" valign="bottom" class="tab" align="center" nowrap>
				<img style="vertical-align:bottom;" src="resources/images/update.gif" border="0" alt="general list">
				<font class="tabLink">Forhåndsregistrering</font>
			</td>
			 
			<td width="85%" class="tabFantomSpace" align="center" nowrap><font class="tabDisabledLink">&nbsp;</font></td>
		</tr>
	</table>
	</td>
 </tr>
 <tr>
 	<td>
	<%-- --------------------------- --%>	
 	<%-- tab area container PRIMARY  --%>
	<%-- --------------------------- --%>
	<table width="100%" class="tabThinBorderWhite" border="0" cellspacing="0" cellpadding="0">

		<tr height="10"><td colspan="2">&nbsp;</td></tr>
		<%-- --------------- --%>
		<%-- CONTENT --%>
		<%-- --------------- --%>
		<tr>
		<td >
		<table align="center" width="98%" border="0" cellspacing="1" cellpadding="0">
		
		<%-- Left cell --%>
		<tr>
			<td class="text14" valign="top">
				<form action="godsno_edit.do" name="editForm" id="editForm" method="post">
				<input type="hidden" name="applicationUser" id="applicationUser" value='${user.user}'>
				<input type="hidden" name="action" id="action" value='${action}'>
				<input type="hidden" name="avd" id="avd" value='${avd}'>
				<c:if test="${not empty updateFlag}">
					<input type="hidden" name="gogn" id="gogn" value="${record.gogn}">
					<input type="hidden" name="updateFlag" id="updateFlag" value="${updateFlag}">
				</c:if>
				<table style="width:50%" align="left" border="0" cellspacing="0" cellpadding="0">
				 	<tr >
					 	<td >
						<table style="width:100%"  class="dashboardFrameHeader" border="0" cellspacing="1" cellpadding="0">
					 		<tr height="15">
					 			<td class="text14White">&nbsp;Godsnr:&nbsp;<font style="color: yellow;">${record.gogn}</font></td>
					 			
			 				</tr>
			            </table>
			            </td>
		            </tr>
		            <tr >
					 	<td>
						<table style="width:100%"  class="tableBorderWithRoundCorners3D_RoundOnlyOnBottom" border="0" cellspacing="2" cellpadding="1">
					 		<tr height="5px"><tb></tb></tr>
					 		<c:if test="${empty updateFlag}">
						 		<tr >
						 			<td class="text14"><span title="gogn">Godsnr:&nbsp;</span></td>
						 			<td class="text14">
						 				<c:choose>
						 				
						 				<%--<c:when test="${fn:contains(godsnr,'ERROR') || fn:contains(godsnr,'FEIL')}" > --%>
						 				
						 				<c:when test="${fn:length(godsnr) == 4}" ><%-- Meaning there is no match. The user MUST fill up accord. to requir. mask --%>
						 					<input readonly type="text" class="inputTextReadOnly" name="owngogn_1" id="owngogn_1" size="4"  value="${godsnr}">
						 					<select  required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" class="inputTextMediumBlueMandatoryField" name="owngogn_2" id="owngogn_2" >
						 						<option value="">-Velg Bev.kode-</option>
						 						<c:forEach var="record" items="${bevKodeList}" >
							 				  		<option value="${record.gflbko}_${record.gfenh}" >${record.gflbko}&nbsp;&nbsp;${record.gfenh}</option>
												</c:forEach>  
											</select>
											<input readonly type="text" class="inputTextReadOnly" name="owngogn_3" id="owngogn_3" size="3"  value="${dayOfYear}">
						 				</c:when>
						 				<c:otherwise>
						 					<input readonly type="text" class="inputTextReadOnly" name="gogn" id="gogn" size="15"  value="${godsnr}">
						 				</c:otherwise>
						 				</c:choose>
						 				<font class="text16RedBold" >*</font>
						 						
						 			</td>
						 			<td >
						 				<img title="Bev.koder" id="bevKoderDialogImgReadOnly" style="vertical-align:middle; cursor:pointer;" width="16px" height="16px" src="resources/images/info4.png" border="0" alt="bev.koder">
						 			</td>
						 		</tr>							 		
						 		<tr >
						 			<td class="text14"><span title="gognManualCounter">Manuelltnr</span></td>
						 			<td class="text14">
						 				<input onKeyPress="return numberKey(event)" style="text-align: right" type="text" class="inputTextMediumBlue" name="gognManualCounter" id="gognManualCounter" size="3" maxlength="2" value="">
						 			</td>
						 		</tr>
					 		</c:if>
					 		<tr >
					 			<td class="text14"><span title="gogren">Grensepassering</span></td>
					 			<td class="text14">
					 				<input type="text" required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" class="inputTextMediumBlueMandatoryField" name="gogren" id="gogren" size="21" maxlength="20" value="${record.gogren}">
					 				<font class="text16RedBold" >*</font>
					 			</td>
					 			<td class="text14">
					 				<img style="vertical-align:middle;" src="resources/images/calendar.gif" width="12px" height="12px" border="0" alt="dato">
					 				<span title="gogrdt">Dato</span>
					 			</td>
					 			<td class="text14">
					 				<c:choose>
						 				<c:when test="${record.gogrdt != '0'}">
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gogrdt" id="gogrdt" size="7" maxlength="6" value="${record.gogrdt}">
						 				</c:when>
						 				<c:otherwise>
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gogrdt" id="gogrdt" size="7" maxlength="6" value="">
						 				</c:otherwise>
					 				</c:choose>
					 			</td>
					 			<td class="text14">
					 				<img style="vertical-align:middle;" src="resources/images/clock2.png" width="12px" height="12px" border="0" alt="kl">
					 				<span title="gogrkl">Kl.</span>
					 			</td>
					 			<td class="text14">
					 			
					 				<c:choose>
						 				<c:when test="${record.gogrkl > 0}">
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gogrkl" id="gogrkl" size="5" maxlength="4" value="${record.gogrkl}">
						 				</c:when>
										<c:otherwise>
											<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gogrkl" id="gogrkl" size="5" maxlength="4" value="">
										</c:otherwise>
									</c:choose>
					 			
					 				
					 			</td>
					 		</tr>
					 		<tr >
					 			<td class="text14"><span title="goturn">Turnr.</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="goturn" id="goturn" size="21" maxlength="17" value="${record.goturn}">
					 			</td>
					 		</tr>
					 		<tr >
					 			<td class="text14"><span title="gobiln">Kjennetegn</span></td>
					 			<td class="text14">
					 				<input type="text" required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" class="inputTextMediumBlueMandatoryField" name="gobiln" id="gobiln" size="21" maxlength="13" value="${record.gobiln}">
					 				<font class="text16RedBold" >*</font>
					 			</td>
					 		</tr>
					 		<tr >
					 			<td class="text14"><span title="gotype">Type last</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="gotype" id="gotype" size="21" maxlength="20" value="${record.gotype}">
					 			</td>
					 		</tr>
					 		<tr >
					 			<td class="text14"><span title="gomott">Varemottaker</span></td>
					 			<td class="text14">
					 				<input type="text" required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" class="inputTextMediumBlueMandatoryField" name="gomott" id="gomott" size="21" maxlength="15" value="${record.gomott}">
					 				<font class="text16RedBold" >*</font>
					 			</td>
					 		</tr>
					 		<tr height="10"><td></td></tr>
			 				<tr><td colspan="10"><hr size="1" width="100%" 	/></td></tr>
			 				<tr height="10"><td></td></tr>
					 		<tr >
					 			<td class="text14"><span title="golsen">Første lossested</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="golsen" id="golsen" size="21" maxlength="20" value="${record.golsen}">
					 			</td>
					 			<td class="text14">
					 				<img style="vertical-align:middle;" src="resources/images/calendar.gif" width="12px" height="12px" border="0" alt="dato">
					 				<span title="golsdt">Dato</span>
					 			</td>
					 			<td class="text14">
					 				<c:choose>
						 				<c:when test="${record.golsdt != '0'}">
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="golsdt" id="golsdt" size="7" maxlength="6" value="${record.golsdt}">
						 				</c:when>
						 				<c:otherwise>
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="golsdt" id="golsdt" size="7" maxlength="6" value="">
						 				</c:otherwise>
					 				</c:choose>
					 			</td>
					 			<td class="text14">
					 				<img style="vertical-align:middle;" src="resources/images/clock2.png" width="12px" height="12px" border="0" alt="kl">
					 				<span title="golskl">Kl.</span>
					 			</td>
					 			<td class="text14">
					 				<c:choose>
						 				<c:when test="${record.golskl > 0}">
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="golskl" id="golskl" size="5" maxlength="4" value="${record.golskl}">
						 				</c:when>
										<c:otherwise>
											<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="golskl" id="golskl" size="5" maxlength="4" value="">
										</c:otherwise>
									</c:choose>
					 			</td>
					 		</tr>
					 		
								 				
			 				
			 				<tr height="20"><td></td></tr>
			 				
			 				<%-- SUBMIT button --%>
			 				<c:if test="${record.gotrnr != '*SLETTET' && (!fn:contains(godsnr,'ERROR') && !fn:contains(godsnr,'FEIL')) }">
				 				<tr>
				 					<td colspan="10">
				 					<table width="100%" border="0" cellspacing="1" cellpadding="1">
					 					<tr>
					 					<td align="right" class="text14" valign="top">
					 						<input class="inputFormSubmit" type="submit" name="submit" id="submit" value='Lagre'>&nbsp;
					 					</td>
					 					</tr>
				 					</table>
				 					</td>
			 					</tr>
		 					</c:if>
		 					<tr height="5"><td></td></tr>
		 					
			            </table>
			            </td>
		            </tr>

	            </table>
	            </form>
            </td>
            <%--
            <td width="40%"class="text14" valign="top">
            	<table width="80%" align="left" border="0" cellspacing="0" cellpadding="0">
				 	<tr >
					 	<td >
						<table width="100%" class="dashboardFrameHeader" border="0" cellspacing="1" cellpadding="0">
					 		<tr height="15">
					 			<td class="text14White">
					 			&nbsp;&nbsp;Info&nbsp;
					 			</td>
			 				</tr>
			            </table>
			            </td>
		            </tr>
		            
		            <tr >
					 	<td>
						<table width="100%" class="formFrame" border="0" cellspacing="2" cellpadding="1">
							
			 				<tr height="5"><td></td></tr>
			 				<tr>
			 					<td class="text14MediumBlue">&nbsp;Endring av registrert Godsnr:&nbsp;<b>${record.gogn}</b></td>
			 				</tr>
		 					<tr height="5"><td></td></tr>
			 				<tr>
			 					<td>
				 					<table>
				 					<tr>	
					 					<td class="text14MediumBlue">
					 						<img style="vertical-align:middle;" src="resources/images/calendar.gif" width="12px" height="12px" border="0" alt="dato">&nbsp;<b>${today}</b>
					 					</td>
					 					<td class="text14MediumBlue">
					 						&nbsp;&nbsp;<img style="vertical-align:middle;" src="resources/images/clock2.png" width="12px" height="12px" border="0" alt="kl"><b>${time}</b>
					 					</td>
				 					</tr>
				 					</table>
			 					</td>
			 				</tr>
		 					
			 				<tr><td colspan="10"><hr size="1" width="100%"/></td></tr>

			            </table>
			            </td>
		            </tr>
	            </table>
            </td>
             --%>
		</tr>
		<tr height="20"><td colspan="2">&nbsp;</td></tr>

	</table> 
	</td>
 </tr>
 </table>
 </td>
 </tr>
 
 
 <tr>
	<td>
		<div id="dialogBevKoderReadOnly" title="Bev.koder">
		<table >
	 		<thead >
	 			<tr>
	 			<th align="center" class="text14" ><b>Avd</b></th>
	 			<th align="left" class="text14" ><b>Kode</b></th>
	 			</tr>
	 		</thead>
	 		<tbody>
	 		<c:forEach items="${bevKodeList}" var="record" varStatus="counter">    
	        	<tr class="tableRow" >  
	              <td align="center" width="2%" class="tableCellFirst" >${record.gflavd}</td>
	              <td align="left" class="tableCell" >${record.gflbko}&nbsp;&nbsp;${record.gfenh}</td>
            	</tr>
           	</c:forEach>
		    </tbody>           
	
		</table>
		</div>
	</td>	
 </tr>