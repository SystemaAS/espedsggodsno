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
	  "dom": '<"top"f>t<"bottom"ip><"clear">', //look at mainListFilter on JSP SCRIPT-tag
	  "scrollY":     "500px",
  	  "scrollCollapse":  true,
  	  "tabIndex": -1,
  	  "order": [[ 0, "asc" ]],
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
  