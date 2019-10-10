<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header =====================================-->
<jsp:include page="/WEB-INF/views/headerGodsnoChildWindows.jsp" />
<!-- =====================end header ====================================-->

	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
	specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>
	<SCRIPT type="text/javascript" src="resources/js/godsno_childwindows.js?ver=${user.versionEspedsg}"></SCRIPT>
	
	<style type = "text/css">
		.ui-datepicker { font-size:9pt;}
		/* this line will align the datatable search field in the left */
		.dataTables_wrapper .oppdragsListFilter .dataTables_filter{float:left}
		.dataTables_wrapper .oppdragsListFilter .dataTables_info{float:right}
	</style>
	
	<table class="tableBorderWithRoundCorners3D" cellspacing="0" border="0" cellpadding="0">
		<tr>
			<td>
				<table style="width:90%;">
				<tr>
					<td class="text14">&nbsp;Godsnr.</td>
					<td class="text14" style="color:navy;"><b>${model.hegn}</b></td>
					<td class="text14">&nbsp;Transittnr.</td>
					<td class="text14" style="color:navy;"><b>${model.gotrnr}</b></td>
					<td class="text14" align="center">
						<form name="offsetForm" id="offsetForm" action="godsno_childwindow_uppdragslist.do?action=doFind" method="post" >
							<input type="hidden" name="hegn" id="hegn" value='${model.hegn}'>
							<input type="hidden" name="gotrnr" id="gotrnr" value='${model.gotrnr}'>
							<c:choose>
								<c:when test="${not empty model.opd_offset}">
									<input checked onChange="setBlockUI();this.form.submit();" style="cursor:pointer" title="Alle oppdrag och MRN" tabindex=-1 type="checkbox" id="opd_offset" name="opd_offset" value="1" >
								</c:when>
								<c:otherwise>
									<input onChange="setBlockUI();this.form.submit();" style="cursor:pointer" title="Alle oppdrag och MRN" tabindex=-1 type="checkbox" id="opd_offset" name="opd_offset" value="1" >
								</c:otherwise>
							</c:choose>
							<label class="isa_warning" for = "opd_offset">Vis alle pos (selv om allerede plukket)</label>
						</form>
					</td>							
				</tr>
				
				<tr>
					<td colspan="5" class="text12" style="color:navy;"><i>&nbsp;Koble posisjoner til (avvikende) transitnr.</i></td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
		<td valign="top">
		
		  		<%-- this container table is necessary in order to separate the datatables element and the frame above, otherwise
			 	the cosmetic frame will not follow the whole datatable grid including the search field... --%>
				<table id="containerdatatableTable" cellspacing="2" align="left" width="100%" >
					<tr height="20"><td></td></tr>
					
					<tr class="text11" >
					
					<td class="ownScrollableSubWindowDynamicWidthHeight" style="width:100%" >
					 
					 
					<%-- this is the datatables grid (content)--%>
					<form action="N/A_is_done_with_jquery....?action=doFind" name="searchForm" id="searchForm" method="post">
							
					<table id="oppdragsList" class="display compact cell-border" style="width:100%" >
						<thead>
						<tr class="tableHeaderField" >
							<th title="Velg alla" nowrap>
								<font class="text12" style="font-weight:bold;">Alle</font>
								<input style="cursor:pointer;" class="text12" type="checkbox" id="selectAll" >
							</th>
							
							<th width="2%" class="text14" title="Pos1">&nbsp;Pos1&nbsp;</th>
		                    <th width="2%" class="text14" title="Pos2">&nbsp;Pos2&nbsp;</th>
		                    <th align="right" class="text14" title="Avd">&nbsp;Avd.&nbsp;</th>
		                    <th align="left" class="text14" title="Oppdrag">&nbsp;Oppd.&nbsp;</th>
		                    <th align="left" class="text14" title="Fortollingsoppdrag">&nbsp;Fortoll.opp.&nbsp;</th>
		                    <th class="text14" title="Selger">&nbsp;Selger&nbsp;</th>
		                    <th class="text14" title="Kjøper">&nbsp;Kjøper&nbsp;</th>
		                    <th width="2%" class="text14" title="Agentnr.">&nbsp;Ag.nr.&nbsp;</th>
		                    <th class="text14" title="Agentnavn">&nbsp;Ag.navn&nbsp;</th>
		                </tr> 
		                </thead>
		                
		                <tbody>
		                <c:forEach var="record" items="${model.list}" varStatus="counter">
		                	<tr >
				           	<td width="2%" align="center" class="text14" >
			               		<c:choose>
				               		<c:when test="${not empty record.hepos1}">
				               			<input style="cursor:pointer;" class="clazzSelectionAware" type="checkbox" value="J" id="godsno${record.hegn}_pos1${record.hepos1}_id${counter.count}" name="godsno${record.hegn}_pos1${record.hepos1}_id${counter.count}" >
				               		</c:when>
				               		<c:otherwise>
				               			<img title="already picked" style="cursor:pointer;" src="resources/images/lock.gif" border="0" alt="edit">	
				               		</c:otherwise>
			               		</c:choose>
			               	</td>
			               <td width="2%" align="center" class="text14NoneColor">&nbsp;${record.hepos1}</td>
			               <td width="2%" align="center" class="text14NoneColor">&nbsp;${record.hepos2}</td>
			               <td align="right" class="text14NoneColor">&nbsp;${record.heavd}</td>
			               <td align="left" class="text14NoneColor">&nbsp;${record.heopd}</td>
			               <td align="center" title="${record.hepk4}" class="text14NoneColor">
			               		<c:choose>
			               			<c:when test="${not empty record.hepk4}">
				               			<c:choose>
					               			<c:when test="${record.hepk4 == 'J' || record.hepk4 == 'P'}">
					               				<font style="color:green"><b>JA</b></font>
					               			</c:when>
					               			<c:otherwise>
					               				&nbsp;
					               			</c:otherwise>
				               			</c:choose>
			               			</c:when>
			               			<c:otherwise>
			               				&nbsp;
			               			</c:otherwise>
			               		</c:choose>
			               		
			               	</td>
			               <td class="text14NoneColor">&nbsp;${record.henas}</td>
		               	   <td class="text14NoneColor">&nbsp;${record.henak}</td>
		               	   <td width="2%" align="center" class="text14NoneColor">&nbsp;${record.hekna}</td>
		               	   <td class="text14NoneColor">&nbsp;${record.heknaName}</td>
			            </tr> 
			            </c:forEach>
			            </tbody>
		            </table>
		            </form>
	    	        </td>
	    	        
           		</tr>
           		<tr height="10"><td></td></tr>
           		<tr>
		          <td align="left">
		          		&nbsp;<input class="inputFormSubmit" type="button" name="buttonPick" id="buttonPick" value='Plukk'>
		          		<%--
		          		&nbsp;&nbsp;&nbsp;<input class="inputFormSubmit" type="button" name="buttonCloseOk" id="buttonCloseOk" value='Importera'>
		          		 --%>
		          		&nbsp;<input class="inputFormSubmit" type="button" name="cancel" id="cancel" value='Avbryt'>
		          </td>
		         </tr>
		         
		         <c:if test="${not empty model.opd_offset}">
			         <tr height="10"><td></td></tr>
			         <tr><td><h3>Ekstra info.</h3></td></tr>
			         <table id="extraTrnrList" class="display compact cell-border" style="width:100%" >
							<thead>
							<tr class="tableHeaderField" >
								<th class="tableHeaderFieldFirst12">&nbsp;Godsnr.&nbsp;</th>
								<th class="tableHeaderField12">&nbsp;Transittnr.&nbsp;</th>
								<th class="tableHeaderField12">&nbsp;Pos1&nbsp;</th>
								<th class="tableHeaderField12">&nbsp;Pos2&nbsp;</th>
			                    
			                </tr> 
			                </thead>
			                
			                <tbody>
			                <c:forEach var="record" items="${model.trnrList}" varStatus="counter">
			                	<tr >
					           <td align="center" class="tableCellFirst12" >${record.gtgn}</td>
				               <td align="center" class="tableCell12" >${record.gttrnr}</td>
				               <td align="left" class="tableCell12" >${record.gtpos1}</td>
				               <td align="left" class="tableCell12" >${record.gtpos2}</td>
				               
				               
				            </tr> 
				            </c:forEach>
				            </tbody>
			            </table>
		          </c:if>
		         
		         
		         
		         
       			</table>
		</td>
		</tr>

	</table> 
