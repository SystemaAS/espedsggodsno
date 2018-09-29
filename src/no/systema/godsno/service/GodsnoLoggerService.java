/**
 * 
 */
package no.systema.godsno.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.systema.godsno.controller.GodsnoMainListController;
import no.systema.godsno.mapper.JsonGodsnoMapper;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.manager.CodeDropDownMgr;
import no.systema.jservices.common.dao.GodshfDao;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.util.JsonDebugger;
import no.systema.main.util.StringManager;
import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.model.JsonContainerDaoGODSGF;
import no.systema.godsno.model.JsonContainerDaoGODSFI;
import no.systema.godsno.model.JsonContainerDaoGODSHF;

/**
 * 
 * @author oscardelatorre
 * @date Jun 2018
 * 
 * 
 */
@Service
public class GodsnoLoggerService {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = Logger.getLogger(GodsnoLoggerService.class.getName());
	private StringManager strMgr = new StringManager();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
	@Autowired
	private GodsnoService godsnoService;
	
	/**
	 * 
	 * @param appUser
	 * @param gogn
	 * @return
	 */
	private Collection<GodshfDao> getLogHfList(SystemaWebUser appUser, String gogn){
		Collection<GodshfDao> outputList = new ArrayList<GodshfDao>();
		String defaultDaysBack = "10";
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_LOG_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		
		if(strMgr.isNotNull(gogn) ){
			urlRequestParams.append("&gogn=" + gogn);
		}else{
			if(strMgr.isNotNull(appUser.getDftdg()) ){
				urlRequestParams.append("&dftdg=" + appUser.getDftdg());
			}else{
				urlRequestParams.append("&dftdg=" + defaultDaysBack);
			}
			/* MORE ...
			if(strMgr.isNotNull(recordToValidate.getGomott()) ){
				urlRequestParams.append("&gomott=" + recordToValidate.getGomott());
			}
			*/
		}
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSHF listContainer = this.godsnoService.getContainerGodshf(jsonPayload);
    		outputList = (List)listContainer.getList();
    		//logger.info(outputList.size());
    		//outputList.forEach(record -> logger.info(record.getGogn()));
    	}		
	    
		return outputList;
	}
	

}
