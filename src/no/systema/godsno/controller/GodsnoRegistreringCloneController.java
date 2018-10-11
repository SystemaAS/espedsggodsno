package no.systema.godsno.controller;

import java.util.*;
import java.util.function.ToDoubleBiFunction;
import java.util.regex.*;

import javax.annotation.PostConstruct;


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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Scope;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.ui.ModelMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//application imports
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.validator.LoginValidator;
import no.systema.main.util.AppConstants;
import no.systema.main.util.DateTimeManager;
import no.systema.main.util.JsonDebugger;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.util.StringManager;
import no.systema.main.util.DateTimeManager;
import no.systema.jservices.common.dao.GodsafDao;
import no.systema.jservices.common.dao.GodsfiDao;
import no.systema.jservices.common.dao.GodsjfDao;
import no.systema.jservices.common.dao.GodsgfDao;
import no.systema.jservices.common.dao.GodshfDao;
import no.systema.jservices.common.dao.MerknfDao;


//GODSNO
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.service.GodsnoLoggerService;
import no.systema.godsno.validator.GodsnoRegistreringValidator;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.util.manager.GodsnrManager;
import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.model.JsonContainerDaoGODSFI;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.model.JsonContainerDaoGODSGF;
import no.systema.godsno.model.JsonContainerDaoMERKNF;
import no.systema.godsno.filter.SearchFilterGodsnoMainList;
import no.systema.godsno.mapper.url.request.UrlRequestParameterMapper;

/**
 * Godsregistrering-NO Clone Controller
 * 
 * @author oscardelatorre
 * @date 2018 Oct
 * 
 */

