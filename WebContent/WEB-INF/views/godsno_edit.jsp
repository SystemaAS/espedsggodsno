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
		.ui-dialog{font-size:11pt;}
		.ui-datepicker { font-size:10pt;}
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
				<c:choose>
					<c:when test="${not empty updateFlag}">
						<font class="tabLink">Godsregistrering</font>
					</c:when>
					<c:otherwise>
						<font class="tabLink">Forhåndsregistrering</font>
					</c:otherwise>
				</c:choose>
				
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
		<table align="center" style="width:100%" border="0" cellspacing="1" cellpadding="0">

		<tr>
			<td align="center" style="width:50%" class="text14" valign="top">
				<form action="godsno_edit.do" name="editForm" id="editForm" method="post">
				<input type="hidden" name="applicationUser" id="applicationUser" value='${user.user}'>
				<input type="hidden" name="action" id="action" value='${action}'>
				<input type="hidden" name="avd" id="avd" value='${avd}'>
				
				<c:if test="${not empty updateFlag}">
					<input type="hidden" name="gogn" id="gogn" value="${record.gogn}">
					<%-- this is for an update of the key gortrnr --%>
					<input type="hidden" name="gotrnrOrig" id="gotrnrOrig" value='${record.gotrnr}'>
					<input type="hidden" name="updateFlag" id="updateFlag" value="${updateFlag}">
					<input type="hidden" name="pos1TargetString" id="pos1TargetString" value="${pos1TargetString}">
				
				</c:if>
				<table style="width:90%" align="left" border="0" cellspacing="0" cellpadding="0">
				 	<tr >
					 	<td >
						<table style="width:100%"  class="dashboardFrameHeader" border="0" cellspacing="1" cellpadding="0">
					 		<tr height="15">
					 			<td class="text14White">&nbsp;Godsnr:&nbsp;<font style="color: yellow;"><b>${record.gogn}</b></font>
					 				<c:if test="${ not empty updateFlag}">
						 				&nbsp;&nbsp;&nbsp;
						 				<a style="cursor:pointer;" id="alinkClone_${counter.count}" style="display:block;" href="godsno_clone.do?action=doFetch&gogn=${record.gogn}&gotrnr=${record.gotrnr}" onClick="setBlockUI()" >
					               		<img style="vertical-align:bottom;" title="Tillegg av transitt på godsnr." src="resources/images/copy.png" border="0" alt="copy">
						               	</a>
						 			</c:if>
					 			</td>
					 			<c:if test="${ empty updateFlag}">
						 			<td align="right">
						 				<img title="Bev.koder" id="bevKoderDialogImgReadOnly" style="vertical-align:middle; cursor:pointer;" width="14px" height="14px" src="resources/images/info4.png" border="0" alt="bev.koder">
						 				&nbsp;
						 			</td>
					 			</c:if>
						 			
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
						 				<td colspan="3" class="text14">
						 				<c:choose>
						 				<c:when test="${fn:length(godsnr) == 4}" ><%-- Meaning there is no match. The user MUST fill up accord. to requir. mask --%>
						 					
						 					<input type="hidden" name="owngogn_1" id="owngogn_1" size="4"  value="${godsnr}">
						 					<input type="hidden" name="owngogn_3" id="owngogn_3" size="3"  value="${dayOfYear}">
						 					<select  name="owngogn_2" id="owngogn_2" required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" class="inputTextMediumBlueMandatoryField" >
						 						<option value="">Velg Bev.kode</option>
						 						<c:forEach var="record" items="${bevKodeListMainTbl}" >
							 				  		<option value="${record.gflbko}_${record.gfenh}" >${record.gflbko}&nbsp;&nbsp;${record.gfenh}</option>
												</c:forEach>  
											</select><font class="text16RedBold" >*</font>
											<img title="search" id="divBevKodeListDialogImgReadOnly" style="vertical-align:middle; cursor:pointer;" width="12px" height="12px" src="resources/images/find.png" height="11px" width="11px" border="0" alt="bev.koder">
											&nbsp;
											<input tabindex=-1 readonly type="text" class="inputTextReadOnly" name="tmpGogn" id="tmpGogn" size="20"  value="${tmpGogn}">
											<img onMouseOver="showPop('gogn_info');" onMouseOut="hidePop('gogn_info');"style="vertical-align:middle;" width="14px" height="14px" src="resources/images/info3.png" border="0" alt="info">
											<div class="text11" style="position: relative;" align="left">
											<span style="position:absolute; left:250px; top:3px; width:250px" id="gogn_info" class="popupWithInputText"  >
												<font class="text11">
								           			<b>Godsnr.</b>
								           			<div>
								           			<p><b>xx</b> blir erstattet med neste nr fra telleverk ved fullføring.</p>
								           			</div>
							           			</font>
											</span>
											</div>
						 				</c:when>
						 				<c:otherwise>
						 					<input tabindex=-1 readonly type="text" class="inputTextReadOnly" name="gogn" id="gogn" size="20" value="${godsnr}">
						 					<font class="text16RedBold" >*</font>
						 					<img title="search" id="divBevKodeListDialogImgReadOnly" style="vertical-align:middle; cursor:pointer;" width="10px" height="10px" src="resources/images/sort_down.png" border="0" alt="bev.koder">
						 				</c:otherwise>
						 				</c:choose>
						 				<div id="divBevKodeList" style="display:none;position: relative;height:10em;" class="ownScrollableSubWindowDynamicWidthHeight" align="left" >
					 						<table id="tblBevKodeList" class="inputTextMediumBlueMandatoryField">
					 							<tr><td colspan="2" class="text12"><b>Bev.koder</b></td></tr>
					 						
					 							<c:forEach items="${bevKodeListMainTbl}" var="record" varStatus="counter">  
												<tr>
													<td id="id_${record.gflbko}@id2_${record.gfenh}" OnClick="doPickBevKode(this)" class="tableHeaderFieldFirst" style="cursor:pointer;" ><font class="text14SkyBlue">${record.gflbko}&nbsp;${record.gfenh}</font></td>
													<td class="tableHeaderField12">${record.gflbs1}&nbsp;${record.gflbs2}&nbsp;${record.gflbs3}&nbsp;${record.gflbs4}</td>
												</tr>
												</c:forEach>
											</table>	
										</div>	
									</td>	
						 		</tr>
						 		<%-- hide it temporarily according to JOVO,CB							 		
						 		<tr >
						 			<td class="text14"><span title="gognManualCounter">Manuelltnr</span></td>
						 			<td class="text14">
						 				<input onKeyPress="return numberKey(event)" style="text-align: right" type="text" class="inputTextMediumBlue" name="gognManualCounter" id="gognManualCounter" size="3" maxlength="2" value="">
						 			</td>
						 		</tr>
						 		 --%>
					 		</c:if>
					 		<tr >
					 			<td class="text14"><span title="gogren">Grensepassering</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlueUPPERCASE" name="gogren" id="gogren" size="21" maxlength="20" value="${record.gogren}">
					 			</td>
					 			<td class="text14">
					 				<img style="vertical-align:middle;" src="resources/images/calendar.gif" width="12px" height="12px" border="0" alt="dato">
					 				<span title="gogrdt">Dato</span>
					 			
					 				<c:choose>
						 				<c:when test="${record.gogrdt != '0'}">
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gogrdt" id="gogrdt" size="7" maxlength="6" value="${record.gogrdt}">
						 				</c:when>
						 				<c:otherwise>
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gogrdt" id="gogrdt" size="7" maxlength="6" value="">
						 				</c:otherwise>
					 				</c:choose>
					 				<img style="vertical-align:middle;" src="resources/images/clock2.png" width="12px" height="12px" border="0" alt="kl">
					 				<span title="gogrkl">Kl.</span>
					 			
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
					 				<input type="text" required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" class="inputTextMediumBlueUPPERCASEMandatoryField" name="gobiln" id="gobiln" size="21" maxlength="13" value="${record.gobiln}">
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
					 		<tr >
					 			<td class="text14"><span title="gotrnr">Transittnr.</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="gotrnr" id="gotrnr" size="21" maxlength="20" value="${record.gotrnr}">
					 				&nbsp;
					 				<button title="Hente Posisjon" name="godsnoPosButton" id="godsnoPosButton" class="inputFormSubmitStd" type="button">Pos.</button>
					 			</td>
					 			<td class="text14">
					 				<img style="vertical-align:middle;" src="resources/images/calendar.gif" width="12px" height="12px" border="0" alt="dato">
					 				<span title="gotrdt">Transittdato</span>
					 			
					 				<c:choose>
						 				<c:when test="${record.gotrdt != '0'}">
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gotrdt" id="gotrdt" size="7" maxlength="6" value="${record.gotrdt}">
						 				</c:when>
						 				<c:otherwise>
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="gotrdt" id="gotrdt" size="7" maxlength="6" value="">
						 				</c:otherwise>
					 				</c:choose>
					 			</td>
					 		</tr>
					 		
					 		
					 		
					 		
					 		<tr >
					 			<td class="text14"><span title="gotrty">T-papirtype</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlueUPPERCASEMandatoryField" name="gotrty" id="gotrty" size="5" maxlength="5" value="${record.gotrty}">
					 				
					 				<font class="text12" style="font-style: italic;">Blank=T1</font>
					 			</td>
					 		</tr>
					 		<tr >
					 			<td class="text14"><span title="goavg">Avg.tollsted</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="goavg" id="goavg" size="15" maxlength="15" value="${record.goavg}">
					 				<%--<input type="text" tabindex=-1 readonly class="inputTextReadOnly" name="xreadyOnly" id="xreadyOnly" size="4" value="${record.gotrty}"> --%>
					 				
					 			</td>
					 		</tr>
					 		<tr height="2"><td></td></tr>
					 		
					 		<tr >
					 			<td class="text14"><img onMouseOver="showPop('goortn_info');" onMouseOut="hidePop('goortn_info');"style="vertical-align:middle;" width="12px" height="12px" src="resources/images/info3.png" border="0" alt="info">
									<span title="goortn">Orig.transittnr.</span>	
									<div class="text11" style="position: relative;" align="left">
									<span style="position:absolute; top:3px; width:250px" id="goortn_info" class="popupWithInputText"  >
										<font class="text11">
						           			<b>Orig.transittnr.</b>
						           			<p>Ved forpassing</p>
						           			
					           			</font>
									</span>
									</div>
					 			</td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="goortn" id="goortn" size="21" maxlength="20" value="${record.goortn}">
					 				
					 			</td>
					 			<td class="text14" colspan="3">
					 				<span title="goorty">Type</span>
					 				<input type="text" class="inputTextMediumBlueUPPERCASEMandatoryField" name="goorty" id="goorty" size="5" maxlength="5" value="${record.goorty}">
					 				<font class="text12" style="font-style: italic;">Blank=T2</font>
					 				<c:if test="${record.gotrnr != '*SLETTET' && (!fn:contains(godsnr,'ERROR') && !fn:contains(godsnr,'FEIL')) }" >
							 			<%-- present button ONLY if the record to release exists --%>
							 			<c:if test="${not empty releaseRecordExists}">
								 			&nbsp;&nbsp;<span title="Fristill forpasset tollager-beholdning"><input onClick="executeReleaseFristill(this);" class="inputFormSubmitStd" type="button" name="buttonRelease" id="buttonRelease" value='Fristill'></span>
					 					</c:if>
									</c:if>
					 			</td>
					 			
					 		</tr>
					 		
					 		<c:if test="${not empty jtPosList}">
					 		<tr>
					 		<td>&nbsp;</td>
						 	<td >
								<table style="width:50%;" id="containerdatatableTable" border="0" cellspacing="0" cellpadding="0">	
						 		<tr >
						 			<td >
									<table id="posList" class="display compact cell-border" >
										<thead>
										<tr class="tableHeaderField">
											<th align="left" class="text12">&nbsp;<spring:message code="systema.godsno.maintenance.godsjt.gtpos1"/></th>
											<th align="left" class="text12">&nbsp;<spring:message code="systema.godsno.maintenance.godsjt.gtpos2"/></th>
											<th align="center" class="text12">&nbsp;<spring:message code="systema.godsno.mainlist.label.delete"/></th>
										</tr>
										</thead>
						                
						                <tbody>
							            <c:forEach items="${jtPosList}" var="record" varStatus="counter">    
							             <tr class="tableRow" >  
							               <td width="2%" class="text12" style="background-color: lightyellow" >${record.gtpos1}</td>
							               <td width="2%" class="text12" style="background-color: lightyellow" >${record.gtpos2}</td>
							               <td align="center" width="2%" class="text12" style="background-color: lightyellow" >
							               		<a style="cursor:pointer;display:block;" id="gtgn_${record.gtgn}@gttrnr_${record.gttrnr}@gtpos1_${record.gtpos1}@gtpos2_${record.gtpos2}" onClick="setBlockUI();doDeletePosisjon(this)" >
							               			<img title="Slett post" style="vertical-align:bottom;" src="resources/images/delete.gif" width="12px" height="12px" border="0" alt="edit">
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
					 		</c:if>
					 		
							
					 		<tr height="2"><td></td></tr>
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
					 			
					 				<c:choose>
						 				<c:when test="${record.golsdt != '0'}">
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="golsdt" id="golsdt" size="7" maxlength="6" value="${record.golsdt}">
						 				</c:when>
						 				<c:otherwise>
						 					<input onKeyPress="return numberKey(event)" type="text" class="inputTextMediumBlue" name="golsdt" id="golsdt" size="7" maxlength="6" value="">
						 				</c:otherwise>
					 				</c:choose>
					 			
					 				<img style="vertical-align:middle;" src="resources/images/clock2.png" width="12px" height="12px" border="0" alt="kl">
					 				<span title="golskl">Kl.</span>
					 			
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
					 		<tr height="5"><td></td></tr>
					 		<%--
					 		<tr >
					 			<td class="text14"><span title="golsen">Reiserute 1-2</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="gorei1" id="gorei1" size="22" maxlength="20" value="${record.gorei1}">
				 				</td>
				 				<td colspan="2" class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="gorei2" id="gorei2" size="22" maxlength="20" value="${record.gorei2}">
				 				</td>
							</tr> 	
							<tr >
					 			<td class="text14"><span title="golsen">Reiserute 3-4</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="gorei3" id="gorei3" size="22" maxlength="20" value="${record.gorei3}">
				 				</td>
				 				<td colspan="2" class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="gorei4" id="gorei4" size="22" maxlength="20" value="${record.gorei4}">
				 				</td>
							</tr> 				
			 				
			 				<tr height="15"><td></td></tr>
			 				 --%>
			 				<%-- SUBMIT button --%>
			 				<c:if test="${record.gotrnr != '*SLETTET' && (!fn:contains(godsnr,'ERROR') && !fn:contains(godsnr,'FEIL')) }">
				 				<tr>
				 					<td colspan="10">
				 					<table style="width:98%" border="0" cellspacing="0" cellpadding="0">
					 					<tr>
					 					<td align="right" class="text14" valign="top">
					 						<input class="inputFormSubmit" type="submit" name="submit" id="submit" value='Lagre'>
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
            <%-- ONLY with UPDATE --%>
            <c:choose>
            <c:when test="${not empty updateFlag}">
	            <td style="width:50%" valign="top" >
	            	<table align="left" style="width:95%" class="greenContainerFrameE2" id="containerdatatableTable" border="0" cellspacing="0" cellpadding="0">
						<tr >
				 			<td class="text14">
				 			<img style="vertical-align: bottom" src="resources/images/app.png" width="16" hight="16" border="0" alt="journal">
				 			<b>Merknadsjournal</b>
				 			</td>
			 			</tr>
			 			<tr height="5"><td></td></tr>
						<tr>
							<td >
							<table style="width:100%" id="merknadList" class="display compact cell-border" >
								<thead>
								<tr class="tableHeaderField" >
									<th width="2%" class="tableHeaderFieldFirst14">Endre</th>
									<th width="2%" align="center" class="tableHeaderField14">&nbsp;<spring:message code="systema.godsno.merknedlist.label.gopos"/></th>
									<th width="2%" align="center" class="tableHeaderField14">&nbsp;<spring:message code="systema.godsno.merknedlist.label.goantk"/></th>
									<th nowrap align="left" class="tableHeaderField14">&nbsp;<spring:message code="systema.godsno.merknedlist.label.govsla"/></th>
									<th align="left" class="tableHeaderField14">&nbsp;<spring:message code="systema.godsno.merknedlist.label.gomerk"/></th>
									<th align="center" class="tableHeaderField14">&nbsp;<spring:message code="systema.godsno.merknedlist.label.gomkod"/></th>
									<th width="2%" class="tableHeaderFieldFirst14">Slett</th>
									
									 
								</tr>
								</thead>
				                
				                <tbody>
					            <c:forEach items="${merknadList}" var="record" varStatus="counter">    
					             <tr class="tableRow" >  
					               <td align="center" width="2%" class="tableCellFirst12" >
					               		<a onClick="getRecordMerknad(this);" id="gogn_${record.gogn}@gotrnr_${record.gotrnr}@gopos_${record.gopos}" style="display:block;" >
					               			<img title="Endre post" style="vertical-align:bottom;" src="resources/images/update.gif" border="0" alt="edit">
					               		</a>	
					               </td>
					               <td align="center" width="2%" class="tableCell12" style="color:navy;">${record.gopos}</td>
					               <td align="right" width="2%" class="tableCell12" >${record.goantk}&nbsp;</td>
					               <td class="tableCell12" >${record.govsla}</td>
					               <td class="tableCell12" >${record.gomerk}</td>
					               <td align="center" width="2%" class="tableCell12" >${record.gomkod}</td>
					               <td align="center" width="2%" class="tableCell12" >
					               		<a onClick="setBlockUI();doDeleteMerknad(this);" id="gogn_${record.gogn}@gotrnr_${record.gotrnr}@gopos_${record.gopos}" style="display:block;" >
					               			<img title="remove post" style="vertical-align:bottom;" src="resources/images/delete.gif" border="0" alt="remove">
					               		</a>	
					               </td>
					               </tr>
					               <c:set var="merknadCounter" scope = "request" value = "${record.gopos}"/>
				               	</c:forEach>
				            	</tbody>
							</table>
							</td>
						</tr>
						<tr height="5"><td>&nbsp;</td></tr>
						<tr>
							<td><button name="newRecordButton" id="newRecordButton" class="inputFormSubmitStd" type="button" >Fjern verdier</button></td>
						</tr>	
						<tr>
							<td align="center" class="text14" valign="top" >
			            	<table style="width:100%" align="center" id="containerdatatableTable" border="0" cellspacing="0" cellpadding="0">
			            		<tr >
						 			<td >
						 			<table style="width:100%" align="center" class="formFrameHeader" border="0" cellspacing="0" cellpadding="0">
						 				<tr height="15">
							 				<td class="text14White">&nbsp;Lage ny / Endre merknad</td>
						 				</tr>
						 			</table>	
					 				</td>
			 					</tr>
			            	
								<tr >
						 			<td >
						 				<form name="editMerknadForm" id="editMerknadForm" >
						 				<input type="hidden" name="updateMerknad_flag" id="updateMerknad_flag" value=""/>
						 				<table style="width:100%" align="center" class="formFrame" border="0" cellspacing="0" cellpadding="0">
										 <tr>
										 	<td class="text14"><span title="gopos">&nbsp;Pos i M.jour</span><font class="text12RedBold" >*</font></td>
										 	<td class="text14"><img onMouseOver="showPop('gomkod_info');" onMouseOut="hidePop('gomkod_info');"style="vertical-align:middle;" width="14px" height="14px" src="resources/images/info3.png" border="0" alt="info">
										 		<span title="gomkod">M-jourkode</span><font class="text12RedBold" >*</font>
										 		<div class="text11" style="position: relative;" align="left">
												<span style="position:absolute; top:3px; width:250px" id="gomkod_info" class="popupWithInputText"  >
													<font class="text11">
									           			<b>M-jourkode</b>
									           			<div>
										           			<p><b>DI</b>=Avvik på eksisterende post (manko eller annet avvik)<br/> 
										           			<b>NE</b>=Ny post ( = Overtalig)    
									           			</div>
								           			</font>
												</span>
												</div>	
										 	</td>
										 	<td class="text14"><span title="goantk">&nbsp;Ant.kolli</span><font class="text12RedBold" >*</font></td>
										 	<td class="text14"><span title="govsla">&nbsp;<spring:message code="systema.godsno.merknedlist.label.govsla"/></span><font class="text12RedBold" >*</font></td>
										 	<td class="text14"><span title="gomer1">&nbsp;Merket</span></td>
										 	<td class="text14"><span title="gosted">&nbsp;Avg.tollsted</span><font class="text12RedBold" >*</font></td>
										 	
										 </tr>
										 <tr>
										 	<td class="text14">
										 		<c:choose>
											 		<c:when test="${empty merknadCounter}">
											 			<input readonly style="text-align: right" type="text" class="inputTextReadOnly" name="gopos" id="gopos" size="4" maxlength="4" value="1">	
											 		</c:when>
											 		<c:otherwise>
											 			<input readonly style="text-align: right" type="text" class="inputTextReadOnly" name="gopos" id="gopos" size="4" maxlength="4" value="${merknadCounter + 1}">
											 		</c:otherwise>
										 		</c:choose>
										 											 		
										 	</td>
										 	<td class="text14">
										 		<select class="inputTextMediumBlueMandatoryField" style="width:60px" name="gomkod" id="gomkod" >
							 						<option value="DI" >DI</option>
							 						<option value="NE" >NE</option>
												</select>									 		
										 	</td>
										 	<td class="text14">
										 		<input onBlur="checkMandatoryFields()" onKeyPress="return numberKey(event)" style="text-align: right" type="text" class="inputTextMediumBlueMandatoryField" name="goantk" id="goantk" size="5" maxlength="5" value="">									 		
										 	</td>
										 	<td class="text14">
										 		<input onBlur="checkMandatoryFields()" type="text" class="inputTextMediumBlueMandatoryField" name="govsla" id="govsla" size="18" maxlength="18" value="${record.gotype}">									 		
										 	</td>
										 	<td class="text14">
										 		<input type="text" class="inputTextMediumBlue" name="gomer1" id="gomer1" size="9" maxlength="9" value="">									 		
										 	</td>
										 	<td class="text14">
										 		<input onBlur="checkMandatoryFields()" type="text" class="inputTextMediumBlueMandatoryField" name="gosted" id="gosted" size="18" maxlength="18" value="${record.goavg}">									 		
										 	</td>
										 </tr>
										 
										 <tr>
										 	<td class="text14"><span title="gopos2">Pos. i lastel.</span></td>
										 	<td colspan="2" class="text14"><span title="gomotm">&nbsp;Varemottaker</span><font class="text12RedBold" >*</font></td>
										 	<td colspan="2" class="text14"><span title="gomerk">&nbsp;Merknad</span><font class="text12RedBold" >*</font></td>
										 	
										 </tr>
										 <tr>
										 	
										 	<td class="text14">
										 		<input onKeyPress="return numberKey(event)" style="text-align: right" type="text" class="inputTextMediumBlue" name="gopos2" id="gopos2" size="4" maxlength="4" value="">									 		
										 	</td>
											<td colspan="2" class="text14">
										 		<input onBlur="checkMandatoryFields()" type="text" class="inputTextMediumBlueMandatoryField" name="gomotm" id="gomotm" size="25" maxlength="28" value="${record.gomott}">									 		
										 	</td>
										 	<td colspan="2" class="text14">
										 		<input onBlur="checkMandatoryFields()" type="text" class="inputTextMediumBlueMandatoryField" name="gomerk" id="gomerk" size="21" maxlength="20" value="">									 		
										 	</td>
										 	
										 </tr>
										 <tr height="10"><td>&nbsp;</td></tr>
										 <tr>
										 	<td colspan="3" class="text14"><span title="gomerb">&nbsp;Merknad 2</span></td>
										 	<td class="text14"><span title="gomerc">&nbsp;Merknad 3</span></td>
										 	<td class="text14"><span title="gomerd">&nbsp;Merknad 4</span></td>
										 </tr>
										 <tr>
										 	
										 	<td colspan="3" class="text14">
										 		<input type="text" class="inputTextMediumBlue" name="gomerb" id="gomerb" size="15" maxlength="20" value="">									 		
										 	</td>
											<td class="text14">
										 		<input type="text" class="inputTextMediumBlue" name="gomerc" id="gomerc" size="15" maxlength="20" value="">									 		
										 	</td>
										 	<td class="text14">
										 		<input type="text" class="inputTextMediumBlue" name="gomerd" id="gomerd" size="15" maxlength="20" value="">									 		
										 	</td>
										 	<%-- SUBMIT button --%>
			 								<c:if test="${record.gotrnr != '*SLETTET' && (!fn:contains(godsnr,'ERROR') && !fn:contains(godsnr,'FEIL')) }">
										 		<td align="left" class="text14" ><img onMouseOver="showPop('buttonLM_info');" onMouseOut="hidePop('buttonLM_info');"style="vertical-align:middle;" width="14px" height="14px" src="resources/images/info3.png" border="0" alt="info">
					 								<input disabled OnClick="setBlockUI();doUpdateMerknad();" class="inputFormSubmitGrayDisabled" type="button" name="buttonMerknadSubmit" id="buttonMerknadSubmit" value='Lagre Merknad' >
					 								<div class="text11" style="position: relative;" align="left">
													<span style="position:absolute; top:3px; width:100px" id="buttonLM_info" class="popupWithInputText"  >
														<font class="text11">
										           			<b>Knappen blir aktivert når de obligatoriske feltene er fylt ut</b>
									           			</font>
													</span>
													</div>	
					 							</td>
					 						</c:if>
										 </tr>
										</table>
										</form>
									</td>
					 			</tr>
					 			
				 			</table>
						 	</td>
			 			</tr>		
					</table>
				</td>
			</c:when>
			<c:otherwise>
				<td align="center" style="width:50%" valign="top" >&nbsp;</td>
			</c:otherwise>
			</c:choose>	
		</tr>
		
		<%-- ONLY with UPDATE --%>
		<c:if test="${not empty updateFlag}">
			<tr height="20"><td colspan="2">&nbsp;</td></tr>
			<tr >
				<td class="text14">&nbsp;
					<img style="vertical-align: bottom" src="resources/images/log-icon.gif" width="16" hight="16" border="0" alt="show log">
					<b>Hendelseslogg</b>
				</td>
				
			</tr>
			<tr>
				<td valign="top" style="width:50%" >
				<table style="width:90%" id="containerdatatableTable" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
					
					<table style="width:100%" id="hfLoggerList" class="display compact cell-border" >
						<thead>
						<tr class="tableHeaderField" >
							<th width="2%" nowrap align="center" class="tableHeaderFieldFirst12">&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohkod"/></th>
							<th align="left" class="tableHeaderField12">&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohusr"/></th>
							<th align="left" class="tableHeaderField12">&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohdat"/></th>
							<th align="left" class="tableHeaderField12">&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohtim"/></th>
							<th align="left" width="2%" class="tableHeaderField12"><spring:message code="systema.godsno.maintenance.godshf.gogn"/></th>
							<th align="left" width="2%" class="tableHeaderField12">&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gotrnr"/></th>
							<th nowrap align="center" class="tableHeaderField12">&nbsp;<spring:message code="systema.godsno.maintenance.godshf.gohpgm"/></th>
						</tr>
						</thead>
		                
		                <tbody>
			            <c:forEach items="${hfLoggerList}" var="record" varStatus="counter">    
			             <tr class="tableRow" >  
			               <td width="2%" align="center" class="tableCellFirst12" >${record.gohkod}</td>
			               <td width="2%" class="tableCell12" >${record.gohusr}</td>
			               <td width="2%" class="tableCell12" >${record.gohdat}</td>
			               <td width="2%" class="tableCell12" >${record.gohtim}</td>
			               <td align="left" width="2%" class="tableCell12" >${record.gogn}</td>
			               <td width="2%" class="tableCell12" style="color:navy;">${record.gotrnr}</td>
			               <td width="2%" align="center" class="tableCell12" >${record.gohpgm}</td>
		                 </tr>
		               	</c:forEach>
		            	</tbody>
					</table>
					</td>
				</tr>
				</table>
				</td>	
				
				
			</tr>
		</c:if>
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