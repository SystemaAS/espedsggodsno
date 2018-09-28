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
	  jq("#gogrdt").datepicker({ 
		  dateFormat: 'ddmmy'
	  });
	  jq("#golsdt").datepicker({ 
		  dateFormat: 'ddmmy'
	  });
	  
	  //Custom Validity
	  jq('#gogren').focus(function() {
	    	if(jq('#gogren').val()!=''){
	    		refreshCustomValidity(jq('#gogren')[0]);
	  		}
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
	  jq('#gotrnr').focus(function() {
	    	if(jq('#gotrnr').val()!=''){
	    		refreshCustomValidity(jq('#gotrnr')[0]);
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
	  jq('#owngogn_2').val(id + "_" + id2);
	  jq('#owngogn_2').focus();
  }
  
	 


  
  