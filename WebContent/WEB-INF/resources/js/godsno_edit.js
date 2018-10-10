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
	 
  });
  
  jq(function() {
	  jq("#gogrdt").datepicker({ 
		  dateFormat: 'ddmmy'
	  });
	  jq("#golsdt").datepicker({ 
		  dateFormat: 'ddmmy'
	  });
	  jq("#gotrdt").datepicker({ 
		  dateFormat: 'ddmmy'
	  });
	  
	  jq('#gobiln').focus(function() {
	    	if(jq('#gobiln').val()!=''){
	    		refreshCustomValidity(jq('#gobiln')[0]);
	  		}
	  	});
	  jq('#gomott').focus(function() {
	    	if(jq('#gomott').val()!=''){
	    		refreshCustomValidity(jq('#gomott')[0]);
	  		}
	  	});
	  jq('#gogn').focus(function() {
	    	if(jq('#gogn').val()!=''){
	    		refreshCustomValidity(jq('#gogn')[0]);
	  		}
	  	});
	  //Special cases on gogn
	  jq('#owngogn_2').focus(function() {
	    	if(jq('#owngogn_2').val()!=''){
	    		refreshCustomValidity(jq('#owngogn_2')[0]);
	  		}
	  	});
	  
	  jq('#owngogn_2').on('change', function(){
		  var rawValue = this.value;
		  var record = rawValue.split("_");
		  var bevKode = record[0];
		  var enh = '0'; //default
		  if(record[1]!=''){
			  enh = record[1];
		  }
		  //console.log(bevKode + "XX" + enh);
		  var tmpGogn = jq('#owngogn_1').val() + bevKode + jq('#owngogn_3').val() + enh ;
		  jq('#tmpGogn').val(tmpGogn + "xx");//The suffix xx will be known after the "Save" and calculation of the whole gogn
		  
	  });
  });
  
  
  
  jq(function() {
      jq('#editForm').submit(function() { 
    	  setBlockUI();
  	  });
      //info list on bev.koder
      jq("#dialogBevKoderReadOnly").dialog({
		  autoOpen: false,
		  maxWidth:350,
          maxHeight: 600,
          width: 300,
          height: 350,
		  //modal: true,
		  dialogClass: 'main-dialog-class',
		  
	  });
      
 	  //Read only dialog
	  jq("#bevKoderDialogImgReadOnly").click(function() {
		  jq('#dialogBevKoderReadOnly').dialog( "option", "title", "Avd/Bev.koder" );
		  //deal with buttons for this modal window
		  jq('#dialogBevKoderReadOnly').dialog({
			 buttons: [ 
	            {
				 id: "dialogSaveTU",	
				 text: "Lukk",
				 click: function(){
					 		jq( this ).dialog( "close" );
			 			}
			 	 } ]
		  });
		  //open now
		  jq('#dialogBevKoderReadOnly').dialog('open');
	  });
	  //div for bev.koder main table (as in maintenance table)
	  jq('#divBevKodeListDialogImgReadOnly').click(function() {
		  if(jq('#divBevKodeList').css('display') == 'none'){
			  jq('#divBevKodeList').css('display','block');
		  }else{
			  jq('#divBevKodeList').css('display','none');
		  }
	  });
    	  

  });    
  
  function doPickBevKode(element){
	  jq('#divBevKodeList').css('display','none');
	  var rawId = element.id;
	  var record = rawId.split("@");
	  var id = record[0].replace("id_","");
	  var id2 = record[1].replace("id2_","");
	  //choose drop-down value
	  jq('#owngogn_2').val(id + "_" + id2).change();
	  jq('#owngogn_2').focus();
  }
 
  //-------------------
  //Datatables jquery
  //-------------------
  jq(document).ready(function() {
	jq('#merknadList').dataTable( {
	  "jQueryUI": false,
	  "dom": '<"top">t<"bottom"ip><"clear">', //look at mainListFilter on JSP SCRIPT-tag
	  "scrollY":  "300px",
  	  "scrollCollapse":  true,
  	  "tabIndex": -1,
  	  "order": [[ 1, "asc" ]],
	  
	} );

    //event on input field for search
    jq('input.merknadList_filter').on( 'keyup click', function () {
		jq('#merknadList').DataTable().search(
			jq('#merknadList_filter').val()
	    ).draw();
    } );
    
    jq('#hfLoggerList').dataTable( {
  	  "jQueryUI": false,
  	  "dom": '<"top">t<"bottom"ip><"clear">', //look at mainListFilter on JSP SCRIPT-tag
  	  "scrollY":  "250px",
    	  "scrollCollapse":  true,
    	  "tabIndex": -1,
    	  "order": [[ 2, "desc" ],[3, "desc" ]]
  	} );

      //event on input field for search
      jq('input.hfLoggerList_filter').on( 'keyup click', function () {
    	  jq('#hfLoggerList').DataTable().search(
			jq('#hfLoggerList_filter').val()
		   ).draw();
      } );
  } );
  //Check mandatory
  function checkMandatoryFields(){
	  var flag = -1
	  if(jq('#gomkod').val()!=''){
		  if(jq('#gopos').val()!=''){
			  if(jq('#goantk').val()!=''){
				  if(jq('#govsla').val()!=''){
					  if(jq('#gosted').val()!=''){
						  if(jq('#gomotm').val()!=''){
							  if(jq('#gomerk').val()!=''){
								  if(jq('#updateMerknad_flag').val()!=''){
									  enableButtonMerknadSubmit();
									  flag = 0;
								  }else{
									  //with new record
									  if(!jq('#gopos').is(".isa_error")){
										  enableButtonMerknadSubmit();
										  flag = 0;
										  //console.log("A");
									  }								  
								  }
							  }
						  }
					  } 
				  }
			  } 
		  } 
	  }
	  if(flag == -1){
		  jq('#buttonMerknadSubmit').prop('disabled', true);
		  disableButtonMerknadSubmit();
	  }else{
		  jq('#buttonMerknadSubmit').prop('disabled',false);
		  enableButtonMerknadSubmit();
	  }

  }
  function enableButtonMerknadSubmit(){
	  jq("#buttonMerknadSubmit").removeClass("inputFormSubmitGrayDisabled");
	  jq("#buttonMerknadSubmit").addClass("inputFormSubmit");
  }
  function disableButtonMerknadSubmit(){
	  jq("#buttonMerknadSubmit").removeClass("inputFormSubmit");
	  jq("#buttonMerknadSubmit").addClass("inputFormSubmitGrayDisabled");
  }
  
  //Get record - Merknad for update 
  function getRecordMerknad(record){
	  var id = record.id;
	  var fields = id.split('@');
	  var gogn = fields[0];
	  var gotrnr = fields[1];
	  var gopos = fields[2];
	  gogn = gogn.replace("gogn_","");
	  gotrnr = gotrnr.replace("gotrnr_","");
	  gopos = gopos.replace("gopos_","");
	  
	  jq.ajax({
	        type        : 'GET',
	        url         : 'getSpecificRecord_merknf.do',
	        data		: { applicationUser : jq('#applicationUser').val(), 
	        				gogn : gogn,
	        				gotrnr : gotrnr,
	        				gopos : gopos },
	        dataType    : 'json',
	        cache: false,
	        contentType : 'application/json',
	        success     : function(data){
	        	var len = data.length;
	        	for ( var i = 0; i < len; i++) {
	        		//special treatment for key gopos
		  			jq('#gopos').val(data[i].gopos);
		  			//jq('#gopos').prop('readonly', true);
			  		//jq('#gopos').removeClass("inputTextMediumBlueMandatoryField");
			  		//jq('#gopos').addClass("inputTextReadOnly");
		  			//rest of the gang
			  		jq('#gomkod').val(data[i].gomkod).change();
		  			jq('#goantk').val(data[i].goantk);
		  			jq('#govsla').val(data[i].govsla);
		  			jq('#gomer1').val(data[i].gomer1);
		  			jq('#gosted').val(data[i].gosted);
		  			jq('#gopos2').val(data[i].gopos2);
		  			jq('#gomotm').val(data[i].gomotm);
		  			jq('#gomerk').val(data[i].gomerk);
		  			jq('#gomerb').val(data[i].gomerb);
		  			jq('#gomerc').val(data[i].gomerc);
		  			jq('#gomerd').val(data[i].gomerd);
		  			//
		  			jq('#updateMerknad_flag').val('1');
	        		//END
		  			jq('#gomkod').focus();
		  			jq('#buttonMerknadSubmit').prop('disabled',false);
		  		}
	        },
          error: function() {
		  		  //alert('Error loading ...');
        	  console.log('Error loading');
        	  
			  }
	    })
  }
  
  //Get record - Merknad for update 
  function doDeleteMerknad(record){
	  var id = record.id;
	  var fields = id.split('@');
	  var gogn = fields[0];
	  var gotrnr = fields[1];
	  var gopos = fields[2];
	  gogn = gogn.replace("gogn_","");
	  gotrnr = gotrnr.replace("gotrnr_","");
	  gopos = gopos.replace("gopos_","");
	  	
	  jq.ajax({
	        type        : 'GET',
	        url         : 'deleteSpecificRecord_merknf.do',
	        data		: { applicationUser : jq('#applicationUser').val(), 
	        				gogn : gogn,
	        				gotrnr : gotrnr,
	        				gopos : gopos },
	        dataType    : 'json',
	        cache: false,
	        contentType : 'application/json',
	        success     : function(data){
	        	var len = data.length;
	        	for ( var i = 0; i < len; i++) {
	        		//console.log("B");
	        		window.location.href = 'godsno_edit.do?updateFlag=1&gogn=' + jq("#gogn").val() + '&gotrnr=' + jq("#gotrnr").val();
		  		}
	        },
          error: function() {
		  		  //alert('Error loading ...');
        	  console.log('Error loading');
        	  
			  }
	    })
	    
  }
  
  //SAVE merknad line
  function doUpdateMerknad() {
	  	var form = new FormData(document.getElementById('editMerknadForm'));
	  	//add values to form since we do not combine form data and other data in the same ajax call.
	  	//all fields in the form MUST exists in the DTO or DAO in the rest-Controller
	  	form.append("applicationUserMerknf", jq('#applicationUser').val());
	  	form.append("gogn", jq('#gogn').val());
	  	form.append("gotrnr", jq('#gotrnr').val());
	  	var payload = jq('editMerknadForm').serialize();
	  	
	    jq.ajax({
	        type        : 'POST',
	        url         : 'updateSpecificRecord_merknf.do?' + payload,
	        data        : form,
	        dataType    : 'text',
	        cache: false,
	  	  	processData: false,
	        contentType : false,
	        success     : function(data){
	        		//console.log("B");
	        		window.location.href = 'godsno_edit.do?updateFlag=1&gogn=' + jq("#gogn").val() + '&gotrnr=' + jq("#gotrnr").val();
	        		
             },
             error: function() {
		  		  //alert('Error loading ...');
            	  console.log('Error loading');
            	  
			  }
	    });
	}
  
  
  //Delete values Merknad
  jq(function() {
	  	jq('#newRecordButton').click(function() {
	  	//for a future update
	    jq('#updateMerknad_flag').val("");	
	  	//adjust	
	  	//jq('#gopos').val("");
	  	//jq('#gopos').prop('readonly', false);
	  	//jq('#gopos').removeClass("inputTextReadOnly");
	  	//jq('#gopos').addClass("inputTextMediumBlueMandatoryField");
		//rest of the gang
  		jq('#gomkod').val("DI").change();
		jq('#goantk').val("");
		jq('#govsla').val("");
		jq('#gomer1').val("");
		jq('#gosted').val("");
		jq('#gopos2').val("");
		jq('#gomotm').val("");
		jq('#gomerk').val("");
		jq('#gomerb').val("");
		jq('#gomerc').val("");
		jq('#gomerd').val("");
		//END
		jq('#gomkod').focus();
		jq('#buttonMerknadSubmit').prop('disabled',true);
		disableButtonMerknadSubmit();
	  });	
  });
  
  //check for duplicates Merknad and refresh datatable
  jq(function() {
	  jq('#gopos').blur(function(){
		    //only for new records and not for those being updated
		  	/*OBSOLETE since the field is now readyonly ...
		  	if(jq('#updateMerknad_flag').val()==''){
			    jq.ajax({
			        type        : 'GET',
			        url         : 'getSpecificRecord_merknf.do',
			        data		: { applicationUser : jq('#applicationUser').val(), 
			        				gogn : jq('#gogn').val(),
			        				gotrnr : jq('#gotrnr').val(),
			        				gopos :jq('#gopos').val()},
			        dataType    : 'json',
			        cache: false,
			        contentType : 'application/json',
			        success     : function(data){
			        	var len = data.length;
				  		if(len==1){
				  			jq('#gopos').addClass( "isa_error" );
				  			jq('#gopos').focus();
				  		}else{
				  			jq('#gopos').removeClass( "isa_error" );
				  		}
			        },
	                error: function() {
				  		  //alert('Error loading ...');
	              	  console.log('Error loading');
	              	  
					  }
			    })
		  	}
		  	*/
	  	});
	  
  	});
  
  //Special MERKNF section to trigger ENTER as submit-behavior. (Button has no std-dafault as Submit type)
  jq(function() {
	  jq('#gomkod').keypress(function(e){
		  saveItemLine(e);});
	  jq('#goantk').keypress(function(e){
		  saveItemLine(e);});
	  jq('#govsla').keypress(function(e){
		  saveItemLine(e);});
	  jq('#gomer1').keypress(function(e){
		  saveItemLine(e);});
	  jq('#gosted').keypress(function(e){
		  saveItemLine(e);});
	  jq('#gopos2').keypress(function(e){
		  saveItemLine(e)});
	  jq('#gomotm').keypress(function(e){
		  saveItemLine(e);});
	  jq('#gomerk').keypress(function(e){
		  saveItemLine(e);});
	  jq('#gomerb').keypress(function(e){
		  saveItemLine(e);});
	  jq('#gomerc').keypress(function(e){
		  saveItemLine(e)});
	  jq('#gomerd').keypress(function(e){
		  saveItemLine(e);});
  })
  function saveItemLine(e){
  		if(e.which == 13) {
			e.preventDefault();//this is necessary in order to avoid form.action in form submit button (Save)
			if(!jq("#buttonMerknadSubmit").is(":disabled")){
				jq( "#buttonMerknadSubmit" ).click();
			}
		}	
  }
  
  