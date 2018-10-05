package no.systema.godsno.controller;

import java.lang.reflect.Field;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//application imports
import no.systema.main.context.TdsAppContext;
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.validator.LoginValidator;
import no.systema.main.util.AppConstants;
import no.systema.main.util.DateTimeManager;
import no.systema.main.util.JsonDebugger;
import no.systema.main.util.io.PayloadContentFlusher;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.util.StringManager;

import no.systema.jservices.common.dao.GodsjfDao;
import no.systema.jservices.common.dao.MerknfDao;
import no.systema.jservices.common.dao.GodshfDao;

//GODSNO
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.filter.SearchFilterGodsnoMainList;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.model.JsonContainerDaoMERKNF;
import no.systema.godsno.model.JsonContainerDaoGODSHF;
import no.systema.godsno.util.manager.CodeDropDownMgr;

/**
 * Godsregistrering-NO main list Controller 
 * 
 * @author oscardelatorre
 * @date 2018 June
 * 
 */

@Controller
public class GodsnoMainListController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = Logger.getLogger(GodsnoMainListController.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
	@Autowired
	private CodeDropDownMgr codeDropDownMgr;
	
	@Autowired
	private GodsnoService godsnoService;
	
		
	
	@PostConstruct
	public void initIt() throws Exception {
		if("DEBUG".equals(AppConstants.LOG4J_LOGGER_LEVEL)){
			logger.setLevel(Level.DEBUG);
		}
	}
		
	/**
	 * 
	 * @param recordToValidate
	 * @param bindingResult
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsno_mainlist.do", params="action=doFind",  method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doFind(@ModelAttribute ("record") SearchFilterGodsnoMainList recordToValidate, BindingResult bindingResult, HttpSession session, HttpServletRequest request){
		Collection mainList = new ArrayList();
		logger.info("Inside: doFind");
		Map model = new HashMap();
		
		ModelAndView successView = new ModelAndView("godsno_mainlist");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		String redirect = request.getParameter("rd");
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			
			appUser.setActiveMenu(SystemaWebUser.ACTIVE_MENU_GODSREGNO);
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			logger.info("####!!!" + recordToValidate.getSign());
			//-----------
			//Validation
			//-----------
			/* TODO
			SadImportListValidator validator = new SadImportListValidator();
			logger.info("Host via HttpServletRequest.getHeader('Host'): " + request.getHeader("Host"));
		    validator.validate(recordToValidate, bindingResult);
		    */
		    //check for ERRORS
			if(bindingResult.hasErrors()){
	    		logger.info("[ERROR Validation] search-filter does not validate)");
	    		//put domain objects and do go back to the successView from here
	    		//drop downs
			
	    		successView.addObject(GodsnoConstants.DOMAIN_MODEL, model);
	    		successView.addObject("searchFilter", recordToValidate);
				return successView;
	    		
		    }else{
				
	    		Map maxWarningMap = new HashMap<String,String>();
	    		SearchFilterGodsnoMainList searchFilter = (SearchFilterGodsnoMainList)session.getAttribute(GodsnoConstants.SESSION_SEARCH_FILTER);
    			if(redirect!=null && !"".equals(redirect)){
	    			recordToValidate = searchFilter;
	    		}
    			//get list
    			mainList = this.getList(appUser, recordToValidate, maxWarningMap, model);
    			
    			//--------------------------------------
	    		//Final successView with domain objects
	    		//--------------------------------------
	    		//drop downs
				model.put(GodsnoConstants.DOMAIN_LIST, mainList);
				
				successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
	    		
	    		//domain and search filter
	    		//Put list for upcomming view (PDF, Excel, etc)
	    		if(mainList!=null && (redirect==null || "".equals(redirect)) ){
	    			session.setAttribute(session.getId() + GodsnoConstants.SESSION_LIST, mainList);
	    			session.setAttribute(GodsnoConstants.SESSION_SEARCH_FILTER, recordToValidate);
	    		}
	    		if(redirect!=null && !"".equals(redirect)){
	    			//when this function is called with a redirect in another function
	    			successView.addObject("searchFilter", searchFilter);
	    		}else{
	    			//default
	    			successView.addObject("searchFilter", recordToValidate);
	    		}

	    		logger.info(Calendar.getInstance().getTime() + " CONTROLLER end - timestamp");
	    		return successView;
		    }
		}
	}
	
	
	
	
	/**
	 * 
	 * @param appUser
	 * @param recordToValidate
	 * @param maxWarningMap
	 * @return
	 */
	private Collection<GodsjfDao> getList(SystemaWebUser appUser, SearchFilterGodsnoMainList recordToValidate, Map<String,String> maxWarningMap, Map model){
		Collection<GodsjfDao> outputList = new ArrayList();
		String defaultDaysBack = "10";
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MAIN_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		
		if(strMgr.isNotNull(recordToValidate.getGogn()) ){
			urlRequestParams.append("&gogn=" + recordToValidate.getGogn());
		}else{
			if(strMgr.isNotNull(appUser.getDftdg()) ){
				urlRequestParams.append("&dftdg=" + appUser.getDftdg());
			}else{
				urlRequestParams.append("&dftdg=" + defaultDaysBack);
			}
			//
			if(strMgr.isNotNull(recordToValidate.getGomott()) ){
				urlRequestParams.append("&gomott=" + recordToValidate.getGomott());
			}
			if(strMgr.isNotNull(recordToValidate.getGotrnr()) ){
				urlRequestParams.append("&gotrnr=" + recordToValidate.getGotrnr());
			}
			if(strMgr.isNotNull(recordToValidate.getGoturn()) ){
				urlRequestParams.append("&goturn=" + recordToValidate.getGoturn());
			}
			if(strMgr.isNotNull(recordToValidate.getGobiln()) ){
				urlRequestParams.append("&gobiln=" + recordToValidate.getGobiln());
			}
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
    		JsonContainerDaoGODSJF listContainer = this.godsnoService.getContainerGodsjf(jsonPayload);
    		outputList = listContainer.getList();

    		for(GodsjfDao record : outputList){
    			this.adjustFieldsForFetch(record);
    			//limit ... just in case
    			if(outputList.size()<150){
    				this.getListOfExistingMerknf(appUser, record.getGogn(), record.getGotrnr(), model);
    			}
    		}
    	}		
	    
		return outputList;
	}
	
	private void adjustFieldsForFetch(GodsjfDao recordToValidate){
		recordToValidate.setGogrdt(this.convertToDate_NO(recordToValidate.getGogrdt()));
		recordToValidate.setGolsdt(this.convertToDate_NO(recordToValidate.getGolsdt()));
		recordToValidate.setGotrdt(this.convertToDate_NO(recordToValidate.getGotrdt()));
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String convertToDate_NO (String value){
		
		DateTimeManager dateMgr = new DateTimeManager();
		return dateMgr.getDateFormatted_NO(value, DateTimeManager.ISO_FORMAT);
	}
	
	/**
	 * 
	 * @param appUser
	 * @param gogn
	 * @param gotrnr
	 * @return
	 */
	private Collection<MerknfDao> getListOfExistingMerknf(SystemaWebUser appUser, String gogn, String gotrnr, Map model){
		Collection<MerknfDao> outputList = new ArrayList<MerknfDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MERKNF_COUNT_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		urlRequestParams.append("&gogn=" + gogn);
		urlRequestParams.append("&gotrnr=" + gotrnr);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoMERKNF listContainer = this.godsnoService.getContainerMerknf(jsonPayload);
    		outputList = listContainer.getList();
    		if(outputList!=null && !outputList.isEmpty()){
    			
    			String key = gogn + gotrnr;
    			model.put(key, gogn);//will be used in the JSP
    		}
    	}		
	    
		return outputList;
	}
	
}

