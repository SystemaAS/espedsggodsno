  //this variable is a global jQuery var instead of using "$" all the time. Very handy
  var jq = jQuery.noConflict();
  var counterIndex = 0;
  
  
  jq(function() {
	  /*jq("#fromDate").datepicker({ 
		  dateFormat: 'ddmmy'
	  });*/
  });
  
  jq(function() {
      jq('#submit').click(function() { 
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
	  "dom": '<"top"f>rt<"bottom"lip><"clear">',
	  "order": [[ 0, "desc" ]],
	  "lengthMenu": [ 50, 75, 100]
	} );
    //event on input field for search
    jq('input.mainList_filter').on( 'keyup click', function () {
    		filtersInit();
    } );
  } );
  
  
	 


  
  