package no.systema.godsno.controller;

import java.util.*;
import java.util.function.ToDoubleBiFunction;
import java.util.regex.*;

import javax.annotation.PostConstruct;


 
import org.slf4j.*;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import no.systema.jservices.common.dao.GodsjtDao;
import no.systema.jservices.common.dao.GodsgfDao;
import no.systema.jservices.common.dao.GodshfDao;
import no.systema.jservices.common.dao.MerknfDao;
import no.systema.jservices.common.dao.LlmrfDao;

//GODSNO
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.service.GodsnoLoggerService;
import no.systema.godsno.validator.GodsnoRegistreringValidator;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.util.manager.CodeDropDownMgr;
import no.systema.godsno.util.manager.GodsnrManager;
import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.model.JsonContainerDaoGODSFI;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.model.JsonContainerDaoGODSJT;
import no.systema.godsno.model.JsonContainerDaoGODSGF;
import no.systema.godsno.model.JsonContainerDaoMERKNF;
import no.systema.godsno.model.JsonContainerDaoLLMRF;

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
public class GodsnoRegistreringController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = LoggerFactory.getLogger(GodsnoRegistreringController.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	DateTimeManager dateMgr = new DateTimeManager();
	private UrlRequestParameterMapper urlRequestParameterMapper = new UrlRequestParameterMapper();
	private final String LOGGER_CODE_EDIT = "E";
	private final String LOGGER_CODE_NEW = "N";
	private final String DEFAULT_TPAPIR_TYPE_T1 = "T1";
	private final String DEFAULT_TPAPIR_TYPE_T2 = "T2";
	private final String DATE_MASK_NORWAY = "ddMMyy";
	
	@Autowired
	private GodsnoService godsnoService;

	@Autowired
	private CodeDropDownMgr codeDropDownMgr;
	
	@Autowired
	private GodsnoLoggerService godsnoLoggerService;
	
	@PostConstruct
	public void initIt() throws Exception {
		if("DEBUG".equals(AppConstants.LOG4J_LOGGER_LEVEL)){
			 
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
		String overrideFromDayUserInput = request.getParameter("overrideFromDayUserInput");
		
		String updateFlag = request.getParameter("updateFlag");
		String gognManualCounter = strMgr.leadingStringWithNumericFiller(request.getParameter("gognManualCounter"), 2, "0");
		String gotrnrOrig = request.getParameter("gotrnrOrig");
		String pos1TargetString = request.getParameter("pos1TargetString");
		String tmpGogn = request.getParameter("tmpGogn");
		String ownTmpGognOffset = request.getParameter("ownTmpGognOffset"); //check-box
		
		//Special case for gotrty
		if(strMgr.isNull(recordToValidate.getGotrty())){
			recordToValidate.setGotrty(this.DEFAULT_TPAPIR_TYPE_T1	);
		}
		if(strMgr.isNull(recordToValidate.getGoorty())){
			if(strMgr.isNotNull(recordToValidate.getGoortn())){
				recordToValidate.setGoorty(this.DEFAULT_TPAPIR_TYPE_T2);
			}
		}

		logger.info("action:" + action);
		logger.info("pos1TargetString:" + pos1TargetString);
		
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
					//----------------------------------------------------
					//check and adjust godsNr if it was done by user input
					//----------------------------------------------------
					if(this.godsNrInputManually(request)){
						//the counter (xx) is not present here
						recordToValidate.setGogn(this.constructGodsNrManually(request));
						
					}
					//--------------------------------------------------------------------------------
					//at this point we do have a godsnr well-formed (either manually or automatically)
					//--------------------------------------------------------------------------------
					if(strMgr.isNotNull( recordToValidate.getGogn()) ){
						//Add or Update
						if(strMgr.isNotNull(updateFlag)){
							logger.info("doUpdate");
							if(gotrnrOrig!=null && gotrnrOrig.equals(recordToValidate.getGotrnr())){
								dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_UPDATE, errMsg);
								if(dmlRetval>=0){
									//Gods-logger (footprint after each DML-success operation)
									this.godsnoLoggerService.logIt(appUser.getUser(), recordToValidate.getGogn(), recordToValidate.getGotrnr(), this.LOGGER_CODE_EDIT, errMsg);
									//CREATE a record in GODSJT (if applicable)
									//only create new since the GUI will present ONLY the ones valid for creation
									if(strMgr.isNotNull(pos1TargetString)){
										this.updateRecordGodsjt(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg, pos1TargetString);
									}
								}
							}else{
								//duplicate check
								if(this.recordExistsGodsjf(appUser, recordToValidate, errMsg)){
									logger.info("duplicate exists ... ??????? :-(" );
									recordToValidate.setGotrnr(gotrnrOrig);//rescue the original value in this special case
									//duplicate found
									dmlRetval = -1;
								}else{
									//meaning the end-user changed the gotrnr (key) to a non-existent transittnr (valid key)
									dmlRetval = this.updateRecordSpecialTransittnrCase(appUser.getUser(), gotrnrOrig, recordToValidate, GodsnoConstants.MODE_UPDATE_TRANSITT_KEY, errMsg);
									if(dmlRetval>=0){
										//Gods-logger (footprint after each DML-success operation)
										this.godsnoLoggerService.logIt(appUser.getUser(), recordToValidate.getGogn(), recordToValidate.getGotrnr(), this.LOGGER_CODE_EDIT, errMsg);
										//CREATE a record in GODSJT (if applicable)
										//only create new since the GUI will present ONLY the ones valid for creation
										if(strMgr.isNotNull(pos1TargetString)){
											this.updateRecordGodsjt(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg, pos1TargetString);
										}
									}
								}
							}
							
						}else{
							logger.info("doCreate branch starting...");
							String godsNrOriginalValue = recordToValidate.getGogn();
							//duplicate check
							if(this.recordExistsGodsjf(appUser, recordToValidate, errMsg)){
								logger.info("duplicate exists ... ??????? :-(" );
								//duplicate found
								dmlRetval = -1;
							}else{
								
								//CREATE NEW with manual counter (Only main table is updated)
								if(strMgr.isNotNull(gognManualCounter) ){
									logger.info("Create new with manual counter ...");
									recordToValidate.setGogn(recordToValidate.getGogn() + gognManualCounter);
									dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
									//Gods-logger (footprint after each DML-success operation)
									if(dmlRetval>=0){
										this.godsnoLoggerService.logIt(appUser.getUser(), recordToValidate.getGogn(), recordToValidate.getGotrnr(), this.LOGGER_CODE_NEW, errMsg);
									}
								
								//CREATE NEW with automatic counter (Both: main table and secondary table (GODSGF=teller table) are updated.	
								}else{
									logger.info("Create new with no manual counter ...");
									//return to the original value (without the counter suffix since this is the one we will calculate now)
									recordToValidate.setGogn(godsNrOriginalValue);
									logger.info("GOGN hittills:" + recordToValidate.getGogn());
									//check if the end-user has overridden gogn_Nr totally and written his/her own gogn-Nr
									if(this.isTotallyCompletedGognFromUser(tmpGogn)){
										//set complete godsnr from GUI
										recordToValidate.setGogn(tmpGogn);
										//(1) Create main record
										logger.info("doCreate with (tmpGogn) Gogn = <end-user> input via checkbox:" + recordToValidate.getGogn());
										dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
										
									}else if(this.isTotallyCompletedGognFromUser(recordToValidate.getGogn())){
										//(1) Create main record
										logger.info("doCreate with (gogn) Gogn = <end-user> input via checkbox:" + recordToValidate.getGogn());
										dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
										
									}else{
										
										//Compare Gogn vs tmpGogn in order to elucidate if the user has edited the gogn (with checkbox)and leave xx left
										//if they are not equal: use tmpGogn minus <xx> in order to respect the user entrance ...
										if(strMgr.isNotNull(tmpGogn)){
											tmpGogn = tmpGogn.replace("xx", "");
											logger.info("GOGN tmpGogn:" + recordToValidate.getGogn());
											if(!tmpGogn.equals(recordToValidate.getGogn())){
												recordToValidate.setGogn(tmpGogn);
											}
										}else if(strMgr.isNotNull(recordToValidate.getGogn())) {
											logger.info("GOGN REAL:" + recordToValidate.getGogn());
											if(recordToValidate.getGogn().contains("xx")){
												recordToValidate.setGogn(recordToValidate.getGogn().replace("xx", ""));
											}
											
										}
										
										//Start process 
										if(this.recordExistsGodsgf(appUser, recordToValidate)){
											logger.info("Record in teller table exists...");
											GodsgfDao tmpRecord = this.getRecordGodsgf(appUser, recordToValidate.getGogn());
											//set complete godsnr with counter
											recordToValidate.setGogn(recordToValidate.getGogn() + tmpRecord.getGggn2());
											
											//(1) Create main record
											logger.info("doCreate");
											dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
											if(dmlRetval>=0){
												//Gods-logger (footprint after each DML-success operation)
												this.godsnoLoggerService.logIt(appUser.getUser(), recordToValidate.getGogn(), recordToValidate.getGotrnr(), this.LOGGER_CODE_NEW, errMsg);
												
												//(2) Update counter record
												logger.info("doUpdate - teller (Godsgf)");
												GodsgfDao godsgfDao  = this.increaseCounter(godsNrOriginalValue, tmpRecord.getGggn2());
												int dmlSec = this.updateRecordGodsnrCounter(appUser.getUser(), godsgfDao, GodsnoConstants.MODE_UPDATE, errMsg);
												
											}
										}else{
											logger.info("Record in teller table DOES NOT exist...");
											//set complete godsnr with counter
											String firstCounter = "01";
											recordToValidate.setGogn(recordToValidate.getGogn() + firstCounter);
											logger.info("doCreate");
											dmlRetval = this.updateRecord(appUser.getUser(), recordToValidate, GodsnoConstants.MODE_ADD, errMsg);
											if(dmlRetval>=0){
												//Gods-logger (footprint after each DML-success operation)
												this.godsnoLoggerService.logIt(appUser.getUser(), recordToValidate.getGogn(), recordToValidate.getGotrnr(), this.LOGGER_CODE_NEW, errMsg);
												
												//(2) Update counter record
												logger.info("doCreate - teller (Godsgf)");
												GodsgfDao godsgfDao  = this.increaseCounter(godsNrOriginalValue, firstCounter);
												int dmlSec = this.updateRecordGodsnrCounter(appUser.getUser(), godsgfDao, GodsnoConstants.MODE_ADD, errMsg);
											}
										}
									}	
								}
							}
						}
						logger.info(Calendar.getInstance().getTime() + " CONTROLLER end - timestamp");
					}
					if(dmlRetval<0){
						isValidRecord = false;
						model.addAttribute(GodsnoConstants.ASPECT_ERROR_MESSAGE, errMsg.toString());
						if(strMgr.isNotNull(updateFlag)){
							//Nothing
						}else{
							//Error on create new
							action = "ERROR_ON_CREATE";
							model.addAttribute("tmpGogn",request.getParameter("tmpGogn"));
						}
					}else{
						//Create OK. Prepare for upcoming Update
						model.addAttribute("updateFlag", "1");
					}
			    }
			}
			//--------------
			//Fetch record
			//--------------
			GodsjfDao auxDao = null;
			if(strMgr.isNotNull(recordToValidate.getGogn()) ){
				if(isValidRecord){
					GodsjfDao updatedDao = this.getRecordGodsjf(appUser, recordToValidate);
					this.adjustFieldsForFetch(updatedDao);
					//get default date in order to keep creating new records from this UI
					overrideFromDayUserInput = this.getUserDateFromGodsNrDayNr(updatedDao);
					model.addAttribute("overrideFromDayUserInput", overrideFromDayUserInput);
					model.addAttribute(GodsnoConstants.DOMAIN_RECORD, updatedDao);
					auxDao = updatedDao;
					
				}else{
					//in case of validation errors
					this.adjustFieldsForFetch(recordToValidate);
					//get default date in order to keep creating new records from this UI
					overrideFromDayUserInput = this.getUserDateFromGodsNrDayNr(recordToValidate);
					model.addAttribute("overrideFromDayUserInput", overrideFromDayUserInput);
					model.addAttribute(GodsnoConstants.DOMAIN_RECORD, recordToValidate);
					auxDao = recordToValidate;
				}
				//Fetch all children item lines
				model.addAttribute("merknadList", this.getListMerknf(appUser, auxDao.getGogn(), auxDao.getGotrnr()));
				model.addAttribute("hfLoggerList", this.godsnoLoggerService.getLogHfList(appUser, this.getGodshrDao(auxDao)));
				model.addAttribute("jtPosList", this.getListGodsjtPosisjon(appUser, auxDao.getGogn(), auxDao.getGotrnr()));
				
				if(this.releaseRecordExist(appUser, auxDao.getGoortn())){
					model.addAttribute("releaseRecordExists", "1");
				}
				
			}
			
			if(action==null || "".equals(action)){ 
				action = "doUpdate";	
			}else if (action.equals(GodsnoConstants.ACTION_CREATE) || action.equals("ERROR_ON_CREATE")){
				//some default values
				recordToValidate.setGomott("Diverse mottak.");
				String calcGodsNr = this.calculateGodsNrAutomatically_withoutCounter(avd, overrideFromDayUserInput, appUser, model, recordToValidate);
				String dayNrOfYear = dateMgr.getDayNrOfYear();
				if(strMgr.isNotNull(overrideFromDayUserInput)){
					dayNrOfYear = dateMgr.getDayNrOfYear(overrideFromDayUserInput, this.DATE_MASK_NORWAY);
				}
				//START This is used only for user input but we send it also as information when the godsnr was automatically calculated
				model.addAttribute("dayOfYear", dayNrOfYear);
				//get Godsnr bev.kode since we will be creating a new record...
				List<GodsfiDao> fiList = (List)this.getListGodsfi(appUser);
				//OBSOLETE? ... List<GodsafDao> afList = (List)this.getBeviljningsKodeList(appUser);
				//OBSOLETE? ... List<GodsfiDao> bevKodeList = this.getMatchedBevKodeList(afList, fiList);
				
				model.addAttribute("bevKodeListMainTbl", fiList);
				//END 
				action = "doUpdate";
				if(action.equals("ERROR_ON_CREATE")){
					action = "doCreate";
				}
			}
			//get aspects
			this.setCodeDropDownMgr(appUser, model);
			
			model.addAttribute("action", action);
			model.addAttribute("avd", avd);
			logger.info("AVD:" + avd);
			logger.info("action:" + action);
			logger.info("updateFlag:" + updateFlag);
			//arrange Goavd for presentation
		    
			//set some other model values
			this.populateUI_ModelMap(model);
			
			//successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
			return successView;
		}
	}
	
	/**
	 * 
	 * @param recordToValidate
	 * @return
	 */
	public GodshfDao getGodshrDao(GodsjfDao recordToValidate){
		GodshfDao dao = new GodshfDao();
		dao.setGogn(recordToValidate.getGogn());
		return dao;
	}
	/**
	 * 
	 * @param tmpGogn
	 * @return
	 */
	public boolean isTotallyCompletedGognFromUser(String value){
		boolean retval = false;
		if(strMgr.isNotNull(value) && !value.contains("xx")){
			retval = true;
		}
		return retval;
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
					logger.info("doDelete");
					//Update with delete flag
					dmlRetval = this.deleteRecord(appUser.getUser(), recordToDelete, GodsnoConstants.MODE_DELETE, errMsg);
					if(dmlRetval<0){
						logger.info("ERROR on delete ... ??? check your code");
						model.addAttribute(GodsnoConstants.ASPECT_ERROR_MESSAGE, errMsg.toString());
						
					}else{
						logger.info("doDelete = OK");
						//Gods-logger (footprint after each DML-success operation)
						this.godsnoLoggerService.logIt(appUser.getUser(), recordToValidate.getGogn(), recordToValidate.getGotrnr(), this.LOGGER_CODE_EDIT, errMsg);
						
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
	private int deleteRecord(String applicationUser, GodsjfDao recordToValidate, String mode, StringBuffer errMsg ){
		int retval = 0;
		//---------------
    	//DML
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
	 * Goavg is 15(VARCHAR) long. The last 3-positions are reserved for the T-papirtype. The first 12-chars are the Avg.sted
	 * @param request
	 * @return
	 */
	/*
	private String constructGoavg(HttpServletRequest request, ModelMap model){
		String DEFAULT_TPAPIR_TYPE = "T1";
		int FILLER_LIMIT = 12;
		String FILLER_CHAR = " ";
		String retval = "";
		
		String owngoavg_ptype = request.getParameter("owngoavg_ptype");
		String owngoavg_toll = request.getParameter("owngoavg_toll");
		if(strMgr.isNull(owngoavg_ptype)){
			owngoavg_ptype = DEFAULT_TPAPIR_TYPE;
		}
		if(strMgr.isNull(owngoavg_toll)){
			owngoavg_toll = ""; //important for trailing function below
		}
	
		owngoavg_toll = strMgr.trailingStringWithFiller(owngoavg_toll, FILLER_LIMIT, FILLER_CHAR);
		//set value
		retval = owngoavg_toll + owngoavg_ptype;
		//
		model.addAttribute("owngoavg_ptype", owngoavg_ptype);
		model.addAttribute("owngoavg_toll", owngoavg_toll.trim());
		
				
		return retval;
	}
	*/
	/**
	 * Construct the godsNr from user input
	 * @param request
	 * @return
	 */
	private String constructGodsNrManually(HttpServletRequest request){
		
		String godsnrYear = request.getParameter("owngogn_1");
		String godsnrBevKodeRaw = request.getParameter("owngogn_2");
		String godsnrDayOfYear = request.getParameter("owngogn_3");
		//process the raw godsnr in order to separate enhetskode
		String[] tmpRecord = godsnrBevKodeRaw.split("_"); 
		String godsnrBevKode = tmpRecord[0];
		String godsnrEnhetsKod = "0";
		if(tmpRecord.length>1){
			godsnrEnhetsKod = tmpRecord[1];
		}
		StringBuffer godsNr = new StringBuffer();
		godsNr.append(godsnrYear);
		godsNr.append(godsnrBevKode);
		godsNr.append(godsnrDayOfYear);
		godsNr.append(godsnrEnhetsKod);
		
		return godsNr.toString();
		
	}
	/**
	 * Check it the godsnr was input by the end-user
	 * @param request
	 * @return
	 */
	private boolean godsNrInputManually(HttpServletRequest request){
		boolean retval = false;
		if(strMgr.isNotNull(request.getParameter("owngogn_1")) ){
			retval = true;
		}
		return retval;
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
	 * @param applicationUser
	 * @param gotrnrOrig
	 * @param recordToValidate
	 * @param mode
	 * @param errMsg
	 * @return
	 */
	private int updateRecordSpecialTransittnrCase(String applicationUser, String gotrnrOrig, GodsjfDao recordToValidate, String mode, StringBuffer errMsg ){
		int retval = 0;
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSJF_DML_UPDATE_URL;
		//add URL-parameters
		String urlRequestParamsKeys = "user=" + applicationUser + "&mode=" + mode + "&gotrnrOrig=" + gotrnrOrig;
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
	 
	 * @param avd
	 * @param overrideFromDayUserInput
	 * @param appUser
	 * @param model
	 * @param recordToValidate
	 * @return
	 */
	private String calculateGodsNrAutomatically_withoutCounter(String avd, String overrideFromDayUserInput, SystemaWebUser appUser, ModelMap model, GodsjfDao recordToValidate){
		boolean ONLY_ONE_BEVKODE_FOR_FIRM = false;
		
		String retval = "";
		String ALERT_CODE_BEVKODE_MISSING = "XXXXX";
		GodsnrManager godsnrMgr = new GodsnrManager(this.DATE_MASK_NORWAY);
		//get Godsnr bev.kode since we will be creating a new record...
		Collection<GodsafDao> list = this.getBeviljningsKodeList(appUser);
		//save this list for information purposes on GUI
		model.addAttribute("bevKodeList", list);
		
		//Check if we use the only bevk. that exists,
		List<GodsfiDao> fiList = (List)this.getListGodsfi(appUser);
		
		//TEST TEST TEST Single record!
		/*List singletonList = new ArrayList();
		for(GodsfiDao rec : fiList){
			GodsfiDao x = new GodsfiDao();
			x = rec;
			if(rec.getGflbko().length()==5){
				singletonList.add(x);
				break;
			}
		}
		if(singletonList!=null && singletonList.size()==1){
			ONLY_ONE_BEVKODE_FOR_FIRM = true;
		}
		*/
		
		if(fiList!=null && fiList.size()==1){
			ONLY_ONE_BEVKODE_FOR_FIRM = true;
		}
		//We do not have to do nothing else since there is only one code ...
		if(ONLY_ONE_BEVKODE_FOR_FIRM){
			//PROD
			godsnrMgr.getGodsnrBevKode_PatternC(fiList);
			//-------
			//STEP 2: Calculate the godsNr with: Year + bev.kode + daynr: yyyy12345ddd
			//-------
			this.calculateGodsNrWithYearBevKodeDaynr(godsnrMgr, overrideFromDayUserInput);
			
		}else{
			//-------
			//STEP 1: Calculate the godsNr bev.kode
			//-------
			if(strMgr.isNotNull(avd)){
				
				godsnrMgr.getGodsnrBevKode_PatternA(avd, list);
				logger.info("bev.kode (PATTERN A):" + godsnrMgr.getGodsNrBevKode());
				if(godsnrMgr.getGodsNrBevKode()==null){
					godsnrMgr.getGodsnrBevKode_PatternB(avd, list);
					logger.info("bev.kode (PATTERN B):" + godsnrMgr.getGodsNrBevKode());
					if(godsnrMgr.getGodsNrBevKode()==null){
						godsnrMgr.setGodsNrBevKode(ALERT_CODE_BEVKODE_MISSING);
					}
				}
			}else{
				godsnrMgr.setGodsNrBevKode(ALERT_CODE_BEVKODE_MISSING);
			}
			//
			if( ALERT_CODE_BEVKODE_MISSING.equals(godsnrMgr.getGodsNrBevKode())){
				godsnrMgr.setGodsNr(dateMgr.getYear());
				retval = godsnrMgr.getGodsNr();
			}else{
				//-------
				//STEP 2: Calculate the godsNr with: Year + bev.kode + daynr: yyyy12345ddd
				//-------
				this.calculateGodsNrWithYearBevKodeDaynr(godsnrMgr, overrideFromDayUserInput);
			}
		}
		//Now send the proposed GodsNr
		model.addAttribute("godsnr", godsnrMgr.getGodsNr());
		//
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
		if(strMgr.isNotNull(recordToValidate.getGotrty())){
			recordToValidate.setGotrty(recordToValidate.getGotrty().toUpperCase());
		}
		if(strMgr.isNotNull(recordToValidate.getGoorty())){
			recordToValidate.setGoorty(recordToValidate.getGoorty().toUpperCase());
		}
		
	}
	/**
	 * 
	 * @param recordToValidate
	 */
	private void adjustFieldsForFetch(GodsjfDao recordToValidate){
		
		recordToValidate.setGogrdt(this.convertToDate_NO(recordToValidate.getGogrdt()));
		recordToValidate.setGolsdt(this.convertToDate_NO(recordToValidate.getGolsdt()));
		recordToValidate.setGotrdt(this.convertToDate_NO(recordToValidate.getGotrdt()));
		
	}
	/**
	 * 
	 * @param recordToValidate
	 * @return
	 */
	private String getUserDateFromGodsNrDayNr(GodsjfDao recordToValidate){
		//set defaults
		DateFormat formatter = new SimpleDateFormat("ddMMyy");
		Date date = Calendar.getInstance().getTime();
		String retval = formatter.format(date);
		
		try{
			String tmp = recordToValidate.getGogn();
			String numberOfDays = null;
			
			if( tmp!=null && tmp.length()==15 ){
				tmp = tmp.substring(9,12);
				numberOfDays = tmp;
				date = new SimpleDateFormat("D yyyy").parse(numberOfDays + " " + Calendar.getInstance().get(Calendar.YEAR));
				retval = formatter.format(date);
			}
			//
			logger.info("Converted dayNrYear to date:" + retval);
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		return retval;
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
	 * @param appUser
	 * @param gogn
	 * @param gotrnr
	 * @return
	 */
	private Collection<MerknfDao> getListMerknf(SystemaWebUser appUser, String gogn, String gotrnr){
		Collection<MerknfDao> outputList = new ArrayList<MerknfDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MERKNF_LIST_URL;
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
    	}		
	    
		return outputList;
	}
	/**
	 * Posisjon payload
	 * 
	 * @param appUser
	 * @param gtgn
	 * @param gttrnr
	 * @return
	 */
	private Collection<GodsjtDao> getListGodsjtPosisjon(SystemaWebUser appUser, String gtgn, String gttrnr){
		Collection<GodsjtDao> outputList = new ArrayList<GodsjtDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSJT_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		urlRequestParams.append("&gtgn=" + gtgn);
		urlRequestParams.append("&gttrnr=" + gttrnr);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSJT listContainer = this.godsnoService.getContainerGodsjt(jsonPayload);
    		outputList = listContainer.getList();	
    	}		
	    
		return outputList;
	}
	
	/**
	 * 
	 * @param appUser
	 * @param id
	 * @return
	 */
	private boolean releaseRecordExist(SystemaWebUser appUser, String id){
		boolean retval = false;
		logger.info("GOORTN:" + id);
		Collection<LlmrfDao> outputList = new ArrayList<LlmrfDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_LLMRF_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		
		if(strMgr.isNotNull(id)){
			urlRequestParams.append("&mrtrnr=" + id);
			//
			logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
	    	logger.info("URL: " + BASE_URL);
	    	logger.info("URL PARAMS: " + urlRequestParams);
	    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
	    	//Debug --> 
	    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
	    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
	    	if(jsonPayload!=null){
	    		JsonContainerDaoLLMRF listContainer = this.godsnoService.getContainerLlmrf(jsonPayload);
	    		outputList = listContainer.getList();
	    		for(LlmrfDao record : outputList){
	    			if(strMgr.isNull(record.getMrst())){
	    				retval = true;
	    			}
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
	 * @param pos1TargetString
	 * @return
	 */
	private int updateRecordGodsjt(String applicationUser, GodsjfDao recordToValidate, String mode, StringBuffer errMsg, String pos1TargetString ){
		int retval = 0;
		if(strMgr.isNotNull(pos1TargetString)){
			String [] fields = pos1TargetString.split(";");
			
			if(fields != null && fields.length>0){
				List<String> fieldList = Arrays.asList(fields);
				//Iterate on updates
				for(String recordPos1: fieldList){
					logger.info("Pos1:" + recordPos1);
					//---------------
			    	//Get main list
					//---------------
					final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSJT_DML_UPDATE_URL;
					//add URL-parameters
					String urlRequestParamsKeys = "user=" + applicationUser + "&mode=" + mode;
					StringBuffer urlRequestParams = new StringBuffer();
					urlRequestParams.append("&gtgn=" + recordToValidate.getGogn());
					urlRequestParams.append("&gttrnr=" + recordToValidate.getGotrnr());
					urlRequestParams.append("&gtpos1=" + recordPos1);
					
					//add params
					String urlRequestString= urlRequestParamsKeys + urlRequestParams.toString();
					
					//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
			    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
			    	logger.info("URL: " + BASE_URL);
			    	logger.info("URL PARAMS: " + urlRequestString);
			    	
			    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestString);
			    	//Debug --> 
			    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
			    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
			    	if(jsonPayload!=null){
			    		//TODO
			    		JsonContainerDaoGODSJT container = this.godsnoService.getContainerGodsjt(jsonPayload);
			    		if(container!=null){
			    			if(strMgr.isNotNull(container.getErrMsg())){
			    				errMsg.append(container.getErrMsg());
			    				//Error ... do something!
					    		logger.info("[ERROR] Record update - Error: " + errMsg.toString());
					    		retval = -1;
			    			}else{
			    				//Update successfully done!
					    		logger.info("[INFO] GODSJT-Record (Pos1) successfully updated, OK ");
			    			}
			    		}
			    	}
		    	}
			}
		}
		return retval;
	}
	
	
	/**
	 * 
	 * @param godsnrMgr
	 * @param overrideFromDayUserInput
	 */
	private void calculateGodsNrWithYearBevKodeDaynr(GodsnrManager godsnrMgr, String overrideFromDayUserInput){
		//-------
		//STEP 2: Calculate the godsNr with: Year + bev.kode + daynr: yyyy12345ddd
		//-------
		godsnrMgr.setGodsNrWithBevKode(godsnrMgr.getGodsNrBevKode(), overrideFromDayUserInput);
		//put the 1-character (default = 0). If std-enhets-kode exists: put it there, otherwise = default = 0
		String enhetsKode = "0";
		if(strMgr.isNotNull(godsnrMgr.getStdEnhetsKode())){
			enhetsKode = godsnrMgr.getStdEnhetsKode();
		}
		godsnrMgr.setGodsNr(godsnrMgr.getGodsNr() + enhetsKode);
		logger.info("STEP 2(godsnr):" + godsnrMgr.getGodsNr());
	}
	
	/**
	 * 
	 * @param appUser
	 * @param model
	 */
	private void setCodeDropDownMgr(SystemaWebUser appUser, Map model){
		//Sign / AVD
		model.put("signatureList", this.codeDropDownMgr.getSignatures(appUser.getUser()) );
		model.put("avdList", this.codeDropDownMgr.getAvdList(appUser.getUser()) );
		
	}
	
	
	//SERVICES
	@Qualifier ("urlCgiProxyService")
	private UrlCgiProxyService urlCgiProxyService;
	@Autowired
	@Required
	public void setUrlCgiProxyService (UrlCgiProxyService value){ this.urlCgiProxyService = value; }
	public UrlCgiProxyService getUrlCgiProxyService(){ return this.urlCgiProxyService; }
	
	
}

