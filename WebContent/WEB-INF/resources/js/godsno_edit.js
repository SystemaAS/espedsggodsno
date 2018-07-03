  //this variable is a global jQuery var instead of using "$" all the time. Very handy
  var jq = jQuery.noConflict();
  var counterIndex = 0;
  
  
  jq(function() {
	  jq("#gogrdt").datepicker({ 
		  dateFormat: 'ddmmy'
	  });
	  jq("#golsdt").datepicker({ 
		  dateFormat: 'ddmmy'
	  });
  });
  
  /*
  jq(function() {
      jq('#submit').click(function() { 
    	  setBlockUI();
  	  }); 
  });    
  */
  
  
	 


  
  