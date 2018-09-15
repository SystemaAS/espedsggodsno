<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- ======================= header ===========================-->
<jsp:include page="/WEB-INF/views/headerGodsnoMaintenance.jsp" />
<!-- =====================end header ==========================-->
	<%-- specific jQuery functions for this JSP (must reside under the resource map since this has been
		specified in servlet.xml as static <mvc:resources mapping="/resources/**" location="WEB-INF/resources/" order="1"/> --%>
	<SCRIPT type="text/javascript" src="resources/js/godsnomaintenance_bevillkoder.js?ver=${user.versionEspedsg}"></SCRIPT>	
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
	
	<style type = "text/css">
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
					<td style="width:20%" valign="bottom" class="tab" align="center" nowrap>
						<img style="vertical-align: middle;"  src="resources/images/list.gif" border="0" alt="general list">
						<font class="tabLink">&nbsp;Bevill.koder - Vedlikehold</font>&nbsp;
					</td>
					<td style="width:80%" class="tabFantomSpace" align="center" nowrap><font class="tabDisabledLink">&nbsp;</font></td>	
				</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<%-- space separator --%>
	 		<table width="100%" class="tabThinBorderWhite" border="0" cellspacing="0" cellpadding="0">
	 		<input type="hidden" name="applicationUser" id="applicationUser" value='${user.user}'>
			<input type="hidden" name="language" id="language" value='${user.usrLang}'>
	 	    
	 	    <tr height="20"><td>&nbsp;</td></tr>
	 	    
			<%-- list component --%>
			<tr>
				<td width="3%">&nbsp;</td>
					
				<td width="100%">
				<table id="containerdatatableTable" style="width:80%"  cellspacing="1" border="0" align="left">
			    	    <tr>
						<td class="text14">
						<table id="mainList" class="display compact cell-border" >
							<thead>
							<tr>
								<th width="2%" class="tableHeaderFieldFirst" align="center" >Id</th>
								<th width="2%" class="tableHeaderField" align="center" >Lage</th>
			                    <th class="tableHeaderField" align="left" >&nbsp;Beskrivelse&nbsp;</th>
			                    <%--
			                    <th class="tableHeaderField" align="left" >&nbsp;Kode&nbsp;</th>
								<th class="tableHeaderField" align="left" >&nbsp;Text&nbsp;</th>
			                    <th class="tableHeaderField" align="center" >&nbsp;Status&nbsp;</th>
			                     --%>
			                </tr>  
			                </thead> 
			                <tbody >  
				            <c:forEach var="record" items="${list}" varStatus="counter">   
				               <tr class="tableRow" height="20" >
				              
				               <td width="2%" class="tableCellFirst" style="border-style: solid;border-width: 0px 1px 1px 1px;border-color:#FAEBD7;" align="center" ><font class="text14">&nbsp;${record.id}&nbsp;</font></td>
				               <td width="2%" class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 0px;border-color:#FAEBD7;" align="center">
				               		<a id="alinkRecordId_${counter.count}" onClick="setBlockUI(this);" href="godsnomaintenance_bevillkoder_${record.pgm}.do?id=${record.dbTable}">
	               						<img src="resources/images/update.gif" border="0" alt="edit">
				               		</a>
				               </td>
				               <td class="tableCell" style="border-style: solid;border-width: 0px 1px 1px 0px;border-color:#FAEBD7;"  >
				               		<font class="text14">&nbsp;&nbsp;${record.subject}&nbsp;</font>
					               		
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
	 		</table>
		</td>
	</tr>
</table>	

<!-- ======================= footer ===========================-->
<jsp:include page="/WEB-INF/views/footer.jsp" />
<!-- =====================end footer ==========================-->

