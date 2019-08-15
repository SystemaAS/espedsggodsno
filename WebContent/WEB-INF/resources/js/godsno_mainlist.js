  //this variable is a global jQuery var instead of using "$" all the time. Very handy
  var jq = jQuery.noConflict();
  var counterIndex = 0;
  var BLOCKUI_OVERLAY_MESSAGE_DEFAULT = "Please wait...";
  
  jq(function() {
	  //General Header Menus
	  jq('#alinkMaintGate').click(function() { 
		  setBlockUI();
	  });
	  jq('#alinkGodsno').click(function() { 
		  setBlockUI();
	  });
	  jq("#fromDayUserInput").datepicker({ 
		  dateFormat: 'ddmmy',
		  onSelect: function (selected) {
			  jq('#imgIdFromDayUserInput').click(); 
		     }
	  });
	  jq("#toDayUserInput").datepicker({ 
		  dateFormat: 'ddmmy',
		  onSelect: function (selected) {
			  jq('#imgIdToDayUserInput').click(); 
		     }
	  });
	 
  });
  
  jq(function() {
      jq('#searchForm').submit(function() {
    	  setBlockUI();
  	  }); 
      
      jq('#imgIdFromDayUserInput').click(function() {
    	  jq('#dnrFromDate').text("");
    	  //default
    	  var date = new Date();
    	  //override if applicable
    	  if(jq('#fromDayUserInput').val()!=''){
    		  var str = jq('#fromDayUserInput').val();
    		  if(str.length==6){
    			  var day = str.substring(0,2);
    			  var month = str.substring(2,4);
    			  var year = str.substring(4,6);
    			  var isoDate = "20" + year + "-" + month + "-" + day;
    			  //console.log(isoDate);
    			  date = new Date(isoDate);
    		  }
    		  
    	  }
    	  jq('#dnrFromDate').text(getDayNrInYear(date));
      });
      
      jq('#imgIdToDayUserInput').click(function() {
    	  jq('#dnrToDate').text("");
    	  //default
    	  var date = new Date();
    	  //override if applicable
    	  if(jq('#toDayUserInput').val()!=''){
    		  var str = jq('#toDayUserInput').val();
    		  if(str.length==6){
    			  var day = str.substring(0,2);
    			  var month = str.substring(2,4);
    			  var year = str.substring(4,6);
    			  var isoDate = "20" + year + "-" + month + "-" + day;
    			  //console.log(isoDate);
    			  date = new Date(isoDate);
    		  }
    		  
    	  }
    	  jq('#dnrToDate').text(getDayNrInYear(date));
      });
      
      jq('#dnrFromDate').click(function() {
    	  jq('#dnrFromDate').text("");
      });
      jq('#dnrToDate').click(function() {
    	  jq('#dnrToDate').text("");
      });
      
      
  });    
  
  //-------------------
  //Datatables jquery
  //-------------------
  //private function
  
  function filtersInit () {
    jq('#mainList').DataTable().search(
    		jq('#mainList_filter').val()
    ).draw();
  }

  
 
  function getDayNrInYear(date){
	  var today = date;
	  var dayOfYear = Math.ceil((today - new Date(today.getFullYear(),0,1)) / 86400000);
	  return dayOfYear;
  }
  
  jq(document).ready(function() {
	  
	  //init table (no ajax, no columns since the payload is already there by means of HTML produced on the back-end)
	var lang = jq('#language').val();
	
	jq('#mainList').dataTable( {
	  "jQueryUI": false,
	  "searchHighlight": true,
	  "dom": '<"mainListFilter"if>t<"bottom"lp><"clear">', //look at mainListFilter on JSP SCRIPT-tag
	  "scrollY":     "700px",
  	  "scrollCollapse":  true,
  	  "tabIndex": -1,
  	  "order": [[ 1, "desc" ]],
	  "lengthMenu": [ 75, 100, 200],
	  "language": {
		  "url": getLanguage(lang)
      },
	  "fnDrawCallback": function( oSettings ) {
    	jq('.dataTables_filter input').addClass("inputText12LightYellow");
	  }
	} );
	//css styling not working with language localization. We must use fnDrawCallback function above
    //jq('.dataTables_filter input').addClass("inputText12LightYellow");
    
   
    //event on input field for search
    jq('input.mainList_filter').on( 'keyup click', function () {
    		filtersInit();
    } );
  } );
  
  
  //----------------------------------------  
  //START Model dialog "Create new order"
  //--------------------------------------
  //Initialize <div> here
  jq(function() { 
	  jq("#dialogCreateNewOrder").dialog({
		  autoOpen: false,
		  maxWidth:400,
          maxHeight: 220,
          width: 300,
          height: 250,
		  modal: true
	  });
  });
  
  //Present dialog box onClick (href in parent JSP)
  jq(function() {
	  jq("#createNewOrderTabIdLink").click(function() {
		  
		  //setters (add more if needed)
		  jq('#dialogCreateNewOrder').dialog( "option", "title", "Lage ny " );
		  
		  //deal with buttons for this modal window
		  jq('#dialogCreateNewOrder').dialog({
			 buttons: [ 
	            {
				 id: "dialogSaveTU",	
				 text: "Fortsett",
				 click: function(){
					 		setBlockUI();
			 				jq('#createNewOrderForm').submit();
				 		}
			 	 },
	 	 		{
			 	 id: "dialogCancelTU",
			 	 text: "Avbryt", 
				 click: function(){
					 		//back to initial state of form elements on modal dialog
					 		//jq("#dialogSaveSU").button("option", "disabled", true);
					 		
					 		jq( this ).dialog( "close" );
					 		jq("#opd").focus();
				 		} 
	 	 		 } ] 
		  });
		  //init values
		  //jq("#dialogSaveTU").button("option", "disabled", false);
		  //open now
		  jq('#dialogCreateNewOrder').dialog('open');
	  });
  });
  //-----------------------------
  //END Create new order - Dialog
  //-----------------------------

  
  //---------------------------------------
  //DELETE Order
  //This is done in order to present a jquery
  //Alert modal pop-up
  //----------------------------------------
  function doDeleteOrder(element){
	  //start
	  var record = element.id.split('@');
	  var id = record[0];
	  var id2 = record[1];
	  id = id.replace("id_","");
	  id2 = id2.replace("id2_","");
	  
	  	//Start dialog
	  	jq('<div></div>').dialog({
	        modal: true,
	        title: "Slett Godsnr. " + id + " - Transittnr " + id2,
	        width: 500,
	        buttons: {
		        Fortsett: function() {
	        		jq( this ).dialog( "close" );
		            //do delete
	        		setBlockUI();
		            window.location = "godsno_delete.do?gogn=" + id + "&gotrnr=" + id2;
		            
		        },
		        Avbryt: function() {
		            jq( this ).dialog( "close" );
		        }
	        },
	        open: function() {
		  		  var markup = "Er du sikker på at du vil slette denne?";
		          jq(this).html(markup);
		          //make Cancel the default button
		          jq(this).siblings('.ui-dialog-buttonpane').find('button:eq(1)').focus();
		     }
		});  //end dialog
  }	 
  
