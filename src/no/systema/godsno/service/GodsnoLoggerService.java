/**
 * 
 */
package no.systema.godsno.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.systema.godsno.controller.GodsnoMainListController;
import no.systema.godsno.mapper.JsonGodsnoMapper;
import no.systema.godsno.mapper.url.request.UrlRequestParameterMapper;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.manager.CodeDropDownMgr;
import no.systema.jservices.common.dao.GodshfDao;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.util.DateTimeManager;
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
	private static Logger logger = LoggerFactory.getLogger(GodsnoLoggerService.class.getName());
	private StringManager strMgr = new StringManager();
	DateTimeManager dateMgr = new DateTimeManager();
	private GodshfDao dao = null;
	
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
	public Collection<GodshfDao> getLogHfList(SystemaWebUser appUser, GodshfDao dao){
		Collection<GodshfDao> outputList = new ArrayList<GodshfDao>();
		String defaultDaysBack = "10";
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_LOG_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		
		if(strMgr.isNotNull(dao.getGogn()) ){
			urlRequestParams.append("&gogn=" + dao.getGogn());
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
    		outputList.forEach(record -> {
    			//logger.info(record.getGogn() + " " + record.getGohpgm() + " " + record.getGohdat());
    			
    		});
    	}		
	    
		return outputList;
	}
	/**
	 * 
	 * @param appUser
	 * @param dao
	 * @return
	 */
	private int add(String applicationUser, StringBuffer errMsg){
		UrlRequestParameterMapper urlRequestParameterMapper = new UrlRequestParameterMapper();
		
		int retval = 0;
		//---------------
    	//DML
		//---------------
		if(this.dao!=null){
			String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSHF_DML_UPDATE_URL;
			//add URL-parameters
			String urlRequestParamsKeys = "user=" + applicationUser + "&mode=A";
			String urlRequestParams = urlRequestParameterMapper.getUrlParameterValidString((this.dao));
			//add params
			urlRequestParams = urlRequestParamsKeys + urlRequestParams;
			
			logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
	    	logger.info("URL: " + BASE_URL);
	    	logger.info("URL PARAMS: " + urlRequestParams);
	    	
	    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
	    	//Debug --> 
	    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
	    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
	    	if(jsonPayload!=null){
	    		//TODO
	    		JsonContainerDaoGODSJF container = this.godsnoService.getContainerGodsjf(jsonPayload);
	    		if(container!=null){
	    			if(strMgr.isNotNull(container.getErrMsg())){
	    				errMsg.append(container.getErrMsg());
	    				//Update successfully done!
			    		logger.info("[ERROR] Record update - Error: " + errMsg.toString());
			    		retval = -1;
	    			}else{
	    				//Update successfully done!
			    		logger.info("[INFO] Record successfully updated, OK ");
			    		
	    			}
	    		}
	    	}		
		}else{
			errMsg.append("[ERROR] Dao is NULL. Missed setLogRecord on GodsnoLoggerService?");
		}
		//clean-up since it is an instance variable in an injected service
		this.dao = null; 
		
		return retval;
	}
	/**
	 * 
	 * @param dao
	 */
	private void setLogRecord(String applicationUser, String gogn, String gotrnr, String code){
		this.dao = new GodshfDao();
		this.dao.setGohkod(code);
		this.dao.setGogn(gogn);
		this.dao.setGotrnr(gotrnr);
		this.dao.setGohpgm("WEB");
		this.dao.setGohusr(applicationUser);
		
		Integer dateISO = Integer.valueOf(dateMgr.getCurrentDate_ISO());
		this.dao.setGohdat(dateISO);
		Integer timeISO = Integer.valueOf(dateMgr.getCurrentDate_ISO("HHmmss"));
		this.dao.setGohtim(timeISO);
		
	}
	/**
	 * 
	 * @param applicationUser
	 * @param gogn
	 * @param gotrnr
	 * @param code
	 * @param errMsg
	 */
	public void logIt(String applicationUser, String gogn, String gotrnr, String code, StringBuffer errMsg){
		this.setLogRecord(applicationUser, gogn, gotrnr, code);
		this.add(applicationUser, errMsg);
	}

}
