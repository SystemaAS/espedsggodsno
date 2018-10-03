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
	  jq("#gotrdt").datepicker({ 
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
  });
  
  
  
  jq(function() {
      jq('#editForm').submit(function() { 
    	  setBlockUI();
  	  });
      
  });    
  
  

    
  