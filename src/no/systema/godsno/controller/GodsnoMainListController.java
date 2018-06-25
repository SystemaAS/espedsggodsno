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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.ServletRequestDataBinder;


//application imports
import no.systema.main.context.TdsAppContext;
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.validator.LoginValidator;
import no.systema.main.util.AppConstants;
import no.systema.main.util.JsonDebugger;
import no.systema.main.util.io.PayloadContentFlusher;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.util.StringManager;

import no.systema.jservices.common.dao.GodsjfDao;

//GODSNO
import no.systema.godsno.service.GodsnoMainListService;
import no.systema.godsno.filter.SearchFilterGodsnoMainList;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.model.JsonGenericContainerDao;


/**
 * eFaktura main list Controller 
 * 
 * @author oscardelatorre
 * @date 2018 June
 * 
 */

@Controller
@SessionAttributes(AppConstants.SYSTEMA_WEB_USER_KEY)
@Scope("session")
public class GodsnoMainListController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = Logger.getLogger(GodsnoMainListController.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private ApplicationContext context;
	private LoginValidator loginValidator = new LoginValidator();
	//private RpgReturnResponseHandler rpgReturnResponseHandler = new RpgReturnResponseHandler();
	private PayloadContentFlusher payloadContentFlusher = new PayloadContentFlusher();
	private StringManager strMgr = new StringManager();
	@Autowired
	private GodsnoMainListService godsnoMainListService;
	
	@PostConstruct
	public void initIt() throws Exception {
		if("DEBUG".equals(AppConstants.LOG4J_LOGGER_LEVEL)){
			logger.setLevel(Level.DEBUG);
		}
	}
		
	/**
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsno_mainlist.do", params="action=doFind",  method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doFind(@ModelAttribute ("record") SearchFilterGodsnoMainList recordToValidate, BindingResult bindingResult, HttpSession session, HttpServletRequest request){
		this.context = TdsAppContext.getApplicationContext();
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
			logger.info("####!!!" + recordToValidate.getAvd());
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
    			mainList = this.getList(appUser, recordToValidate, maxWarningMap);
	    		//--------------------------------------
	    		//Final successView with domain objects
	    		//--------------------------------------
	    		//drop downs
	    		//this.setCodeDropDownMgr(appUser, model);
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
	 * @param wssavd
	 * @return
	 */
	private Collection<GodsjfDao> getList(SystemaWebUser appUser, SearchFilterGodsnoMainList recordToValidate, Map<String,String> maxWarningMap){
		Collection<GodsjfDao> outputList = new ArrayList();
		String defaultDaysBack = "7";
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
		}
		
		
		/*
		if(!"".equals(recordToValidate.getAvd())&& recordToValidate.getAvd()!=null ){ urlRequestParams.append("&avd=" + recordToValidate.getAvd()); }
		if(!"".equals(recordToValidate.getFaktnr())&& recordToValidate.getFaktnr()!=null ){ urlRequestParams.append("&fn=" + recordToValidate.getFaktnr()); }
		if(!"".equals(recordToValidate.getKundenr())&& recordToValidate.getKundenr()!=null ){ urlRequestParams.append("&fkn=" + recordToValidate.getKundenr()); }
		if(!"".equals(recordToValidate.getRfa())&& recordToValidate.getRfa()!=null ){ urlRequestParams.append("&rfa=" + recordToValidate.getRfa()); }
		if(!"".equals(recordToValidate.getFrom())&& recordToValidate.getFrom()!=null ){ urlRequestParams.append("&dtf=" + recordToValidate.getFrom()); }
		if(!"".equals(recordToValidate.getTo())&& recordToValidate.getTo()!=null ){ urlRequestParams.append("&dtt=" + recordToValidate.getTo()); }
		if(!"".equals(recordToValidate.getStatus())&& recordToValidate.getStatus()!=null ){ 
			urlRequestParams.append("&st=" + recordToValidate.getStatus());
		}else{
			urlRequestParams.append("&st=*");
		}
		*/
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonGenericContainerDao listContainer = this.godsnoMainListService.getMainListContainer(jsonPayload);
    		outputList = listContainer.getList();	
    		//maxWarningMap.put(EfakturaConstants.DOMAIN_MAX_WARNING_OPEN_ORDERS, jsonOpenOrdersListContainer.getMaxWarning());
    	}		
	    
		return outputList;
	}
	
	//SERVICES
	@Qualifier ("urlCgiProxyService")
	private UrlCgiProxyService urlCgiProxyService;
	@Autowired
	@Required
	public void setUrlCgiProxyService (UrlCgiProxyService value){ this.urlCgiProxyService = value; }
	public UrlCgiProxyService getUrlCgiProxyService(){ return this.urlCgiProxyService; }
	
	
}

