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
  
  //update Merknad and refresh datatable
  jq(function() {
	  jq('#buttonMerknadSubmit').click(function() {
		
		  	var form = new FormData(document.getElementById('editMerknadForm'));
		  	
		    jq.ajax({
		        type        : 'POST',
		        url         : 'updateMerknad.do',
		        data        : form,
		        dataType    : 'text',
		        cache: false,
		  	  	processData: false,
		        contentType : false,
		        success     : function(data){
		        		
		        		jq.blockUI({ css: { fontSize: '22px' }, message: BLOCKUI_OVERLAY_MESSAGE_DEFAULT});
		        		console.log("B");
		        		window.location.href = 'godsno_edit.do?updateFlag=1&gogn=' + jq("#gogn").val() + '&gotrnr=' + jq("#gotrnr").val();
		        		/* not working
		        		var table = jq('#merknadList').dataTable();
		        		var rowNode = table.row.add( [ 'fuck', 'a duck','','','','X' ] ).draw().node();
		        		jq( rowNode )
		        		    .css( 'color', 'red' )
		        		    .animate( { color: 'black' } );
		        		*/
                 },
                 error: function() {
			  		  //alert('Error loading ...');
                	  console.log('Error loading');
                	  
				  }
		    });
		    jq.unblockUI();
		});
  	});
  