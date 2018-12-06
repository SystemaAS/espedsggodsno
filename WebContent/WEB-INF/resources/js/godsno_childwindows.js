  //this variable is a global jQuery var instead of using "$" all the time. Very handy
  var jq = jQuery.noConflict();
  var counterIndex = 0;
  var BLOCKUI_OVERLAY_MESSAGE_DEFAULT = "Please wait...";
  
  
  jq(function() {
      jq('#searchForm').submit(function() {
    	  setBlockUI();
  	  }); 
      
      
      //abort
      jq('#cancel').click(function(){
    	  window.close();
      });
		
      jq('#buttonPick').click(function(){
    	  //init place holder
    	  var requestParams = "";
    	  var RECORD_SEPARATOR = ';';
    	  //get all id:s
    	  jq( ".clazzSelectionAware" ).each(function( i ) {
			  var id = this.id;
			  var record = id.split('_');
			  var godsno = record[0].replace("godsno", "");
			  var pos1 = record[1].replace("pos1", "");
			  var id = record[2].replace("id", "");
			 
			  if(jq('#godsno' + godsno + '_' + 'pos1' + pos1 + '_' + 'id' + id ).is(':checked')){
				  var str = godsno +  "_" + pos1;
				  //start
				  requestParams += str + RECORD_SEPARATOR;
				  
		  	  }
    	  });
    	  //alert(requestParams);
    	  opener.jq('#pos1TargetString').val(requestParams);
    	 
    	  //
    	  window.close();
      });
  }); 
  
  

  
  //-------------------
  //Datatables jquery
  //-------------------
  //private function
  
  function filtersInit () {
    jq('#oppdragsList').DataTable().search(
    		jq('#oppdragsList_filter').val()
    ).draw();
  }

  jq(document).ready(function() {
    //init table (no ajax, no columns since the payload is already there by means of HTML produced on the back-end)
	var lang = jq('#language').val();
	
	jq('#oppdragsList').dataTable( {
	  "searchHighlight": true,
	  "dom": '<"oppdragsListFilter"if>t<"bottom"lp><"clear">', //look at mainListFilter on JSP SCRIPT-tag
	  "scrollCollapse":  true,
  	  "tabIndex": -1,
  	  "order": [[ 1, "asc" ]],
	  "lengthMenu": [ 25, 50, 100],
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
    jq('input.oppdragsList_filter').on( 'keyup click', function () {
    		filtersInit();
    } );
  } );
  
  