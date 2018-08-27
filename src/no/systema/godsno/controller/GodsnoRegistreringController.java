package no.systema.godsno.controller;

import java.util.*;
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
import no.systema.jservices.common.dao.GodsjfDao;
import no.systema.jservices.common.dao.GodsgfDao;

//GODSNO
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.validator.GodsnoRegistreringValidator;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.util.manager.GodsnrManager;
import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.model.JsonContainerDaoGODSGF;
import no.systema.godsno.filter.SearchFilterGodsnoMainList;
import no.systema.godsno.mapper.url.request.UrlRequestParameterMapper;

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
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	DateTimeManager dateMgr = new DateTimeManager();
	private UrlRequestParameterMapper urlRequestParameterMapper = new UrlRequestParameterMapper();
	
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
	@RequestMapping(value="godsno_edit.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doGodsnoEdit(ModelMap model, @ModelAttribute ("record") GodsjfDao recordToValidate, BindingResult bindingResult, HttpSession session, HttpServletRequest request){
		ModelAndView successView = new ModelAndView("godsno_edit");
		logger.info("Inside: doGodsnoEdit");
		
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		
		String action = request.getParameter("action");
		String avd = request.getParameter("avd");
		String sign = request.getParameter("sign");
		String updateFlag = request.getParameter("updateFlag");
		String gognManualCounter = strMgr.leadingStringWithNumericFiller(request.getParameter("gognManualCounter"), 2, "0");
		logger.info("action:" + action);
		logger.info("gognManualCounter:" + gognManualCounter);
		
		if(strMgr.isNotNull(updateFlag)){
			model.addAttribute("updateFlag", "1");
		}
		boolean isValidRecord = true;
		
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			logger.info("ASAVD:" + appUser.getAsavd());
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			
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
			    	//Start DML operations if applicable
					StringBuffer errMsg = new StringBuffer();
					int dmlRetval = 0;
					
					if(strMgr.isNotNull( recordToValidate.getGogn()) ){
						//Add or Update
						if(strMgr.isNotNull(updateFlag)){
							logger.info("doUpdate");
							dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_UPDATE, errMsg);
							
						}else{
							logger.info("doCreate branch starting...");
							String godsNrOriginalValue = recordToValidate.getGogn();
							//duplicate check
							if(this.recordExistsGodsjf(appUser, recordToValidate, errMsg)){
								logger.info("duplicate exists ... ??????? :-(" );
								//duplicate found
								dmlRetval = -1;
							}else{
								logger.info("no duplicate exists ...");
								
								//CREATE NEW with manual counter (Only main table is updated)
								if(strMgr.isNotNull(gognManualCounter)){
									logger.info("Create new with manual counter ...");
									recordToValidate.setGogn(recordToValidate.getGogn() + gognManualCounter);
									dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
								
								//CREATE NEW with automatic counter (Both: main table and secondary table (GODSGF=teller table) are updated.	
								}else{
									logger.info("Create new with no manual counter ...");
									//return to the original value (without the counter suffix since this is the one we will calculate now)
									recordToValidate.setGogn(godsNrOriginalValue);
									//Start process
									if(this.recordExistsGodsgf(appUser, recordToValidate)){
										logger.info("Record in teller table exists...");
										GodsgfDao tmpRecord = this.getRecordGodsgf(appUser, recordToValidate.getGogn());
										//set complete godsnr with counter
										recordToValidate.setGogn(recordToValidate.getGogn() + tmpRecord.getGggn2());
										//(1) Create main record
										logger.info("doCreate");
										dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
										//(2) Update counter record
										logger.info("doUpdate - teller (Godsgf)");
										GodsgfDao godsgfDao  = this.increaseCounter(godsNrOriginalValue, tmpRecord.getGggn2());
										int dmlSec = this.updateRecordGodsnrCounter(appUser.getUser(), godsgfDao, GodsnoConstants.MODE_UPDATE, errMsg);
									}else{
										logger.info("Record in teller table DOES NOT exist...");
										//set complete godsnr with counter
										String firstCounter = "01";
										recordToValidate.setGogn(recordToValidate.getGogn() + firstCounter);
										logger.info("doCreate");
										dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
										logger.info("doCreate - teller (Godsgf)");
										GodsgfDao godsgfDao  = this.increaseCounter(godsNrOriginalValue, firstCounter);
										int dmlSec = this.updateRecordGodsnrCounter(appUser.getUser(), godsgfDao, GodsnoConstants.MODE_ADD, errMsg);
									}
								}
							}
						}
						logger.info(Calendar.getInstance().getTime() + " CONTROLLER end - timestamp");
					}
					if(dmlRetval<0){
						isValidRecord = false;
						model.addAttribute(GodsnoConstants.ASPECT_ERROR_MESSAGE, errMsg.toString());
					}else{
						//Create OK. Prepare for upcoming Update
						model.addAttribute("updateFlag", "1");
					}
			    }
			}
			//--------------
			//Fetch record
			//--------------
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
				
			}
			
			if(action==null || "".equals(action)){ 
				action = "doUpdate";	
			}else if (action.equals(GodsnoConstants.ACTION_CREATE)){
				this.calculateGodsNr_withoutCounter(avd, appUser, model, recordToValidate);
				action = "doUpdate";
			}
			
			model.addAttribute("action", action);
			model.addAttribute("avd", avd);
			logger.info("AVD:" + avd);
			
			//set some other model values
			this.populateUI_ModelMap(model);
			
			//successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
			return successView;
		}
	}
	/**
	 * 
	 * @param gggn1
	 * @param gggn2
	 * @return
	 */
	private GodsgfDao increaseCounter(String gggn1, String gggn2){
		GodsgfDao dao = new GodsgfDao();
		dao.setGggn1(gggn1);
		//next value for counter
		try{
			int gggn2Int = Integer.parseInt(gggn2);
			gggn2Int++;
			dao.setGggn2(strMgr.leadingStringWithNumericFiller((String.valueOf(gggn2Int)), 2, "0"));		
		}catch(Exception e){
			logger.info("CATCH EXCEPTION:" + e.toString());
		}
		return dao;		
	}
	
	/**
	 * 
	 * @param model
	 * @param recordToValidate
	 * @param bindingResult
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsno_delete.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doGodsnoDelete(ModelMap model, @ModelAttribute ("record") GodsjfDao recordToValidate, BindingResult bindingResult, HttpSession session, HttpServletRequest request){
		ModelAndView successView = new ModelAndView("redirect:godsno_mainlist.do?action=doFind&rd=1");
		logger.info("Inside: doGodsnoDelete");
		final String DELETE_TEXT_ON_DB = "*SLETTET";
		
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			//----------------------------
			//Fetch record and update it 
			//----------------------------
			if(strMgr.isNotNull(recordToValidate.getGogn()) ){
				int dmlRetval = 0;
				StringBuffer errMsg = new StringBuffer();
				//fetch record
				GodsjfDao recordToDelete = this.getRecordGodsjf(appUser, recordToValidate);
				if(recordToDelete!=null && strMgr.isNotNull( recordToDelete.getGogn()) ){
					//adjust some db-fields
					recordToDelete.setGotrnr(DELETE_TEXT_ON_DB);
					this.adjustFieldsForUpdate(recordToDelete);
					logger.info("doDelete");
					//Update with delete flag
					dmlRetval = this.updateRecord(appUser.getUser(), recordToDelete, GodsnoConstants.MODE_UPDATE, errMsg);
					if(dmlRetval<0){
						logger.info("ERROR on delete ... ??? check your code");
						model.addAttribute(GodsnoConstants.ASPECT_ERROR_MESSAGE, errMsg.toString());
						
					}else{
						logger.info("doDelete = OK");
					}
					
				}
				
			}

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
	 * 
	 * @param applicationUser
	 * @param recordToValidate
	 * @param mode
	 * @param errMsg
	 * @return
	 */
	private int updateRecordGodsnrCounter(String applicationUser, GodsgfDao recordToValidate, String mode, StringBuffer errMsg ){
		int retval = 0;
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSGF_DML_UPDATE_URL;
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
    		JsonContainerDaoGODSGF container = this.godsnoService.getContainerGodsgf(jsonPayload);
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
	 * There are several STEPs involved in the calculation of the GodsNr
	 * The returning godsnr (to the end-user) will be without the final counter. 
	 * The counter (2 last numbers) will be first calculated when the final record is saved
	 * 
	 * @param avd
	 * @param appUser
	 * @param model
	 */
	private void calculateGodsNr_withoutCounter(String avd, SystemaWebUser appUser, ModelMap model, GodsjfDao recordToValidate){
		String ERROR_CODE_BEVKODE = "XXXXX";
		GodsnrManager godsnrMgr = new GodsnrManager();
		//get Godsnr bev.kode since we will be creating a new record...
		Collection<GodsafDao> list = this.getBeviljningsKodeList(appUser);
		//-------
		//STEP 1: Calculate the godsNr bev.kode
		//-------
		godsnrMgr.getGodsnrBevKode_PatternA(avd, list);
		logger.info("bev.kode (PATTERN A):" + godsnrMgr.getGodsNrBevKode());
		if(godsnrMgr.getGodsNrBevKode()==null){
			godsnrMgr.getGodsnrBevKode_PatternB(avd, list);
			logger.info("bev.kode (PATTERN B):" + godsnrMgr.getGodsNrBevKode());
			if(godsnrMgr.getGodsNrBevKode()==null){
				godsnrMgr.setGodsNrBevKode(ERROR_CODE_BEVKODE);
			}
		}
		if(ERROR_CODE_BEVKODE.equals(godsnrMgr.getGodsNrBevKode())){
			godsnrMgr.setGodsNr("FEIL: avd mangler bev.kode");
		}else{
			//-------
			//STEP 2: Calculate the godsNr with: Year + bev.kode + daynr: yyyy12345ddd
			//-------
			godsnrMgr.setGodsNrWithBevKode(godsnrMgr.getGodsNrBevKode());
			//put the 1-character (default = 0). If std-enhets-kode exists: put it there, otherwise = default = 0
			String enhetsKode = "0";
			if(strMgr.isNotNull(godsnrMgr.getStdEnhetsKode())){
				enhetsKode = godsnrMgr.getStdEnhetsKode();
			}
			godsnrMgr.setGodsNr(godsnrMgr.getGodsNr() + enhetsKode);
			logger.info("STEP 2(godsnr):" + godsnrMgr.getGodsNr());
		}
		//Now send the proposed GodsNr
		model.addAttribute("godsnr", godsnrMgr.getGodsNr());
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
			errMsg.append("Record:" + newRecord.getGogn());
			errMsg.append(" already exists... ? ");
			logger.info(errMsg.toString());
			retval = true;
		}
		return retval;
	}
	/**
	 * 
	 * @param appUser
	 * @param godsNrHelperMap
	 * @return
	 */
	private boolean recordExistsGodsgf(SystemaWebUser appUser, GodsjfDao recordToValidate){
		boolean retval = false;
		
		GodsgfDao newRecord = this.getRecordGodsgf(appUser, recordToValidate.getGogn());
		if(newRecord!=null && strMgr.isNotNull(newRecord.getGggn1())){
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
		
		//Numbers... since the fucking Spring converter is not working ...
		if(recordToValidate.getGotrdt()==null){ recordToValidate.setGotrdt(0); }
		//date and time
		if(recordToValidate.getGogrdt()==null){ recordToValidate.setGogrdt("0"); }
		if(recordToValidate.getGogrkl()==null){ recordToValidate.setGogrkl(0); }
		//date and time
		if(recordToValidate.getGolsdt()==null){ recordToValidate.setGolsdt("0"); }
		if(recordToValidate.getGolskl()==null){ recordToValidate.setGolskl(0); }
		
	}
	/**
	 * 
	 * @param recordToValidate
	 */
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

