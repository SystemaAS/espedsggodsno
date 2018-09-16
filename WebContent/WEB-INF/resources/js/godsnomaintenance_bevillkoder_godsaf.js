  //this variable is a global jQuery var instead of using "$" all the time. Very handy
  var jq = jQuery.noConflict();
  var counterIndex = 0;
  
  
  jq(function() {
	  //General Header Menus
	  jq('#alinkTopicOne').click(function() { 
		  setBlockUI();
	  });
	  jq('#alinkTopicTwo').click(function() { 
		  setBlockUI();
	  });
	  jq('#alinkGodsno').click(function() { 
		  setBlockUI();
	  });
	  
	  //Create new record
	  jq('#newRecordButton').click(function() {
		  //adjust	
		  jq('#gflavd').val("");
		  jq('#gflavd').prop('readonly', false);
		  jq('#gflavd').removeClass("inputTextReadOnly");
		  jq('#gflavd').addClass("inputTextMediumBlueMandatoryField");
		  //
		  jq('#gflbko').val("");
		  jq('#gfenh').val("");
		  //for a future update
		  jq('#updateId').val("");
			
	  });
	 
  });
  
  
  //Get specific record
  function getRecord(record){
		var rawId = record.id;
	  	var applicationUserParam = jq('#applicationUser').val();
	  	
	  	rawId = rawId.replace("recordUpdate_", "");
	  	var record = rawId.split('_');
		var id = record[0];
		
		jq.ajax({
	  	  type: 'GET',
	  	  url: 'getSpecificRecord_godsaf.do',
	  	  data: { applicationUser : jq('#applicationUser').val(), 
	  		  	  id : id },
	  	  dataType: 'json',
	  	  cache: false,
	  	  contentType: 'application/json',
	  	  success: function(data) {
		  	var len = data.length;
	  		for ( var i = 0; i < len; i++) {
	  			jq('#gflavd').val("");jq('#gflavd').val(data[i].gflavd);
	  			jq('#gflavd').prop('readonly', true);
	  			jq('#gflavd').removeClass("inputTextMediumBlueMandatoryField");
	  			jq('#gflavd').addClass("inputTextReadOnly");
	  			//rest of the gang
	  			jq('#gflbko').val("");jq('#gflbko').val(data[i].gflbko);
	  			jq('#gfenh').val("");jq('#gfenh').val(data[i].gfenh);
	  			
	  			//for a future update
	  			jq('#updateId').val("");jq('#updateId').val(data[i].gflbko);
	  			//enable submit
	  			//jq("#submit").prop("disabled", false);
	  			jq('#gflbko').focus();
	  		}
	  	  }, 
	  	  error: function() {
	  		  alert('Error loading ...');
	  	  }
		});
			
	  }
  
  
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
	  "dom": '<"top"f>t<"bottom"ip><"clear">', //look at mainListFilter on JSP SCRIPT-tag
	  "scrollY":     "500px",
  	  "scrollCollapse":  true,
  	  "tabIndex": -1,
  	  "order": [[ 1, "asc" ]],
	  "lengthMenu": [ 50, 100],
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
  
  
  //---------------------------------------
  //DELETE Order
  //This is done in order to present a jquery
  //Alert modal pop-up
  //----------------------------------------
  function doDeleteRecord(element){
	  //start
	  
	  	//Start dialog
	  	jq('<div></div>').dialog({
	        modal: true,
	        title: "Slett linje ",
	        buttons: {
		        Fortsett: function() {
	        		jq( this ).dialog( "close" );
		            //do delete
	        		deleteRecord(element.id)
		            
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

  
  //Delete specific record
  function deleteRecord(rawId){
		var applicationUserParam = jq('#applicationUser').val();
	  	
	  	rawId = rawId.replace("recordDelete_", "");
	  	var record = rawId.split('_');
		var id = record[0];
		
		jq.ajax({
	  	  type: 'GET',
	  	  url: 'deleteSpecificRecord_godsaf.do',
	  	  data: { applicationUser : jq('#applicationUser').val(), 
	  		  	  id : id },
	  	  dataType: 'json',
	  	  cache: false,
	  	  contentType: 'application/json',
	  	  success: function(data) {
		  	var len = data.length;
	  		for ( var i = 0; i < len; i++) {
	  			setBlockUI();
	  			window.location = "godsnomaintenance_bevillkoder_godsaf_edit.do";
	  		}
	  	  }, 
	  	  error: function() {
	  		  alert('Error loading ...');
	  	  }
		});
			
	  }
  
  