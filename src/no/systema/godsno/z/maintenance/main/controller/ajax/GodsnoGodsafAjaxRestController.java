package no.systema.godsno.z.maintenance.main.controller.ajax;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.jservices.common.dao.GodsafDao;
import no.systema.jservices.common.dao.services.BridfDaoService;
import no.systema.jservices.common.dao.services.GodsafDaoService;
import no.systema.jservices.common.util.StringUtils;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.util.StringManager;


/**
 * 
 * @author oscardelatorre
 * @date Sep 2018
 */
@RestController
public class GodsnoGodsafAjaxRestController {
	private static final Logger logger = Logger.getLogger(GodsnoGodsafAjaxRestController.class.getName());
	private StringManager strMgr = new StringManager();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
	@Autowired
	private GodsnoService godsnoService;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(path="/getSpecificRecord_godsaf.do",method = RequestMethod.GET)
	public Collection getRecord(@RequestParam String applicationUser, @RequestParam String id){
		logger.info("Inside: getRecord");
		return this.getRecordGodsaf(applicationUser, id);
		  
	}
	/**
	 * 
	 * @param applicationUser
	 * @param id
	 * @param id2
	 * @return
	 */
	@RequestMapping(path="/deleteSpecificRecord_godsaf.do",method = RequestMethod.GET)
	public Collection deleteRecord(@RequestParam String applicationUser, @RequestParam String id ){
		logger.info("Inside: deleteRecord");
		String mode = "D";
		return this.updateRecordGodsaf(applicationUser, id, mode);
		  
	}
	
	/**
	 * 
	 * @param appUser
	 * @param id
	 * @return
	 */
	private Collection<GodsafDao> getRecordGodsaf(String applicationUser, String id){
		Collection<GodsafDao> outputList = new ArrayList<GodsafDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSAF_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + applicationUser);
		urlRequestParams.append("&gflavd=" + id);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSAF listContainer = this.godsnoService.getContainerGodsaf(jsonPayload);
    		outputList = listContainer.getList();
    			
    	}		
	    
		return outputList;
	}
	/**
	 * 
	 * @param applicationUser
	 * @param id
	 * @param mode
	 * @return
	 */
	private Collection<GodsafDao> updateRecordGodsaf(String applicationUser, String id, String mode){
		Collection<GodsafDao> outputList = new ArrayList<GodsafDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSAF_DML_UPDATE_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + applicationUser);
		urlRequestParams.append("&mode=" + mode);
		urlRequestParams.append("&gflavd=" + id);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSAF listContainer = this.godsnoService.getContainerGodsaf(jsonPayload);
    		if(strMgr.isNull(listContainer.getErrMsg())){
    			GodsafDao dao = new GodsafDao();
    			dao.setGflavd("OK");
    			outputList.add(dao);
    		}
    	}		
	    
		return outputList;
	}
	
	
	
}
