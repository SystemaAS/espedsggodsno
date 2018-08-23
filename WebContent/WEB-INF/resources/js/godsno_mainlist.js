  //this variable is a global jQuery var instead of using "$" all the time. Very handy
  var jq = jQuery.noConflict();
  var counterIndex = 0;
  
  
  jq(function() {
      jq('#searchForm').submit(function() {
    	  setBlockUI();
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

  jq(document).ready(function() {
    //init table (no ajax, no columns since the payload is already there by means of HTML produced on the back-end)
	jq('#mainList').dataTable( {
	  "jQueryUI": false,
	  "dom": '<"mainListFilter"f>t<"bottom"lip><"clear">', //look at mainListFilter on JSP SCRIPT-tag
	  "order": [[ 1, "desc" ]],
	  "lengthMenu": [ 75, 100, 200]
	} );
	//css styling
    jq('.dataTables_filter input').addClass("inputText12LightYellow");
    
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
					 		jq.blockUI({ css: { fontSize: '22px' }, message: BLOCKUI_OVERLAY_MESSAGE_DEFAULT});
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

	 


  
  