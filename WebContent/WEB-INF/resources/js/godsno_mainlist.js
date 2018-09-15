  //this variable is a global jQuery var instead of using "$" all the time. Very handy
  var jq = jQuery.noConflict();
  var counterIndex = 0;
  
  jq(function() {
	  //General Header Menus
	  jq('#alinkMaintGate').click(function() { 
		  setBlockUI();
	  });
	  jq('#alinkGodsno').click(function() { 
		  setBlockUI();
	  });
	 
  });
  
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
	var lang = jq('#language').val();
	
	jq('#mainList').dataTable( {
	  "jQueryUI": false,
	  "dom": '<"top"lf>t<"bottom"ip><"clear">', //look at mainListFilter on JSP SCRIPT-tag
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
	  //var record = element.id.split('@');
	  //var avd = record[0];
	  //var record = element.id;
	  var id = element.id;
	  id= id.replace("id_","");
	  	//Start dialog
	  	jq('<div></div>').dialog({
	        modal: true,
	        title: "Slett Godsnr. " + id,
	        buttons: {
		        Fortsett: function() {
	        		jq( this ).dialog( "close" );
		            //do delete
	        		setBlockUI();
		            window.location = "godsno_delete.do?gogn=" + id;
		            
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
	 


  
  