@Controller
public class GodsnoRegistreringCloneController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = Logger.getLogger(GodsnoRegistreringCloneController.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	DateTimeManager dateMgr = new DateTimeManager();
	private UrlRequestParameterMapper urlRequestParameterMapper = new UrlRequestParameterMapper();
	private final String LOGGER_CODE_EDIT = "E";
	private final String LOGGER_CODE_NEW = "N";
	private final String DEFAULT_TPAPIR_TYPE = "T1";
	
	@Autowired
	private GodsnoService godsnoService;

	@Autowired
	private GodsnoLoggerService godsnoLoggerService;
	
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
	@RequestMapping(value="godsno_clone.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doGodsnoClone(ModelMap model, @ModelAttribute ("record") GodsjfDao recordToValidate, BindingResult bindingResult, HttpSession session, HttpServletRequest request){
		ModelAndView successView = new ModelAndView("godsno_clone");
		logger.info("Inside: doGodsnoClone");
		
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		
		String action = request.getParameter("action");
		logger.info("action:" + action);
		//Special case for gotrty
		if(strMgr.isNull(recordToValidate.getGotrty())){
			recordToValidate.setGotrty(this.DEFAULT_TPAPIR_TYPE);
		}		
		boolean isValidRecord = true;
		
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			
			//There will always be a doCreate. All updates are outside this controller
			if(GodsnoConstants.ACTION_CREATE.equals(action)){
				//validator
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
			    	
			    	//Start DML operations if applicable
					StringBuffer errMsg = new StringBuffer();
					int dmlRetval = 0;
					//--------------------------------------------------------------------------------
					//at this point we do have a godsnr well-formed (either manually or automatically)
					//--------------------------------------------------------------------------------
					if(strMgr.isNotNull( recordToValidate.getGogn()) ){
						logger.info("doCreate branch starting...");
						//duplicate check
						if(this.recordExistsGodsjf(appUser, recordToValidate, errMsg)){
							logger.info("duplicate exists ... ??????? :-(" );
							//duplicate found
							dmlRetval = -1;
						}else{
							logger.info("doIt(save)");
							dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
							if(dmlRetval>=0){
								//Gods-logger (footprint after each DML-success operation)
								this.godsnoLoggerService.logIt(appUser.getUser(), recordToValidate.getGogn(), recordToValidate.getGotrnr(), this.LOGGER_CODE_NEW, errMsg);
							}
						}
						logger.info(Calendar.getInstance().getTime() + " CONTROLLER end - timestamp");
					}
					if(dmlRetval<0){
						isValidRecord = false;
						model.addAttribute(GodsnoConstants.ASPECT_ERROR_MESSAGE, errMsg.toString());
						
					}else{
						//bye bye ... 
						successView = new ModelAndView("redirect:godsno_mainlist.do?action=doFind&rd=1");
					}
			    }
			}else if (GodsnoConstants.ACTION_FETCH.equals(action)){
				//This will only be executed in the first call
				if(strMgr.isNotNull(recordToValidate.getGogn()) ){
					if(isValidRecord){
						GodsjfDao updatedDao = this.getRecordGodsjf(appUser, recordToValidate);
						this.adjustFieldsForFetch(updatedDao);
						model.addAttribute(GodsnoConstants.DOMAIN_RECORD, updatedDao);
						
					}else{
						//in case of validation errors
						this.adjustFieldsForFetch(recordToValidate);
						model.addAttribute(GodsnoConstants.DOMAIN_RECORD, recordToValidate);
					}
					//prepare for the update for the form submit
					action = "doCreate";	
				}
			}
			
			
			model.addAttribute("action", action);
			logger.info("action:" + action);
		    
			//set some other model values
			this.populateUI_ModelMap(model);
			
			
			return successView;
		}
	}
	
	/**
	 * 
	 * @param applicationUser
	 * @param recordToValidate
	 * @param mode
	 * @param errMsg
	 * @return
	 */
	private int updateRecord(String applicationUser, GodsjfDao recordToValidate, String mode, StringBuffer errMsg ){
		int retval = 0;
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSJF_DML_UPDATE_URL;
		//add URL-parameters
		String urlRequestParamsKeys = "user=" + applicationUser + "&mode=" + mode;
		String urlRequestParams = this.urlRequestParameterMapper.getUrlParameterValidString((recordToValidate));
		//add params
		urlRequestParams = urlRequestParamsKeys + urlRequestParams;
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
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

		return retval;
	}
	
	
	
	/**
	 * Check for duplicate 
	 * 
	 * @param appUser
	 * @param recordToValidate
	 * @param errMsg
	 * @return
	 */
	private boolean recordExistsGodsjf(SystemaWebUser appUser, GodsjfDao recordToValidate, StringBuffer errMsg){
		boolean retval = false;
		GodsjfDao newRecord = this.getRecordGodsjf(appUser, recordToValidate);
		
		if(newRecord!=null && (newRecord.getGogn()!=null && newRecord.getGogn().equals(recordToValidate.getGogn())) ){
			errMsg.append("Godsnr:" + newRecord.getGogn() + " Transittnr:" + newRecord.getGotrnr());
			errMsg.append(" finnes allerede... ? ");
			logger.info(errMsg.toString());
			retval = true;
		}
		return retval;
	}
	
	/**
	 * 
	 * @param recordToValidate
	 */
	private void adjustFieldsForUpdate(GodsjfDao recordToValidate){
		recordToValidate.setGogrdt(this.convertToDate_ISO(recordToValidate.getGogrdt()));
		recordToValidate.setGolsdt(this.convertToDate_ISO(recordToValidate.getGolsdt()));
		recordToValidate.setGotrdt(this.convertToDate_ISO(recordToValidate.getGotrdt()));
		
		//date
		if(strMgr.isNull(recordToValidate.getGotrdt())){ recordToValidate.setGotrdt("0"); }
		//date and time
		if(strMgr.isNull(recordToValidate.getGogrdt())){ recordToValidate.setGogrdt("0"); }
		if(recordToValidate.getGogrkl()==null){ recordToValidate.setGogrkl(0); }
		//date and time
		if(strMgr.isNull(recordToValidate.getGolsdt())){ recordToValidate.setGolsdt("0"); }
		if(recordToValidate.getGolskl()==null){ recordToValidate.setGolskl(0); }
		
		//transform to UPPERCASE values some given fields
		if(strMgr.isNotNull(recordToValidate.getGogren())){
			recordToValidate.setGogren(recordToValidate.getGogren().toUpperCase());
		}
		if(strMgr.isNotNull(recordToValidate.getGobiln())){
			recordToValidate.setGobiln(recordToValidate.getGobiln().toUpperCase());
		}
	}
	/**
	 * 
	 * @param recordToValidate
	 */
	private void adjustFieldsForFetch(GodsjfDao recordToValidate){
		//with clone we must blank the transittnr
		recordToValidate.setGotrnr(null);
		recordToValidate.setGogrdt(this.convertToDate_NO(recordToValidate.getGogrdt()));
		recordToValidate.setGolsdt(this.convertToDate_NO(recordToValidate.getGolsdt()));
		recordToValidate.setGotrdt(this.convertToDate_NO(recordToValidate.getGotrdt()));
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String convertToDate_ISO (String value){
		String retval = null;
		
		if(strMgr.isNotNull(value)){
			DateTimeManager dateMgr = new DateTimeManager();
			retval = dateMgr.getDateFormatted_ISO(value, DateTimeManager.NO_FORMAT);
		}
		return retval;
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
	private GodsjfDao getRecordGodsjf(SystemaWebUser appUser, GodsjfDao recordToValidate){
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
			urlRequestParams.append("&gotrnr=" + recordToValidate.getGotrnr());
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
    		for(GodsjfDao tmpRecord: listContainer.getList()){
    			record = tmpRecord;
    			logger.info("TRNR:" + record.getGotrnr());
    		}
    	}	
    	return record;
	}
	
	/**
	 * 
	 * @param appUser
	 * @param gggn1
	 * @return
	 */
	private GodsgfDao getRecordGodsgf(SystemaWebUser appUser, String gggn1){
		GodsgfDao record = new GodsgfDao();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSGF_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		
		if(strMgr.isNotNull(gggn1)){
			urlRequestParams.append("&gggn1=" + gggn1);
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
    		JsonContainerDaoGODSGF container = this.godsnoService.getContainerGodsgf(jsonPayload);
    		for(GodsgfDao tmpRecord: container.getList()){
    			record = tmpRecord;
    		}
    	}		
		return record;
	}
	
	/**
	 * 
	 * @param appUser
	 * @param wssavd
	 * @return
	 */
	private Collection<GodsafDao> getBeviljningsKodeList(SystemaWebUser appUser){
		Collection<GodsafDao> outputList = new ArrayList<GodsafDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSAF_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSAF listContainer = this.godsnoService.getContainerGodsaf(jsonPayload);
    		outputList = listContainer.getList();	
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

