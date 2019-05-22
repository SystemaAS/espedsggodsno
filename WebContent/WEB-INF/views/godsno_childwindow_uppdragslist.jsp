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
				<table>
				<tr>
					<td class="text14">&nbsp;Godsnr.</td>
					<td class="text14" style="color:navy;"><b>${model.hegn}</b></td>
					<td class="text14">&nbsp;Transittnr.</td>
					<td class="text14" style="color:navy;"><b>${model.gotrnr}</b></td>
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
					
					<td class="ownScrollableSubWindowDynamicWidthHeight" width="100%" >
					 
					 
					<%-- this is the datatables grid (content)--%>
					<form action="N/A_is_done_with_jquery....?action=doFind" name="searchForm" id="searchForm" method="post">
							
					<table id="oppdragsList" class="display compact cell-border" width="100%" >
						<thead>
						<tr class="tableHeaderField" >
							<th width="2%" class="text14">&nbsp;Velg&nbsp;</th>
							<th width="2%" class="text14" title="Pos1">&nbsp;Pos1&nbsp;</th>
		                    <th width="2%" class="text14" title="Pos2">&nbsp;Pos2&nbsp;</th>
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
       			</table>
		</td>
		</tr>

	</table> 
