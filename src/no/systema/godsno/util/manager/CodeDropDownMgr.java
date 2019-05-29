package no.systema.godsno.util.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.util.JsonDebugger;
import no.systema.z.main.maintenance.model.jsonjackson.dbtable.JsonMaintMainKodtaContainer;
import no.systema.z.main.maintenance.model.jsonjackson.dbtable.JsonMaintMainKodtaRecord;
import no.systema.z.main.maintenance.model.jsonjackson.dbtable.JsonMaintMainKodtsfSyparfContainer;
import no.systema.z.main.maintenance.model.jsonjackson.dbtable.JsonMaintMainKodtsfSyparfRecord;
import no.systema.z.main.maintenance.service.MaintMainKodtaService;
import no.systema.z.main.maintenance.service.MaintMainKodtsfSyparfService;
import no.systema.z.main.maintenance.url.store.MaintenanceMainUrlDataStore;


@Service
public class CodeDropDownMgr {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = Logger.getLogger(CodeDropDownMgr.class.getName());
	
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
		
	@Autowired
	private MaintMainKodtsfSyparfService maintMainKodtsfSyparfService;
	
	@Autowired
	private MaintMainKodtaService maintMainKodtaService;
	
	/**
	 * 
	 * @param applicationUser
	 * @param kosfsi
	 * @return
	 */
	public  List <JsonMaintMainKodtsfSyparfRecord>getSignatures (String applicationUser ) {
		final String METHOD = "[DEBUG] getSignatures ";
		logger.info(" Inside..." + METHOD );
		List<JsonMaintMainKodtsfSyparfRecord> result = new ArrayList();
	 	//get table
		String BASE_URL = MaintenanceMainUrlDataStore.MAINTENANCE_MAIN_BASE_SYFA60R_GET_LIST_URL;
		String urlRequestParams = "user=" + applicationUser; // + "&kosfsi=" + kosfsi;
		logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + jsonDebugger.getBASE_URL_NoHostName(BASE_URL));
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams);
    	
    	//extract
    	List<JsonMaintMainKodtsfSyparfRecord> list = new ArrayList<JsonMaintMainKodtsfSyparfRecord>();
    	if(jsonPayload!=null){
			//lists
    		JsonMaintMainKodtsfSyparfContainer container = this.maintMainKodtsfSyparfService.getList(jsonPayload);
    		if(container!=null){
	        	list = (List<JsonMaintMainKodtsfSyparfRecord>)container.getList();
	        	for(JsonMaintMainKodtsfSyparfRecord record: list){
	        		//logger.info(record.getKosfsi());
	        	}
	        }
    	}
    	return list;
	
	}
	/**
	 * 
	 * @param applicationUser
	 * @return
	 */
	public  List <JsonMaintMainKodtaRecord>getAvdList (String applicationUser ) {
		final String METHOD = "[DEBUG] getAvdList ";
		logger.info(" Inside..." + METHOD );
		//get table
		String BASE_URL = MaintenanceMainUrlDataStore.MAINTENANCE_MAIN_BASE_SYFA14R_GET_LIST_URL;
		String urlRequestParams = "user=" + applicationUser;
		logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + jsonDebugger.getBASE_URL_NoHostName(BASE_URL));
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams);
    	//DEBUG
    	//this.jsonDebugger.debugJsonPayload(jsonPayload, 1000);
    	//extract
    	List<JsonMaintMainKodtaRecord> list = new ArrayList<JsonMaintMainKodtaRecord>();
    	if(jsonPayload!=null){
			//lists
    		JsonMaintMainKodtaContainer container = this.maintMainKodtaService.getList(jsonPayload);
	        if(container!=null){
	        	list = (List<JsonMaintMainKodtaRecord>)container.getList();
	        	for(JsonMaintMainKodtaRecord rec: list){
	        		//logger.info(rec.getKoaavd());
	        	}
	        }
    	}
    	return list;

	}

	
	
}
