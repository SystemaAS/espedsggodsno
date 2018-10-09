<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header ===========================-->
<jsp:include page="/WEB-INF/views/headerGodsno.jsp" />
<!-- =====================end header ==========================-->

	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
		specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>
	<SCRIPT type="text/javascript" src="resources/js/godsno_clone.js?ver=${user.versionEspedsg}"></SCRIPT>
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
			<td width="25%" valign="bottom" class="tab" align="center" nowrap>
				<img style="vertical-align:bottom;" src="resources/images/update.gif" border="0" alt="general list">
				<font class="tabLink">Godsregistrering - Tillegg av transittnr.</font>					
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
				<form action="godsno_clone.do" name="editForm" id="editForm" method="post">
				<input type="hidden" name="applicationUser" id="applicationUser" value='${user.user}'>
				<input type="hidden" name="action" id="action" value='${action}'>
				<input type="hidden" name="avd" id="avd" value='${avd}'>
				<input type="hidden" name="gogn" id="gogn" value="${record.gogn}">
				
				<table style="width:90%" align="left" border="0" cellspacing="0" cellpadding="0">
				 	<tr >
					 	<td >
						<table style="width:100%"  class="secondarySectionHeader" border="0" cellspacing="1" cellpadding="0">
					 		<tr height="15">
					 			<td class="text14White">&nbsp;Godsnr:&nbsp;<font style="color: white;"><b>${record.gogn}</b></font>
					 				&nbsp;&nbsp;&nbsp;<font style="color: yellow;">Tillegg av transitt på godsnummer</font>
					 			</td>
			 				</tr>
			            </table>
			            </td>
		            </tr>
		            <tr >
					 	<td>
						<table style="width:100%"  class="formFrame" border="0" cellspacing="2" cellpadding="1">
					 		<tr height="5px"><tb></tb></tr>				 		
					 		
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
					 				<input type="text" required oninvalid="this.setCustomValidity('Obligatorisk')" oninput="setCustomValidity('')" class="inputTextMediumBlueMandatoryField" name="gotrnr" id="gotrnr" size="21" maxlength="20" value="${record.gotrnr}">
					 				<font class="text16RedBold" >*</font>
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
					 			<td class="text14"><span title="owngoavg_ptype">T-papirtype</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="owngoavg_ptype" id="owngoavg_ptype" size="5" maxlength="3" value="${owngoavg_ptype}">
					 				
					 				<font class="text12" style="font-style: italic;">Blank=T1</font>
					 			</td>
					 		</tr>
					 		<tr >
					 			<td class="text14"><span title="owngoavg_toll">Avg.tollsted</span></td>
					 			<td class="text14">
					 				<input type="text" class="inputTextMediumBlue" name="owngoavg_toll" id="owngoavg_toll" size="15" maxlength="12" value="${owngoavg_toll}">
					 				<input type="text" tabindex=-1 readonly class="inputTextReadOnly" name="xreadyOnly" id="xreadyOnly" size="4" value="${owngoavg_ptype}">
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
			 				
			 				<%-- SUBMIT button --%>
			 				<c:if test="${record.gotrnr != '*SLETTET' && (!fn:contains(godsnr,'ERROR') && !fn:contains(godsnr,'FEIL')) }">
				 				<tr>
				 					<td colspan="10">
				 					<table style="width:95%" border="0" cellspacing="0" cellpadding="0">
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
            
				<td align="center" style="width:50%" valign="top" >&nbsp;</td>
			
		</tr>
		
		
		<tr height="20"><td colspan="2">&nbsp;</td></tr>
	</table> 
	</td>
 </tr>
 </table>
 </td>
 </tr>
 
 
 