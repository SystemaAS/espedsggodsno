package no.systema.godsno.z.maintenance.main.controller;

import java.util.*;
import javax.annotation.PostConstruct;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//application imports
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.validator.LoginValidator;
import no.systema.main.util.AppConstants;
import no.systema.main.util.JsonDebugger;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.util.StringManager;

import no.systema.jservices.common.dao.GodsfiDao;
import no.systema.jservices.common.dao.GodsjfDao;
//GODSNO
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.filter.SearchFilterGodsnoMainList;
import no.systema.godsno.mapper.url.request.UrlRequestParameterMapper;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.model.JsonContainerDaoGODSFI;
import no.systema.godsno.util.manager.CodeDropDownMgr;
import no.systema.godsno.z.maintenance.main.validator.GodsnoMaintenanceGodsfiValidator;
import no.systema.godsno.z.maintenance.main.model.MaintenanceMainListObject;

/**
 * Godsregistrering-NO maintenance Bevill.koder Controller 
 * 
 * @author oscardelatorre
 * @date  Sep 2018
 * 
 */

@Controller
@SessionAttributes(AppConstants.SYSTEMA_WEB_USER_KEY)
@Scope("session")
public class GodsnoMaintenanceBevillkoderController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(2000);
	private static Logger logger = Logger.getLogger(GodsnoMaintenanceBevillkoderController.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	private UrlRequestParameterMapper urlRequestParameterMapper = new UrlRequestParameterMapper();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
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
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsnomaintenance_bevillkoder.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doInit(ModelMap model, HttpSession session, HttpServletRequest request){
		logger.info("Inside: doInit");
		
		ModelAndView successView = new ModelAndView("godsnomaintenance_bevillkoder");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			List list = this.populateMaintenanceBevillkoderMainList();
			model.addAttribute("list", list);
			appUser.setActiveMenu(SystemaWebUser.ACTIVE_MENU_GODSREGNO_MAINTENANCE_ONE);
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			
			successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
	    		
			return successView;
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
	@RequestMapping(value="godsnomaintenance_bevillkoder_godsfi.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doGetListGodsfi(@ModelAttribute ("record") GodsfiDao recordToValidate, BindingResult bindingResult,HttpSession session, HttpServletRequest request){
		logger.info("Inside: doGetListGodsfi");
		Map model = new HashMap();
		ModelAndView successView = new ModelAndView("godsnomaintenance_bevillkoder_godsfi");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			List<GodsfiDao> list = (List)this.getListGodsfi(appUser);
			model.put("list", list);
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			
			successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
	    		
			return successView;
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
	@RequestMapping(value="godsnomaintenance_bevillkoder_godsfi_edit.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doEditGodsfi(ModelMap modelS, @ModelAttribute ("record") GodsfiDao recordToValidate, BindingResult bindingResult,HttpSession session, HttpServletRequest request){
		logger.info("Inside: doEditGodsfi");
		Map model = new HashMap();
		ModelAndView successView = new ModelAndView("godsnomaintenance_bevillkoder_godsfi");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		
		String action = request.getParameter("action");
		String updateId = request.getParameter("updateId");
		model.put("updateId", updateId);
		model.put("action", action);
		
		boolean isValidRecord = true;
		
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			if(GodsnoConstants.ACTION_UPDATE.equals(action)){
				int dmlRetval = 0;
				StringBuffer errMsg = new StringBuffer();
				//validator
				GodsnoMaintenanceGodsfiValidator validator = new GodsnoMaintenanceGodsfiValidator();
				validator.validate(recordToValidate, bindingResult);
			    //check for ERRORS
				if(bindingResult.hasErrors()){
		    		logger.info("[ERROR Validation] record does not validate)");
		    		//put domain objects and do go back to the successView from here
		    		//drop downs
		    		isValidRecord = false;
		    		
			    }else{
			    	dmlRetval = this.updateRecordGodsfi(appUser.getUser(), recordToValidate, "U", errMsg);
			    	if(dmlRetval<0){
						logger.info("ERROR on update ... ??? check your code");
						modelS.addAttribute(GodsnoConstants.ASPECT_ERROR_MESSAGE, errMsg.toString());
						
					}else{
						logger.info("doUpdate = OK");
						modelS.addAttribute("record", new GodsfiDao());
					}
			    }
			}
			List<GodsfiDao> list = (List)this.getListGodsfi(appUser);
			model.put("list", list);
			successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
	    }
		
		return successView;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private List<MaintenanceMainListObject> populateMaintenanceBevillkoderMainList(){
		List<MaintenanceMainListObject> listObject = new ArrayList<MaintenanceMainListObject>();
		
		MaintenanceMainListObject object = new  MaintenanceMainListObject();
		        
		object.setId("1");
		object.setSubject("Lage bevill.koder");
		object.setDbTable("GODSFI");
		object.setStatus("G");
		object.setPgm("godsfi");
		listObject.add(object);
		//
		object = new  MaintenanceMainListObject();
		object.setId("2");
		object.setSubject("Låse avd/bevill.koder");
		object.setDbTable("GODSAF");
		object.setStatus("G");
		object.setPgm("godsaf");
		listObject.add(object);
		
		return listObject;
	}
	
	/**
	 * 
	 * @param appUser
	 * @return
	 */
	private Collection<GodsfiDao> getListGodsfi(SystemaWebUser appUser){
		Collection<GodsfiDao> outputList = new ArrayList<GodsfiDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSFI_LIST_URL;
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
    		JsonContainerDaoGODSFI listContainer = this.godsnoService.getContainerGodsfi(jsonPayload);
    		outputList = listContainer.getList();	
    	}		
	    
		return outputList;
	}
	
	/**
	 * 
	 * @param applicationUser
	 * @param recordToValidate
	 * @param mode
	 * @param errMsg
	 * @return
	 */
	private int updateRecordGodsfi(String applicationUser, GodsfiDao recordToValidate, String mode, StringBuffer errMsg){
		int retval = 0;
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSFI_DML_UPDATE_URL;
		//add URL-parameters
		StringBuffer urlRequestKeys = new StringBuffer();
		urlRequestKeys.append("user=" + applicationUser);
		urlRequestKeys.append("&mode=" + mode);
		String urlRequestParams = urlRequestKeys + this.urlRequestParameterMapper.getUrlParameterValidString((recordToValidate));
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams);
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSFI listContainer = this.godsnoService.getContainerGodsfi(jsonPayload);
    		if(listContainer!=null){
    			if(strMgr.isNotNull(listContainer.getErrMsg())){
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
	
	
}

