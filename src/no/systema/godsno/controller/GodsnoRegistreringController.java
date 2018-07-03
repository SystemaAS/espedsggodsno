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
import org.springframework.ui.ModelMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//application imports
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.validator.LoginValidator;
import no.systema.main.util.AppConstants;
import no.systema.main.util.DateTimeManager;
import no.systema.main.util.JsonDebugger;
import no.systema.main.util.io.PayloadContentFlusher;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.util.StringManager;
import no.systema.jservices.common.dao.GodsjfDao;

//GODSNO
import no.systema.godsno.service.GodsnoMainListService;
import no.systema.godsno.validator.GodsnoRegistreringValidator;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.model.JsonGenericContainerDao;


/**
 * Godsregistrering-NO Controller
 * 
 * @author oscardelatorre
 * @date 2018 July
 * 
 */

@Controller
@SessionAttributes(AppConstants.SYSTEMA_WEB_USER_KEY)
@Scope("session")
public class GodsnoRegistreringController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = Logger.getLogger(GodsnoRegistreringController.class.getName());
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
	 * @param recordToValidate
	 * @param bindingResult
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsno_edit.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doGodsnoEdit(ModelMap model, @ModelAttribute ("record") GodsjfDao recordToValidate, BindingResult bindingResult, HttpSession session, HttpServletRequest request){
		ModelAndView successView = new ModelAndView("godsno_edit");
		logger.info("Inside: doGodsnoEdit");
		
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		
		String action = request.getParameter("action");
		boolean isValidRecord = true;
		
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			//appUser.setActiveMenu(SystemaWebUser.ACTIVE_MENU_GODSREGNO);
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			//logger.info("DATE:" + recordToValidate.getGogrdt());
			if(GodsnoConstants.ACTION_UPDATE.equals(action)){
				GodsnoRegistreringValidator validator = new GodsnoRegistreringValidator();
				validator.validate(recordToValidate, bindingResult);
			    //check for ERRORS
				if(bindingResult.hasErrors()){
		    		logger.info("[ERROR Validation] record does not validate)");
		    		//put domain objects and do go back to the successView from here
		    		//drop downs
		    		isValidRecord = false;
		    		
			    }else{
			    	//adjust some db-fields
			    	this.adjustFieldsForUpdate(recordToValidate);
		    		logger.info(Calendar.getInstance().getTime() + " CONTROLLER end - timestamp");
		    		
			    }
			}
			//--------------
			//Fetch record
			//--------------
			if(strMgr.isNotNull(recordToValidate.getGogn()) ){
				if(isValidRecord){
					GodsjfDao updatedDao = this.getRecord(appUser, recordToValidate);
					this.adjustFieldsForFetch(updatedDao);
					model.addAttribute(GodsnoConstants.DOMAIN_RECORD, updatedDao);
				}else{
					//in case of validation errors
					//model.addAttribute(GodsnoConstants.DOMAIN_RECORD, recordToValidate);
				}
			}
			if(action==null || "".equals(action)){ action = "doUpdate"; }
			model.addAttribute("action", action);
			
			//set some other model values
			this.populateUI_ModelMap(model);
			
			//successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
			return successView;
		}
	}
	/**
	 * 
	 * @param recordToValidate
	 */
	private void adjustFieldsForUpdate(GodsjfDao recordToValidate){
		recordToValidate.setGogrdt(this.convertToDate_ISO(recordToValidate.getGogrdt()));
		recordToValidate.setGolsdt(this.convertToDate_ISO(recordToValidate.getGolsdt()));
	}
	private void adjustFieldsForFetch(GodsjfDao recordToValidate){
		recordToValidate.setGogrdt(this.convertToDate_NO(recordToValidate.getGogrdt()));
		recordToValidate.setGolsdt(this.convertToDate_NO(recordToValidate.getGolsdt()));
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String convertToDate_ISO (String value){
		DateTimeManager dateMgr = new DateTimeManager();
		return dateMgr.getDateFormatted_ISO(value, DateTimeManager.NO_FORMAT);
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
	 * @param model
	 */
	private void populateUI_ModelMap(ModelMap model){
		LocalDateTime now = LocalDateTime.now();
		String day = now.format(DateTimeFormatter.ofPattern("dd.MM.yy"));
		String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		model.addAttribute("today", day);
		model.addAttribute("time", time);
	}
	
	/**
	 * 
	 * @param appUser
	 * @param wssavd
	 * @return
	 */
	private GodsjfDao getRecord(SystemaWebUser appUser, GodsjfDao recordToValidate){
		GodsjfDao record = new GodsjfDao();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MAIN_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		
		if(strMgr.isNotNull(recordToValidate.getGogn()) ){
			urlRequestParams.append("&gogn=" + recordToValidate.getGogn());
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
    		JsonGenericContainerDao listContainer = this.godsnoMainListService.getMainListContainer(jsonPayload);
    		for(GodsjfDao tmpRecord: listContainer.getList()){
    			record = tmpRecord;
    		}
    	}		
		return record;
	}
	
	//SERVICES
	@Qualifier ("urlCgiProxyService")
	private UrlCgiProxyService urlCgiProxyService;
	@Autowired
	@Required
	public void setUrlCgiProxyService (UrlCgiProxyService value){ this.urlCgiProxyService = value; }
	public UrlCgiProxyService getUrlCgiProxyService(){ return this.urlCgiProxyService; }
	
	
}

