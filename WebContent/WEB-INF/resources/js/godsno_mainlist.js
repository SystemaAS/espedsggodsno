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
  
  
	 


  
  