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
	  	jq('#godsnoPosButton').click(function() {
  			jq('#godsnoPosButton').attr('target','_blank');
  			window.open('godsno_childwindow_uppdragslist.do?action=doFind&hegn=' + jq('#gogn').val(), "oppWin", "top=100px,left=400px,height=500px,width=600px,scrollbars=no,status=no,location=no");
	    });
	    jq('#godsnoPosButton').keypress(function(e){ //extra feature for the end user
			if(e.which == 13) {
				jq('#godsnoPosButton').click();
			}
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
  
//Some auto-fill values from TURER 
  jq(function() {
	  jq('#goturn').blur(function(){
		  if(jq('#goturn').val() != ''){
			  //Only if the value is empty
			  if(jq('#gobiln').val() == ''){
				  getRecordTurer();
			  }
		  }
	  });
  });
	  
  //Get record - Turer (to auto-fill some values in the form) 
  function getRecordTurer(){
	  jq.ajax({
	        type        : 'GET',
	        url         : 'getSpecificRecord_turer.do',
	        data		: { applicationUser : jq('#applicationUser').val(), 
	        				id : jq('#goturn').val() },
	        dataType    : 'json',
	        cache: false,
	        contentType : 'application/json',
	        success     : function(data){
	        	var len = data.length;
	        	for ( var i = 0; i < len; i++) {
	        		jq('#gobiln').val(data[i].tubiln);
		  			jq('#gobiln').focus();
		  		}
	        },
          error: function() {
		  	  //alert('Error loading ...');
        	  console.log('Error loading');
			  }
	    })
  }
  
  
  

    
  