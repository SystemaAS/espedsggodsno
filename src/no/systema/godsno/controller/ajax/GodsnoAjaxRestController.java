package no.systema.godsno.controller.ajax;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import no.systema.godsno.mapper.url.request.UrlRequestParameterMapper;
import no.systema.godsno.model.JsonContainerDaoTURER;
import no.systema.godsno.model.JsonContainerDaoPrintGogn;
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.jservices.common.dao.TurerDao;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.util.StringManager;


/**
 * 
 * @author oscardelatorre
 * @date Oct 2018
 */
@RestController
public class GodsnoAjaxRestController {
	private static final Logger logger = LoggerFactory.getLogger(GodsnoAjaxRestController.class.getName());
	private StringManager strMgr = new StringManager();
	private UrlRequestParameterMapper urlRequestParameterMapper = new UrlRequestParameterMapper();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
	@Autowired
	private GodsnoService godsnoService;
	
	
	/**
	 * 
	 * @param applicationUser
	 * @param gogn
	 * @param type
	 * @return
	 */
	@RequestMapping(path="/printMerknaderSpecificGogn.do", method = RequestMethod.GET)
	public Collection printMerknaderSpecificGogn(@RequestParam String applicationUser, @RequestParam String gogn, @RequestParam String type){
		logger.info("Inside: printMerknaderSpecificGogn");
		logger.info(gogn + " " + type);
		Collection<JsonContainerDaoPrintGogn> outputList = new ArrayList<JsonContainerDaoPrintGogn>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_PRINT_MERKNADER_SPECIFIC_GOGN_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + applicationUser);
		urlRequestParams.append("&gogn=" + gogn);
		urlRequestParams.append("&type=" + type);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoPrintGogn container = this.godsnoService.getContainerPrintGogn(jsonPayload);
    		outputList.add(container);	
    	}		
	    
		return outputList;
		
		  
	}
	
	/**
	 * 
	 * @param applicationUser
	 * @param mrn
	 * @param type
	 * @return
	 */
	@RequestMapping(path="/releaseSpecificTrnr.do", method = RequestMethod.GET)
	public Collection releaseSpecificTrnr(@RequestParam String applicationUser, @RequestParam String mrn, @RequestParam String type){
		logger.info("Inside: releaseSpecificTrnr");
		logger.info(mrn + " " + type);
		Collection<JsonContainerDaoPrintGogn> outputList = new ArrayList<JsonContainerDaoPrintGogn>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_RELEASE_GOGN_TRNR_TOLLAGER_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + applicationUser);
		urlRequestParams.append("&orimrn=" + mrn);
		urlRequestParams.append("&oritty=" + type);
		
		logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		this.godsnoService.getContainerPhantom(jsonPayload);
    	}		
	    
		return outputList;
		
		  
	}
	/**
	 * Get a record match from TURER
	 * @param applicationUser
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/getSpecificRecord_turer.do",method = RequestMethod.GET)
	public Collection getRecord(@RequestParam String applicationUser, @RequestParam String id){
		logger.info("Inside: getRecord");
		return this.getRecordTurer(applicationUser, id);
		  
	}
	
	/**
	 * Gets a specific record from TURER (db table)
	 * 
	 * @param applicationUser
	 * @param id
	 * @return
	 */
	private Collection<TurerDao> getRecordTurer(String applicationUser, String id){
		Collection<TurerDao> outputList = new ArrayList<TurerDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_TURER_LIST_EXACT_MATCH_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + applicationUser);
		urlRequestParams.append("&tupro=" + id);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoTURER listContainer = this.godsnoService.getContainerTurer(jsonPayload);
    		outputList = listContainer.getList();
    			
    	}		
	    
		return outputList;
	}
	
	
	
}