//-----------------------------------
  //START Model dialog Print Merknad
  //-----------------------------------
  //Initialize <div> here
  /*
  jq(function() { 
	  jq( ".clazz_dialog" ).each(function(){
		jq(this).dialog({
			autoOpen: false,
			modal: true
		});
	  });
  });
//Present dialog box onClick (href in parent JSP)
  jq(function() {
	  jq(".printMerknaderLink").click(function() {
		  var id = this.id;
		  counterIndex = id.replace("printMerknaderLink","");
		  //setters (add more if needed)
		  jq('#dialogPrintMerknader'+counterIndex).dialog( "option", "title", "Generere merknadsjournal");// + jq('#printMerknaderGogn'+counterIndex).val() );
		  
		  //deal with buttons for this modal window
		  jq('#dialogPrintMerknader'+counterIndex).dialog({
			 buttons: [ 
	            {
				 id: "dialogSave"+counterIndex,	
				 text: "Fortsett",
				 click: function(){
					 		//jq('#copyForm'+counterIndex).submit();
					 		executePrintMerknadGognPgm(counterIndex);
				 		}
			 	 },
	 	 		{
			 	 id: "dialogCancel"+counterIndex,
			 	 text: "Avbryt", 
				 click: function(){
					 		//back to initial state of form elements on modal dialog
					 		jq("#dialogSave"+counterIndex).button("option", "disabled", false);
					 		jq( this ).dialog( "close" ); 
					 		  
				 		} 
	 	 		 } ] 
			  
		  });
		  //init values
		  //jq("#dialogSave"+counterIndex).button("option", "disabled", true);
		  
		  //open now
		  jq('#dialogPrintMerknader'+counterIndex).dialog('open');
		 
	  });
  });
	
  function executePrintMerknadGognPgm(counterIndex){
	  //do it
	  jq.ajax({
	  	  type: 'GET',
	  	  url: 'printMerknaderSpecificGogn.do',
	  	  data: { applicationUser : jq('#applicationUser').val(),
	  		  	  gogn : jq('#printMerknaderGogn'+counterIndex).val(),
	  		  	  type : "P" },
	  	  dataType: 'json',
	  	  cache: false,
	  	  contentType: 'application/json',
	  	  success: function(data) {
	  		var len = data.length;
	  		
	  		for ( var i = 0; i < len; i++) {
	  			if(data[i].errMsg != ''){
	  				jq("#printMerknaderStatus" + counterIndex).removeClass( "isa_success" );
	  				jq("#printMerknaderStatus" + counterIndex).addClass( "isa_error" );
	  				jq("#printMerknaderStatus" + counterIndex).text("Error: " + data[i].errMsg);
	  			}else{
	  				jq("#printMerknaderStatus" + counterIndex).removeClass( "isa_error" );
	  				jq("#printMerknaderStatus" + counterIndex).addClass( "isa_success" );
	  				jq("#printMerknaderStatus" + counterIndex).text(data[i].lenke);
	  				//place the link inside the lable
	  				var oHtml = jq("#printMerknaderStatus" + counterIndex).html();
	  				var docLink = "<a href='godsno_renderFile.do?fp=" + data[i].lenke + "'" + " target='_new' > " + oHtml + " </a>"; 
	  				jq("#printMerknaderStatus" + counterIndex).html(docLink);
	  	
	  			}
	  		}
	  	  },
	  	  error: function() {
	  	    alert('Error loading on Ajax callback (?) sendSMS...check js');
	  	  }
	  });
	  
  }
*/
  
  function executePrintMerknadGognPgm(element){
	  var id = element.id;
	  var counterIndex = id.replace("printMerknaderLink","");
	  //do it
	  setBlockUI();
	  
	  jq.ajax({
	  	  type: 'GET',
	  	  url: 'printMerknaderSpecificGogn.do',
	  	  data: { applicationUser : jq('#applicationUser').val(),
	  		  	  gogn : jq('#printMerknaderGogn'+counterIndex).val(),
	  		  	  type : "P" },
	  	  dataType: 'json',
	  	  cache: false,
	  	  contentType: 'application/json',
	  	  success: function(data) {
	  		var len = data.length;
	  		
	  		for ( var i = 0; i < len; i++) {
	  			if(data[i].errMsg != ''){
	  				//Do something
	  			}else{
	  				window.open('godsno_renderFile.do?fp=' + data[i].lenke, '_blank');
	  			}
	  		}
	  		jq.unblockUI();
	  	  },
	  	  error: function() {
	  		jq.unblockUI();  
	  	    alert('Error loading on Ajax callback (?) executePrintMerknadGognPgm...check js');
	  	    jq.unblockUI();
	  	  }
	  	  
	  });

  }
  

  
  